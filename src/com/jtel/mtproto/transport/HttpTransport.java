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
import com.jtel.mtproto.ConfStorage;
import com.jtel.mtproto.secure.Crypto;
import com.jtel.mtproto.message.TlMessage;
import com.jtel.mtproto.tl.InvalidTlParamException;

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

public class HttpTransport extends Transport {

    protected HttpURLConnection connection;
    private ConfStorage conf;

    public HttpTransport() {
        conf = ConfStorage.getInstance();
    }

    @Override
    protected byte[] onSend(byte[] message) throws IOException,InvalidTlParamException {
        connection.getOutputStream().write(message);
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        errorCode = connection.getResponseCode();
        return Crypto.toByteArray(connection.getInputStream());
    }

    @Override
    protected void createConnection(int dc) throws IOException{
        URL url =new URL("http://"+ conf.getDc(dc)+"/apiw1");
        System.setProperty("http.keepAlive", "true");
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
    }


}
