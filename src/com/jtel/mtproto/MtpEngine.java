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

import com.jtel.mtproto.message.EncryptedMessage;
import com.jtel.mtproto.message.InitConnectionMessage;
import com.jtel.mtproto.message.TlMessage;
import com.jtel.mtproto.message.UnencryptedMessage;
import com.jtel.mtproto.tl.*;

import com.jtel.mtproto.transport.Transport;
import com.jtel.mtproto.transport.TransportException;

import java.io.ByteArrayInputStream;


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
    private Logger console = Logger.getInstance();


    ;

    private boolean isApiInitialized;

    private FileStorage storage;
    private AuthManager authManager;
    private Transport transport;
    private boolean verbose;


    /**
     * private constructor you cannot create instance of this class using new you must cull getInstance()
     */
    private MtpEngine(){
        this.isApiInitialized = false;
        this.verbose = false;
    }

    public void setStorage(FileStorage storage) {
        this.storage = storage;
        this.authManager = new AuthManager();
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    protected boolean isVerbose() {
        return verbose;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }


    /**
     * is user authenticated to telegram service?
     * @return true if storage object auth_state is true
     *         this means client has completed the authentication
     */
    protected boolean isAuthenticatedOnDc(int dc){
         return storage.hasKey("dcId_"+dc+"_auth");
    }

    public  boolean isReady(){
        return storage.getItem("auth_state");
    }

    /**
     * if initConnection method has been executed then it will return true
     * @return true or false
     */
    protected boolean isApiInitialized() {
        return isApiInitialized;
    }


    protected void setApiInitialized(boolean apiInitialized) {
        isApiInitialized = apiInitialized;
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

    public FileStorage getStorage() {
        return storage;
    }

    /**
     *
     * @return getAuth current data center id
     */
    public int getCurrentDcID() {
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
            int currentDc = getCurrentDcID();
            TlObject ret = sendMessage(new UnencryptedMessage(method));
            return ret;
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
        int currentDc = getCurrentDcID();
        AuthCredentials credentials = getAuth(currentDc);
        byte[] session_id = storage.getItem("session_id");
        return sendMessage(new EncryptedMessage(session_id,credentials,method));

    }

    public void initConnection (){

        int currentDc = getCurrentDcID();
        byte[] session_id  = storage.getItem("session_id");
        TlMessage method = new InitConnectionMessage(session_id, getAuth(currentDc));
        TlObject nearestDc =sendMessage(method);
        if(!nearestDc.type.equals(method.getMethod().type) ) return;
        setDc(nearestDc.get("nearest_dc"));
        setApiInitialized(true);
    }



    /**
     * clear storage items
     */
    public void reset(){
        storage.clear();
    }

    /**
     * getAuth AuthStorage object by data center id
     * @param dc data center id
     * @return auth_key and server_salt
     */
    public AuthCredentials getAuth(int dc) {
        return storage.getItem("dcId_"+dc+"_auth");
    }

    protected void saveAuth(int dc, AuthCredentials auth) {
        storage.setItem("auth_state",true);
        storage.setItem("dcId",dc);
        storage.setItem("dcId_"+dc+"_auth", auth);
        storage.save();
    }

    /**
     * authenticates all dc ides and stores their values
     * @throws AuthFailedException
     */
    public void authenticate() throws AuthFailedException{
        int dc = getCurrentDcID();
        for (int i =1;i<6;i++){
            saveAuth(i,authManager.authenticate(i));
        }
        setDc(dc);
    }

    public void authenticate(int dc) throws AuthFailedException{
        setDc(dc);
        saveAuth(dc,authManager.authenticate(dc));
    }

    /**
     * checks if client is not authenticated to a dc then authenticates
     * @throws AuthFailedException
     */
    protected void checkCurrentDc() throws AuthFailedException{
        if (!isAuthenticatedOnDc(getCurrentDcID())){
           saveAuth(getCurrentDcID(),authManager.authenticate(getCurrentDcID()));
        }
    }

    protected TlObject sendMessage(TlMessage message){
        RpcResponse rpcResponse = null;
        try {
            byte[] msg = message.serialize();
            int currentDc = getCurrentDcID();
            byte[] response = transport.send(currentDc, msg);
            message.deSerialize(new ByteArrayInputStream(response));
            rpcResponse = message.getRpcResponse();
            if(isVerbose()){
                console.log("rpc-request>", message.getMethod().method);
                console.table(msg,"#");
                console.log("rpc-result >",rpcResponse.getObject().predicate);
                console.table(transport.getResponseAsBytes(),"#");

            }

        }
        catch (TransportException e){
            console.error("mtp:",e.getCode(), e.getMessage());
        }
        catch (InvalidTlParamException e){
            console.log("mtp:",e.getMessage());
        }
        catch (Exception e){
            console.error("init connection:", e.getMessage());
        }


        TlObject object = new TlObject();
        if(rpcResponse != null){
            object = rpcResponse.getObject();
        }
        return object;
    }



}
