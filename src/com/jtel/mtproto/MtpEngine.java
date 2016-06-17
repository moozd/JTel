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

import com.jtel.mtproto.tl.*;

import com.jtel.mtproto.transport.HttpTransport;
import com.jtel.mtproto.transport.Transport;


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
     * method to getCredentialsForDc current instance or create new one in case it is not created yet
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


    /**
     * private constructor you cannot create instance of this class using new you must cull getInstance()
     */
    private MtpEngine(){
        this.isApiInitialized = false;
    }

    public void setStorage(FileStorage storage) {
        this.storage = storage;
        this.authManager = new AuthManager(storage);
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

    /**
     * if initConnection method has been executed then it will return true
     * @return true or false
     */
    public boolean isApiInitialized() {
        return isApiInitialized;
    }


    protected void setApiInitialized(boolean apiInitialized) {
        isApiInitialized = apiInitialized;
    }

    /**
     * change current dc id
     * @param currentDcID data center id
     */
    public void setCurrentDcID(int currentDcID) {
        storage.setItem("dcId",currentDcID);
        storage.save();
    }

    public FileStorage getStorage() {
        return storage;
    }

    /**
     *
     * @return getCredentialsForDc current data center id
     */
    public int getCurrentDcID() {
        return storage.getItem("dcId") == null ? 1 :storage.getItem("dcId");
    }


    /**
     * send a mtp rpc request known as low level request that dose not require authentication
     * message that is created throw this method is known as unencrypted message.
     * @param method @see com.JTel.mtproto.tl.TlMethod
     * @return @see com.JTel.mtproto.tl.TlObject
     */
    public TlObject invokeMtpCall(TlMethod method){

        try {
            long time = System.currentTimeMillis();

            console.log("invokeMtpCall:", method.method);
            //getting transport object from factory you can chose type of transport from @see com.jtel.Config
            //by changing transport field
            int currentDc = getCurrentDcID();
            TlObject ret = transport.send(currentDc, new UnencryptedMessage(method));

            console.log("rpcResult    :", ret.predicate + " (" + (System.currentTimeMillis() - time) / 1000f + "s" + ").");
            return ret;
        }catch (Exception e){
            console.error("mtp call:",e.hashCode(), e.getMessage());
            //// TODO: 6/10/16 add error handling to notify user for this exception
        }
        return new TlObject();
    }

    /**
     * send a mtp rpc request known as low level request that dose not require authentication
     * message that is created throw this method is known as unencrypted message.
     * @param dcId changes current dc id.
     * @param method method to execute
     * @return TlObject
     */
    public TlObject invokeMtpCall(int dcId ,TlMethod method){
        setCurrentDcID(dcId);
        return invokeMtpCall(method);
    }

    /**
     * send a mtp rpc request known as high level request that requires authentication
     * message that is created throw this method is known as encrypted message.
     * @param dcId changes current dc id if client is not authenticated with this this id it will be.
     * @param method method to execute
     * @return TlObject
     */
    public TlObject invokeApiCall(int dcId,TlMethod method){
        setCurrentDcID(dcId);
        return invokeApiCall(method);
    }

    /**
     * send a mtp rpc request known as low level request that  requires authentication
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


        try {

            int currentDc = getCurrentDcID();
            AuthCredentials credentials = getCredentialsForDc(currentDc);
            long session_id = storage.getItem("session_id");
            return transport.send(currentDc, new EncryptedMessage(session_id,credentials,method));
        }
        catch (Exception e){
            console.error("api call:",e.hashCode(), e.getMessage());
        }
        return new TlObject();
    }

    public void initConnection (){
        try {

            if(!isApiInitialized()){
                int currentDc = getCurrentDcID();
                long session_id = storage.getItem("session_id");
                TlObject nearestDc = transport.send(currentDc, new InitConnectionMessage(session_id,getCredentialsForDc(currentDc)));
                setCurrentDcID(nearestDc.get("nearest_dc"));
                setApiInitialized(true);
            }
        }
        catch (Exception e){
             console.error("init connection:",e.hashCode(), e.getMessage());
        }
    }


    /**
     * clear storage items
     */
    public void reset(){
        storage.clear();
    }

    /**
     * getCredentialsForDc AuthStorage object by data center id
     * @param dc data center id
     * @return auth_key and server_salt
     */
    public AuthCredentials getCredentialsForDc(int dc) {
        return storage.getItem("dcId_"+dc+"_auth");
    }

    /**
     * authenticates all dc ides and stores their values
     * @throws AuthFailedException
     */
    public void authenticateForAll() throws AuthFailedException{
        int dc = getCurrentDcID();
        for (int i =1;i<6;i++){
            authManager.authenticate(i);
        }
        setCurrentDcID(dc);
    }

    /**
     * checks if client is not authenticated to a dc then authenticates
     * @throws AuthFailedException
     */
    protected void checkCurrentDc() throws AuthFailedException{
        if (!isAuthenticatedOnDc(getCurrentDcID())){

            authManager.authenticate(getCurrentDcID());
        }
    }


}
