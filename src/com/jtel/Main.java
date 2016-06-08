package com.jtel;

import com.jtel.mtproto.Config;
import com.jtel.mtproto.PlainHttpTransport;
import com.jtel.mtproto.services.TlSchemaManagerService;
import com.jtel.mtproto.tl.TlMethod;

import java.io.IOException;
import java.util.Random;

import static com.jtel.mtproto.tl.Streams.writeInt64;

public class Main {

    public static void main(String[] args) throws IOException {

        TlSchemaManagerService schemaManagerService = TlSchemaManagerService.getInstance();
        TlMethod o = schemaManagerService.getMethod("req_pq",true);


        byte[] b = new byte[16];
        new Random().nextBytes(b);

        o.putParam("nonce",b);

        PlainHttpTransport transport = new PlainHttpTransport(Config.dcAddresses.get(1));
        transport.send(o);
        transport.receive();

    }


}
