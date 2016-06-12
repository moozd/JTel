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

package com.jtel.mtproto.services;

import com.jtel.common.io.Storage;
import com.jtel.common.log.Logger;

import com.jtel.mtproto.Config;

import com.jtel.mtproto.auth.AuthCredentials;
import com.jtel.mtproto.auth.AuthFailedException;
import com.jtel.mtproto.auth.AuthManager;
import com.jtel.mtproto.auth.AuthStorage;

import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

import com.jtel.mtproto.transport.Transport;
import com.jtel.mtproto.transport.TransportFactory;


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

public class MtpService {

    /**
     * class instance to hold single instance of it
     */
    private static MtpService instance;

    /**
     * method to getCredentialsForDc current instance or create new one in case it is not created yet
     * @return object of this class
     */
    public static MtpService getInstance() {
        if (instance == null) {
            instance = new MtpService();
        }
        return instance;
    }

    /**
     * logger object to log operations for debugging
     */
    private Logger console = Logger.getInstance();


    private Storage storage;


    /**
     * private constructor you cannot create instance of this class using new you must cull getInstance()
     */
    private MtpService(){
        this.storage = AuthStorage.getInstance();
    }

    /**
     * is user authenticated to telegram service?
     * @return true if storage object auth_state is true
     *         this means client has completed the authentication
     */
    protected boolean isAuthenticatedOnDc(int dc){
         return storage.hasCache("dcId_"+dc+"_auth");
    }


    /**
     * change current dc id
     * @param currentDcID data center id
     */
    public void setCurrentDcID(int currentDcID) {
        storage.setItem("dcId",currentDcID);
        storage.save();
    }

    /**
     *
     * @return getCredentialsForDc current data center id
     */
    public int getCurrentDcID() {
        return storage.getItem("dcId");
    }

    /**
     *
     * @return getCredentialsForDc current data center address
     */
    public String getCurrentDcAddress(){
        return Config.dcAddresses.get(getCurrentDcID());
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
            Transport transport = TransportFactory.Create(getCurrentDcAddress(),false);
            TlObject ret = transport.send(method);

            console.log("rpcResult    :", ret.predicate + " (" + (System.currentTimeMillis() - time) / 1000f + "s" + ").");
            return ret;
        }catch (Exception e){
            Logger.getInstance().error(e.getMessage());
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
            checkDcBeforeMethodCall();
        }
        catch (AuthFailedException e){
           // handle
        }
        try {
            Transport transport = TransportFactory.Create(getCurrentDcAddress(), true);
            return transport.send(method);
        }
        catch (Exception e){
            //handle
        }
        return new TlObject();
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
            AuthManager manager = new AuthManager();
            manager.authenticate(i);
        }
        setCurrentDcID(dc);
    }

    /**
     * checks if client is not authenticated to a dc then authenticates
     * @throws AuthFailedException
     */
    protected void checkDcBeforeMethodCall() throws AuthFailedException{
        if (!isAuthenticatedOnDc(getCurrentDcID())){
            AuthManager manager = new AuthManager();
            manager.authenticate(getCurrentDcID());
        }
    }


}
