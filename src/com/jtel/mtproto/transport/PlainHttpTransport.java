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

package com.jtel.mtproto.transport;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.Config;
import com.jtel.mtproto.services.TimeManagerService;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;
import com.sun.istack.internal.Nullable;

import static com.jtel.mtproto.tl.Streams.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class PlainHttpTransport implements Transport {

    protected HttpURLConnection connection;
    protected URL url;


    protected Logger console = Logger.getInstance();
    private  boolean DEBUG = Config.Debug;

    public PlainHttpTransport(String address) throws IOException {
        url=new URL("http://"+address+"/apiw1");
    }

    @Override
    @Nullable
    public TlObject send(TlMethod method) throws IOException,InvalidTlParamException {

        byte[] message = method.serialize();


        System.setProperty("http.keepAlive", "true");
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        writeLong(os, 0L,"Auth Key");
        writeLong(os,( TimeManagerService.getInstance().generateMessageId()),"Message ID");
        writeInt(os,message.length,"Message length");
        os.write(message);

        connection.getOutputStream().write(os.toByteArray());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();


        if (DEBUG){
            console.log(method);
            printHexTable(os.toByteArray());
        }

        return  receive();

    }

    @Override
    public TlObject receive() throws IOException {
        InputStream is  = connection.getInputStream();
        long auth_id    = readLong(is);
        long message_id = readLong(is);
        int message_len = readInt(is);

        if(DEBUG){
            console.log("auth_id",auth_id);
            console.log("msg_id",message_id);
            console.log("msg_len",message_len);
        }

        byte[] responseBytes = new byte[message_len];
        is.read(responseBytes);
        ByteArrayInputStream bis = new ByteArrayInputStream(responseBytes);
        TlObject responseObject = new TlObject();
        responseObject.deSerialize(bis);


        if(DEBUG){
            console.log(responseObject);
            printHexTable(responseBytes);
        }

        return responseObject;
    }

    public int getCode(){
        try {
            return connection.getResponseCode();
        }catch (Exception e){
            return 0;
        }
    }


    /*// public Socket getSocket() {
        return socket;
    }*/
}
