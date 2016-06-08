package com.jtel.mtproto;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.services.TimeManagerService;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.jtel.mtproto.tl.Streams.*;

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

    protected Logger l = Logger.getInstance();

    public PlainHttpTransport(String address) throws IOException {
        url=new URL("http://"+address+"/apiw1");
    }

    @Override
    public void send(TlMethod method) throws IOException {
        byte[] message = method.serialize();
        long message_id=0L;

        System.setProperty("http.keepAlive", "true");
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        writeInt64(os, 0L);
        writeInt64(os,(  message_id = TimeManagerService.getInstance().generateMessageId()));
        writeInt32(os,message.length);
        os.write(message);

        connection.getOutputStream().write(os.toByteArray());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();

        l.log("Request",method);

        printHexTable(message);


    }

    @Override
    public TlObject receive() throws IOException {
        InputStream is  = connection.getInputStream();
        long auth_id    = readInt64(is);
        long message_id = readInt64(is);
        int message_len = readInt32(is);

        byte[] response = new byte[message_len];

        is.read(response);
        ByteArrayInputStream bis = new ByteArrayInputStream(response);

        TlObject o = new TlObject();
        o.deSerialize(bis);

        l.log(  "Response", o );
        printHexTable(response);

        return o;
    }

   /*// public Socket getSocket() {
        return socket;
    }*/
}
