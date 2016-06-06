package com.jtel.mtproto;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

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
        long auth=0;
        socket.getOutputStream().write(new byte[]{0,0,0,0,0,0,0,0});

    }

    @Override
    public byte[] receive() {
        return new byte[0];
    }

    public Socket getSocket() {
        return socket;
    }
}
