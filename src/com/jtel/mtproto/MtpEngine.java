
/*
 * This file is part of JTel.
 *
 *     JTel is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     JTel is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with JTel.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jtel.mtproto;

import com.jtel.common.io.FileStorage;
import com.jtel.common.log.Logger;

import com.jtel.mtproto.auth.AuthCredentials;
import com.jtel.mtproto.auth.AuthFailedException;
import com.jtel.mtproto.auth.AuthManager;

import com.jtel.mtproto.message.*;

import com.jtel.mtproto.secure.Randoms;
import com.jtel.mtproto.secure.TimeManager;
import com.jtel.mtproto.tl.*;

import com.jtel.mtproto.transport.Transport;
import com.jtel.mtproto.transport.TransportException;


import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;


/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

/**
 * MTProto service saves api current state to invoke MTP  or API  rpc method
 * to Telegram servers
 * this class is singleton
 */

public class MtpEngine {

    /**
     * class instance to hold single instance of it
     */
    private static MtpEngine instance;

    /**
     * method to getAuth current instance or create new one in case it is not created yet
     * @return object of this class
     */
    public static MtpEngine getInstance() {
        if (instance == null) {
            instance = new MtpEngine();

        }
        return instance;
    }



    /**
     * logger object to log operations for debugging
     */
    private Logger      console;

    /**
     * file layer to store state of application
     * @see com.jtel.common.io.FileStorage
     */
    private FileStorage storage;
    
    /**
     *authenticates client for each dc
     * @see com.jtel.mtproto.auth.AuthManager 
     */
    private AuthManager authManager;

    /**
     * network layer ,controls the data transfer
     * @see com.jtel.mtproto.transport.Transport
     */
    private Transport   transport;

    /**
     * if initConnection method is called this should be true
     */
    private boolean     isApiInitialized;

    /**
     * a flag for showing or not showing method name while executing theme
     */
    private boolean     verbose;

    /**
     * a flag for showing or not showing hex table of current request
     */
    private boolean     verboseTables;


    /**
     * private constructor you cannot create instance of this class using new you must cull getInstance()
     */
    private MtpEngine(){
        this.console        = Logger.getInstance();
        setApiInitialized(false);
        setVerboseEnabled(true);
        setVerboseHexTablesEnabled(false);
    }
    

    /**
     * checks if client is not authenticated to a dc then authenticates
     * @throws AuthFailedException
     */
    protected void checkCurrentDc() throws AuthFailedException{
        if (!isAuthenticatedOnDc(getDc())){
            saveAuth(getDc(),authManager.authenticate(getDc()));
        }
    }

    /**
     * save auth data for a dc
     * @param dc dc number
     * @param auth auth data to be saved
     *             @see com.jtel.mtproto.auth.AuthCredentials
     */
    protected void saveAuth(int dc, AuthCredentials auth) {
        storage.setItem("auth_state",true);
        storage.setItem("dcId",dc);
        storage.setItem("dcId_"+dc+"_auth", auth);
        storage.save();
    }

    /**
     * sending messages starts from here Encrypted or Unencrypted
     * @param message message to send to telegram server (RPC)
     *                @see com.jtel.mtproto.message.TlMessage
     * @return RPC result after deserialization 
     *         @see com.jtel.mtproto.tl.TlObject 
     */
    protected RpcResponse sendMessage(TlMessage message){
        RpcResponse rpcResponse = null;
        try {
            byte[] msg = message.serialize();
            int currentDc = getDc();
            byte[] response = transport.send(currentDc, msg);
            //console.table(response,"msg");
            message.deSerialize(new ByteArrayInputStream(response));
            rpcResponse = message.getRpcResponse();

            if(isVerbose()){
                console.log("[request]", message.getMethod().method);
                if(isVerboseTables()) console.table(msg,"#");
                console.log("[result ]",rpcResponse.getObject().predicate);
                if(isVerboseTables()) console.table(transport.getResponseAsBytes(),"#");

            }

        }
        catch (TransportException e){
            console.error("Transport error :",e.getCode(), e.getMessage());
        }
        catch (InvalidTlParamException e){
            console.error("InvalidTlParam error :",e.getMessage());
        }
        catch (Exception e){
            console.error("Unknown error :", e.getMessage());
        }


       rpcResponse = handleMessage(message);

        return rpcResponse;
    }

    /**
     * creates rpc headers for unencrypted message
     * @param dc to get auth data if it exists
     * @return Rpc header object that contains everything that
     * a message needs.
     * @see com.jtel.mtproto.message.RpcHeaders
     */
    protected RpcHeaders getMtpMessageHeaders(int dc){
        RpcHeaders headers = new RpcHeaders();

        AuthCredentials credentials = getAuth(dc);
        TimeManager.getInstance().setTimeDelta(credentials.getServerTime());
        headers.setAuthKey(credentials.getAuthKey());

        headers.setAuthKeyId(new byte[]{0,0,0,0,0,0,0,0});
        headers.setMessageId(TimeManager.getInstance().generateMessageId());
        return headers;
    }

    /**
     * creates rpc headers for encrypted messages
     * @param dc to get auth data and server time registered to this dc
     * @return rpc header object that contains everything that
     * a message serialization needs
     * @see com.jtel.mtproto.message.RpcHeaders
     */
    protected RpcHeaders getApiMessageHeaders(int dc){
        RpcHeaders headers = new RpcHeaders();

        AuthCredentials credentials = getAuth(dc);
        TimeManager.getInstance().setTimeDelta(credentials.getServerTime());
        headers.setAuthKey(credentials.getAuthKey());

        headers.setAuthKeyId(credentials.getAuthKeyId());
        headers.setServerSalt(credentials.getServerSalt());
        headers.setSessionId(storage.getItem("session_id"));
        headers.setSequenceId(TimeManager.getInstance().generateSeqNo());
        headers.setMessageId(TimeManager.getInstance().generateMessageId());

        return headers;
    }

    /**
     * init connection is a special method of telegram api it start with
     * invokeWithLayer method that determines layer number and initConnection
     * method that sends api id user agent and some other stuffs to servers
     */
    public void initConnection (){

        console.log("initializing connection");
        int currentDc = getDc();
        TlMessage method = new InitConnectionMessage(getApiMessageHeaders(currentDc));
        TlObject nearestDc =sendMessage(method).getObject();
        nearestDc.params.forEach(a-> console.log(a));
        if(!nearestDc.type.equals(method.getMethod().type) ) return;
        setDc(nearestDc.get("nearest_dc"));
        setApiInitialized(true);
    }

    /**
     * if initConnection method has been executed then it will return true
     * @return true or false
     */
    protected boolean isApiInitialized() {
        return isApiInitialized;
    }


    /**
     * determines if api is initialized (initConnection called or not)
     * @param apiInitialized initConnection called or not
     */
    protected void setApiInitialized(boolean apiInitialized) {
        isApiInitialized = apiInitialized;
    }


    /**
     * is user authenticated to telegram service?
     * @return true if storage object auth_state is true
     *         this means client has completed the authentication
     */
    protected boolean isAuthenticatedOnDc(int dc){
        return storage.hasKey("dcId_"+dc+"_auth");
    }

    /**
     * to print request and response as hex table
     * @return if true print
     */
    protected boolean isVerboseTables() {
        return verboseTables & verbose;
    }

    /**
     * to print request and response of methods to screen
     * @return if true print , true by default
     */
    protected boolean isVerbose() {
        return verbose;
    }


    /**
     * setter
     * @param storage storage object
     */
    public void setStorage(FileStorage storage) {
        this.storage = storage;
        this.authManager = new AuthManager();
    }

    /**
     * setter
     * @param verbose if true show verbose messages
     */
    public void setVerboseEnabled(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * setter
     * @param verboseTables if true show returned bytes as hex table
     */
    public void setVerboseHexTablesEnabled(boolean verboseTables) {
        this.verboseTables = verboseTables;
    }


    /**
     * setter
     * @param transport transport object to send messages over network
     */
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    /**
     * getter
     * @return true if client did authentication before
     */
    public  boolean isNetworkReady(){
        return storage.getItem("auth_state") == null ? false : storage.getItem("auth_state") ;
    }
    

    /**
     * change current dc id
     * @param currentDcID data center id
     */
    public void setDc(int currentDcID) {
        console.log("dcid changed to" , currentDcID);
        storage.setItem("dcId",currentDcID);
        storage.save();
    }


    /**
     * getter
     * @return get storage object
      */
    public FileStorage getStorage() {
        return storage;
    }

    /**
     *
     * @return getAuth current data center id
     */
    public int getDc() {
        return storage.getItem("dcId") == null ? 1 :storage.getItem("dcId");
    }


    /**
     * sendSync a mtp rpc request known as low level request that dose not require authentication
     * message that is created throw this method is known as unencrypted message.
     * @param method @see com.JTel.mtproto.tl.TlMethod
     * @return @see com.JTel.mtproto.tl.TlObject
     */
    public TlObject invokeMtpCall(TlMethod method){

            //getting transport object from factory you can chose type of transport from @see com.jtel.Config
            //by changing transport field
            return sendMessage(new UnencryptedMessage(method, getMtpMessageHeaders(getDc()))).getObject();
    }

    /**
     * sendSync a mtp rpc request known as low level request that dose not require authentication
     * message that is created throw this method is known as unencrypted message.
     * @param dcId changes current dc id.
     * @param method method to execute
     * @return TlObject
     */
    public TlObject invokeMtpCall(int dcId ,TlMethod method){
        setDc(dcId);
        return invokeMtpCall(method);
    }

    /**
     * sendSync a mtp rpc request known as high level request that requires authentication
     * message that is created throw this method is known as encrypted message.
     * @param dcId changes current dc id if client is not authenticated with this this id it will be.
     * @param method method to execute
     * @return TlObject
     */
    public TlObject invokeApiCall(int dcId,TlMethod method){
        setDc(dcId);
        return invokeApiCall(method);
    }

    /**
     * sendSync a mtp rpc request known as low level request that  requires authentication
     * message that is created throw this method is known as encrypted message.
     * @param method @see com.JTel.mtproto.tl.TlMethod
     * @return @see com.JTel.mtproto.tl.TlObject
     */
    public TlObject invokeApiCall(TlMethod method){
        try {
            checkCurrentDc();
        }
        catch (AuthFailedException e){
           // handle
        }
        if(!isApiInitialized){
            initConnection();
        }
        int currentDc = getDc();
        return sendMessage(new EncryptedMessage(method, getApiMessageHeaders(currentDc))).getObject();

    }





    /**
     * clear storage items
     */
    public void reset(){
        storage.clear();
    }

    /**
     * prepare client to start authenticate 
     * @param storage storage file
     * @param transport transport object
     * @throws AuthFailedException
     */
    public void prepare(FileStorage storage,Transport transport) throws AuthFailedException{
        prepare(storage,transport,false);
    }

    /**
     * 
     * @param storage storage object
     * @param transport transport object
     * @param reset rest if (true) storage will be removed
     * @throws AuthFailedException
     */
    public void prepare(FileStorage storage,Transport transport,boolean reset) throws AuthFailedException{
        setStorage(storage);
        setTransport(transport);
        if(reset){
            reset();
        }
        if(!isNetworkReady()){
           authenticate();
        }
    }

    /**
     * getAuth AuthStorage object by data center id
     * @param dc data center id
     * @return auth_key and server_salt
     */
    public AuthCredentials getAuth(int dc) {
        return storage.getItem("dcId_"+dc+"_auth");
    }



    /**
     * authenticates all dc ides and stores their values
     * @throws AuthFailedException
     */
    public void authenticate() throws AuthFailedException{
        int dc = getDc();
        for (int i =1;i<6;i++){
            console.log("authenticating dc" , i);
            saveAuth(i,authManager.authenticate(i));
        }
        setDc(dc);
    }

    public void authenticate(int dc) throws AuthFailedException{
        setDc(dc);
        saveAuth(dc,authManager.authenticate(dc));
    }

   public RpcResponse handleMessage(TlMessage message){
       RpcResponse response = message.getRpcResponse();
       switch (response.getObject().predicate){
           case "bad_server_salt":
               console.log(response.getObject().predicate, "changing server salt to new one.");
               int dc = getDc();
               AuthCredentials credentials = getAuth(dc);
               credentials.setServerSalt(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(response.getObject().get("new_server_salt")).array());
               saveAuth(dc,credentials);
               message.getRpcHeaders().setServerSalt(credentials.getServerSalt());
               response = sendMessage(message);
               break;
           case "bad_msg_notification":
               console.log(response.getObject(), "fixing message ides.");
               Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
               long utctime = cal.getTimeInMillis();
               //TimeManager.getInstance().setTimeDelta(((long)message.getRpcResponse().getObject().get("bad_msg_id") >> 32) -utctime);
             //   message.getRpcHeaders().setMessageId(TimeManager.getInstance().generateMessageId());
              // message.getRpcHeaders().setSequenceId(TimeManager.getInstance().generateSeqNo());
              // response = sendMessage(message);
               break;

       }

      return response;
   }



}