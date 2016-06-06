package com.jtel;

import com.jtel.mtproto.services.TlSchemaManagerService;
import com.jtel.mtproto.tl.TlObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        TlSchemaManagerService schemaManagerService = TlSchemaManagerService.getInstance();
        TlObject o =schemaManagerService.getConstructor("server_DH_params_fail",true);
        System.out.println(o.toString());
    }
}
