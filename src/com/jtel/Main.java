package com.jtel;

import com.jtel.mtproto.services.TlSchemaManagerService;
import com.jtel.mtproto.tl.TlMethod;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        TlSchemaManagerService schemaManagerService = TlSchemaManagerService.getInstance();
        TlMethod o =schemaManagerService.getMethod("ping_delay_disconnect",true);
        System.out.println(o);
    }
}
