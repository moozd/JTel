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

import com.jtel.common.log.Logger;
import com.jtel.mtproto.Config;
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

public class MTProtoService {

    /**
     * class instance to hold single instance of it
     */
    private static MTProtoService instance;

    /**
     * method to get current instance or create new one in case it is not created yet
     * @return object of this class
     */
    public static MTProtoService getInstance() {
        if (instance == null) {
            instance = new MTProtoService();
        }
        return instance;
    }

    /**
     * logger object to log operations for debugging
     */
    private Logger console = Logger.getInstance();

    /**
     * connected dc id of user
     */
    private int currentDcID;

    /**
     * private constructor you cannot create instance of this class using new you must cull getInstance()
     */
    private MTProtoService(){
        this.currentDcID =1;
    }


    /**
     * change current dc id
     * @param currentDcID data center id
     */
    public void setCurrentDcID(int currentDcID) {
        this.currentDcID = currentDcID;
    }

    /**
     *
     * @return get current data center id
     */
    public int getCurrentDcID() {
        return currentDcID;
    }

    /**
     *
     * @return get current data center address
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
            Transport transport = TransportFactory.Create(getCurrentDcAddress());
            TlObject ret = transport.send(method);

            console.log("rpcResult    :", ret.predicate + " (" + (System.currentTimeMillis() - time) / 1000f + "s" + ").");
            return ret;
        }catch (Exception e){
            Logger.getInstance().error(e.getMessage());
            //// TODO: 6/10/16 add error handling to notify user for this exception
        }
        return new TlObject();
    }
}
