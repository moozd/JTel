
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

import com.jtel.mtproto.schedule.MessageQueue;
import com.jtel.mtproto.secure.Randoms;
import com.jtel.mtproto.secure.TimeManager;
import com.jtel.mtproto.tl.*;

import com.jtel.mtproto.transport.Transport;
import com.jtel.mtproto.transport.TransportException;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;


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

public class MtpClient {

    private final String TAG = getClass().getSimpleName();
    /**
     * class instance to hold single instance of it
     */
    private static MtpClient instance;

    /**
     * method to getAuth current instance or create new one in case it is not created yet
     * @return object of this class
     */
    public static MtpClient getInstance() {
        if (instance == null) {
            instance = new MtpClient();

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
     * sent messages history
     */
    private MessageQueue sentQueue;







    /**
     * private constructor you cannot create instance of this class using new you must cull getInstance()
     */
    private MtpClient(){
        this.console         = Logger.getInstance();
        this.sentQueue       = new MessageQueue();
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
     * is user authenticated to telegram service?
     * @return true if storage object auth_state is true
     *         this means client has completed the authentication
     */
    protected boolean isAuthenticatedOnDc(int dc){
        return storage.hasKey("dcId_"+dc+"_auth");
    }


    /**
     * setter
     * @param storage storage object
     */
    public void setStorage(FileStorage storage) {
        this.storage = storage;
    }

    /**
     * setter
     * @param transport transport object to send messages over network
     */
    protected void setTransport(Transport transport) {
        this.transport = transport;
    }

    protected void setAuthManager(AuthManager authManager) {
        this.authManager = authManager;
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
        console.log(TAG,"dcid changed to" , currentDcID);
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
     * clear storage items
     */
    public void reset(){
        storage.clear();
    }

    /**
     * createSession client to start authenticate
     * @param storage storage file
     * @param transport transport object
     * @throws AuthFailedException
     */
    public void createSession(FileStorage storage, Transport transport) throws AuthFailedException{
        createSession(storage,transport,false);
    }

    /**
     * 
     * @param storage storage object
     * @param transport transport object
     * @param reset rest if (true) storage will be removed
     * @throws AuthFailedException
     */
    public void createSession(FileStorage storage, Transport transport, boolean reset) throws AuthFailedException{
        setStorage(storage);
        setTransport(transport);
        setAuthManager(new AuthManager(transport));
        if(reset){
            reset();
        }
        if(!isNetworkReady()){
           authenticate();
           setDc(1);
        }
        getStorage().setItem("session_id", Randoms.nextRandomBytes(8));
        initConnection();

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
    protected void authenticate() throws AuthFailedException{
        for (int i =1;i<6;i++){
            authenticate(i);
        }
    }

    protected void authenticate(int dc) throws AuthFailedException{
        saveAuth(dc,authManager.authenticate(dc));
    }


    public boolean needSignIn(){
        return getStorage().getItem("need_sign_in");
    }

    public  void saveSignIn(TlObject object){
        getStorage().setItem("need_sign_in",false);
        getStorage().setItem("user_info",object);
        getStorage().save();
    }

    public TlObject getUserInfo(){
        return getStorage().getItem("user_info");
    }

    /**
     * creates rpc headers for encrypted messages
     * @param dc to get auth data and server time registered to this dc
     * @return rpc header object that contains everything that
     * a message serialization needs
     * @see MessageHeaders
     */
    public MessageHeaders createMessageHeaders(int dc,boolean contentRelated){
        MessageHeaders headers = new MessageHeaders();
        AuthCredentials credentials = getAuth(dc);
        TimeManager.getInstance().setTimeDelta(credentials.getServerTime());
        headers.setAuthKey(credentials.getAuthKey());
        headers.setAuthKeyId(credentials.getAuthKeyId());
        headers.setServerSalt(credentials.getServerSalt());
        headers.setSessionId(storage.getItem("session_id"));
        headers.setSequenceId(TimeManager.getInstance().generateSeqNo(contentRelated));
        headers.setMessageId(TimeManager.getInstance().generateMessageId());

        return headers;
    }

    /**
     * init connection is a special method of telegram api it start with
     * invokeWithLayer method that determines layer number and initConnection
     * method that sends api id user agent and some other stuffs to servers
     */
    public void initConnection (){
        console.log(TAG ,"initializing connection");
        int currentDc = getDc();
        TlMessage message = new InitConnectionMessage(createMessageHeaders(currentDc,false));
        TlObject nearestDc =sendMessage(message);
        TlMethod method = message.getContext();
        console.log(TAG,"conf    >","country="+nearestDc.get("country"),"dc="+nearestDc.get("nearest_dc"));


        if(!nearestDc.getType().equals(method.getType())) return;

        setDc(nearestDc.get("nearest_dc"));

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
        int currentDc = getDc();
       return sendMessage(new EncryptedMessage(method, createMessageHeaders(currentDc,false)));

    }


    /**
     * sending messages starts from here Encrypted or Unencrypted
     *
     * @param message message to send to telegram server (RPC)
     *                @see com.jtel.mtproto.message.TlMessage
     * @return RPC result after deserialization
     *         @see com.jtel.mtproto.tl.TlObject
     */
    protected TlObject sendMessage(TlMessage message){
       // clearPublishedResponse();
        try {
            long t = System.currentTimeMillis();
            console.log(TAG,"request >",message.getContext().getEntityName());
            byte[] msg = message.serialize();
            int currentDc = getDc();
            byte[] response = transport.send(currentDc, msg);
            message.deSerialize(new ByteArrayInputStream(response));
            console.log(TAG,"finished in ",(System.currentTimeMillis()-t)/1000F+"s");
            sentQueue.push(message);
        }
        catch (TransportException e){
            console.error(TAG,"Transport error :",e.getCode(), e.getMessage());
            e.printStackTrace();
        }
        catch (InvalidTlParamException e){
            console.error(TAG,"InvalidTlParam error :",e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e){
                console.error(TAG,"Unknown error :", e.getMessage());
            e.printStackTrace();
        }

        return  processResponse(message.getHeaders().getMessageId(),message.getResponse().getObject().getPredicate(),message.getResponse().getObject());
    }

    protected TlObject processResponse(long messageId,String predicate,TlObject response){
        //console.log(TAG,"processing message", messageId , predicate);
        switch (predicate){
            case "msg_container":
                List<TlObject> messages = response.get("messages");
               return handleMessageContainer(messages).get(messages.size()-1);
            case "message":
                TlObject body = response.get("body");
                return processResponse(response.get("msg_id"),body.getPredicate(),body);
            case "bad_server_salt":
                return handleBadServerSalt(getDc(),response.get("new_server_salt"));
            case "msgs_ack":
                List<Long> messageAck = response.get("msg_ids");
                return handleMessageAck(messageAck);

            case "rpc_result":
                return handleRpc(messageId,response);
        }
        return response;
    }

    protected List<TlObject> handleMessageContainer(List<TlObject> messages){
        List<TlObject> responses = new ArrayList<>();
        messages.stream().forEach(message ->{
            responses.add(processResponse(message.get("msg_id"),message.getPredicate(),message));
        });
        return responses;
    }

    protected TlObject handleBadServerSalt(int dc, long salt){
        AuthCredentials credentials = getAuth(dc);
        credentials.setServerSalt(salt);
        saveAuth(dc, credentials);
        TlMessage lastMessage = sentQueue.getTop();
        lastMessage.getHeaders().setServerSalt(credentials.getServerSalt());
        TlObject resend = sendMessage(lastMessage);
        return processResponse(lastMessage.getHeaders().getMessageId(),resend.getPredicate(),resend);
    }

    protected TlObject handleMessageAck(List<Long> messageIds){
        try {
            TlMessage ack = new AckMessage(createMessageHeaders(getDc(),false),new ArrayList<Long>(messageIds));
            TlObject res = sendMessage(ack);
            return processResponse(ack.getHeaders().getMessageId(),res.getPredicate(),res);
        }catch (Exception e) {
            console.error(TAG,"an error occurred while sending ack message.",e.getMessage());
            return null;
        }
    }

    protected TlObject handleRpc(long messageId,TlObject rpc){

        TlObject rpcResult = rpc.get("result");

        if(rpcResult.getPredicate().equals("rpc_error")){
            String   error =rpcResult.get("error_message");;
            switch ((int)rpcResult.get("error_code")){
                case 303:
                case 420:
                    int X =Integer.parseInt(error.substring(error.lastIndexOf("_")+1));
                    String tag = error.substring(0,error.lastIndexOf("_"));
                    switch (tag){
                        case "FLOOD_WAIT":
                            console.error(TAG,"\\",tag,"\\");
                            console.error(TAG,"you have to wait about " +Math.floor( X/60 )+" minutes.");
                            break;
                    }
                case 401:
                    console.error(TAG,"\\",error,"\\");
                    break;

            }

        }

        return rpcResult;
    }


    protected TlObject sendLongPoll(){
        console.log(TAG,"Long poll sent.");
        try{
            return invokeApiCall(new TlMethod("http_wait").put("max_delay",500).put("wait_after",500).put("max_wait",25000));
        }catch (Exception e){
            //nothing
            return null;
        }

    }

}