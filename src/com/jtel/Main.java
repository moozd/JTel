package com.jtel;

import com.jtel.mtproto.Config;
import com.jtel.mtproto.PlainTcpTransport;
import com.jtel.mtproto.services.TimeManagerService;
import com.jtel.mtproto.services.TlSchemaManagerService;
import com.jtel.mtproto.tl.Streams;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import static com.jtel.mtproto.tl.Streams.writeInt64;

public class Main {

    public static void main(String[] args) throws IOException {
        TlSchemaManagerService schemaManagerService = TlSchemaManagerService.getInstance();
        TlMethod o = schemaManagerService.getMethod("req_pq",true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] b = new byte[16];
        new Random().nextBytes(b);
        o.putParam("nonce",b);
System.out.println(Config.dcAddresses.get(1));
        PlainTcpTransport transport = new PlainTcpTransport(Config.dcAddresses.get(1));
        transport.send(o.serialize());
        //Streams.printHexTable(o.serialize());

    }


}
