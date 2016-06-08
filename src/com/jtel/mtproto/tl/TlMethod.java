package com.jtel.mtproto.tl;

import com.jtel.mtproto.services.TlSchemaManagerService;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.jtel.mtproto.tl.Streams.writeInt32;
import static com.jtel.mtproto.tl.Streams.writeParams;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/7/16
 * Package : com.jtel.mtproto.tl
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class TlMethod implements Tl {
    int id;
    public String method;
    public List<TlParam> params;
    public String type;

    public TlMethod(String methodName) throws IOException{
        TlSchemaManagerService schemaManagerService = TlSchemaManagerService.getInstance();
        TlMethod method = schemaManagerService.getMethod(methodName);
        this.id     = method.id;
        this.method = method.method;
        this.params = method.params;
        this.type   = method.type;
    }
    public  TlMethod(){
        method ="unknown";
        params = new ArrayList<>();
        type   = "unknown";
    }
    @Nullable
    public TlMethod put(String field, @Nullable Object o) {
        for (TlParam param : params) {
            if(param.name.equals(field)){
                param.setValue(o);
                return this;
            }
        }
        return this;
    }

    @Override
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writeInt32(os,id);
        writeParams(os,params);
        return os.toByteArray();
    }




    @Override
    public void deSerialize(InputStream is) throws IOException {
        return;
    }

    @Override
    public String toString() {
        return String.format("\n%s#%s \n%s = %s",method, HexBin.encode( ByteBuffer.allocate(4).putInt(id).array()),params.toString(),type);
    }
}
