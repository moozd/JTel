package com.jtel.mtproto.tl;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;

import static com.jtel.mtproto.tl.Streams.*;
/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/7/16
 * Package : com.jtel.mtproto.tl
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class TlObject implements Tl {

    int id;
    public String predicate;
    public List<TlParam> params;
    public String type;

    public void putParam(String field, Object o) {
        for (TlParam param : params) {
            if(param.name.equals(field)){
                param.setValue(o);
                return;
            }
        }
    }

    @Override
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writeInt32(os,id);
        writeParams(os,params);
        return os.toByteArray();
    }




    @Override
    public TlObject deSerialize(InputStream is) throws IOException {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s#%s %s = %s",predicate, HexBin.encode( ByteBuffer.allocate(4).putInt(id).array()),params.toString(),type);
    }
}
