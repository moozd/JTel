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
import java.util.ArrayList;
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
        long time = System.currentTimeMillis();
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
                //System.out.println(o);
                return o;
            }
        }
        return null;
    }
    public TlObject getConstructor(String predicate) {
        TlObject tlObject = getConstructor(predicate,false);
        if(tlObject == null) {
            tlObject = getConstructor(predicate,true);
        }
        return tlObject;
    }
    public TlObject getConstructor(int id) {


        for (TlObject o : mtpSchema.constructors) {
            if (o.id == id) {
               // System.out.println(o);
                return o;
            }
        }
        for (TlObject o : apiSchema.constructors) {
            if (o.id == id) {
                //System.out.println(o);
                return o;
            }
        }
        return null;
    }
    public List<TlObject> getDefinitions(String type, boolean mtp) {
        List<TlObject> constructors;
        List<TlObject> definitions = new ArrayList<>();
        if(mtp) {
            constructors = mtpSchema.constructors;
        }
        else {
            constructors = apiSchema.constructors;
        }

        for (TlObject o : constructors) {
            if (o.type.equals(type)) {
                definitions.add(o);
            }
        }
        return definitions;
    }
    public TlMethod getMethod(String method){
        TlMethod tlMethod = getMethod(method,false);
        if (tlMethod == null) {
            tlMethod = getMethod(method,true);
        }
        return tlMethod;
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
//                System.out.println(o);
                return o;
            }
        }
        return null;
    }

}
