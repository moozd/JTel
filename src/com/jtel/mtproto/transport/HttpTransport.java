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

import com.jtel.mtproto.ConfStorage;
import com.jtel.mtproto.tl.TlMessage;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.tl.TlObject;
import com.sun.istack.internal.Nullable;

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

public class HttpTransport implements Transport {

    protected HttpURLConnection connection;
    private ConfStorage conf;

    public HttpTransport() {
        conf = ConfStorage.getInstance();
    }


    protected void createConnection(int dc) throws IOException{
        URL url =new URL("http://"+ conf.getDc(dc)+"/apiw1");
        System.setProperty("http.keepAlive", "true");
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
    }

    @Override
    @Nullable
    public TlObject send(int dc , TlMessage message) throws IOException,InvalidTlParamException {
        createConnection(dc);
        connection.getOutputStream().write(message.serialize()
        );
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        message.deSerialize(connection.getInputStream());
        return  message.getResponse();

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
