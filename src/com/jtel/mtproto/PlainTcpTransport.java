package com.jtel.mtproto;

import com.jtel.mtproto.services.TimeManagerService;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.jtel.mtproto.tl.Streams.*;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class PlainTcpTransport implements Transport {

    protected Socket socket;

    public PlainTcpTransport(String address) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(address,443),3000);
        socket.setKeepAlive(true);
        socket.getOutputStream().write(0x7f);
    }

    @Override
    public void send(byte[] message) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writeInt64(os, 0L);
        writeInt64(os, TimeManagerService.getInstance().generateMessageId());
        writeInt32(os,message.length);
        os.write(message);
        printHexTable(os.toByteArray());

    }

    @Override
    public byte[] receive() throws IOException {
        InputStream is = socket.getInputStream();
        long auth_id    = readInt64(is);
        long message_id = readInt64(is);
        int message_len = readInt32(is);

        byte[] response = new byte[message_len];

        is.read(response);

        return  response;
    }

    public Socket getSocket() {
        return socket;
    }
}
