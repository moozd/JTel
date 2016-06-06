package com.jtel.mtproto.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jtel.mtproto.Config;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;
import com.jtel.mtproto.tl.TlSchema;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/7/16
 * Package : com.jtel.mtproto.services
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public final class TlSchemaManagerService {

    private static TlSchemaManagerService instance;

    public static TlSchemaManagerService getInstance() throws IOException {
        if(instance == null) {
            instance = new TlSchemaManagerService();
        }
        return instance;
    }

    private TlSchema mtpSchema;
    private TlSchema apiSchema;

    private TlSchemaManagerService() throws IOException{
        try(Reader reader = new FileReader(Config.mtpSchema)){
            Gson gson = new GsonBuilder().create();
            mtpSchema = gson.fromJson(reader, TlSchema.class);
        }
        try(Reader reader = new FileReader(Config.apiSchema)){
            Gson gson = new GsonBuilder().create();
            apiSchema = gson.fromJson(reader, TlSchema.class);
        }

    }

    public TlObject getConstructor(String predicate, boolean mtp) {
        List<TlObject> constructors;
        if(mtp) {
            constructors = mtpSchema.constructors;
        }
        else {
            constructors = apiSchema.constructors;
        }

        for (TlObject o : constructors) {
            if (o.predicate.equals(predicate)) {
                return o;
            }
        }
        return null;
    }

    public TlMethod getMethod(String method,boolean mtp){
        List<TlMethod> methods;
        if(mtp) {
            methods = mtpSchema.methods;
        }
        else {
            methods = apiSchema.methods;
        }

        for (TlMethod o : methods) {
            if (o.method.equals(method)) {
                return o;
            }
        }
        return null;
    }

    public TlSchema getApiSchema() {
        return apiSchema;
    }

    public TlSchema getMtpSchema() {
        return mtpSchema;
    }
}
