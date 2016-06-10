package com.jtel.mtproto.transport;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.Config;
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


    protected Logger console = Logger.getInstance();
    private  boolean DEBUG = Config.DEBUG;

    public PlainHttpTransport(String address) throws IOException {
        url=new URL("http://"+address+"/apiw1");
    }

    @Override
    public TlObject send(TlMethod method) throws IOException {
        byte[] message = method.serialize();
        long message_id=0L;


        System.setProperty("http.keepAlive", "true");
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        //connection.setRequestProperty("Content-Type",null);
        //connection.setRequestProperty("Accept",null);
        connection.setRequestMethod("POST");

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        writeLong(os, 0L,"Auth Key");
        writeLong(os,(  message_id = TimeManagerService.getInstance().generateMessageId()),"Message ID");
        writeInt(os,message.length,"Message length");
        os.write(message);

        connection.getOutputStream().write(os.toByteArray());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();

        console.log(method);

        if (DEBUG)printHexTable(os.toByteArray());
        return receive();

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

        console.log(responseObject);
        if(DEBUG) printHexTable(responseBytes);

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
