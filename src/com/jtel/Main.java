package com.jtel;

import com.jtel.mtproto.services.TlSchemaManagerService;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        TlSchemaManagerService schemaManagerService = TlSchemaManagerService.getInstance();
        TlObject o = schemaManagerService.getConstructor("boolFalse");
        TlObject b = schemaManagerService.getConstructor("null");

        System.out.println(b);
    }
}
