/*
 * This file is part of JTel.
 *
 *     JTel is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     JTel is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with JTel.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jtel.mtproto.tl;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.tl.schema.TlSchemaProvider;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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

    public int id;
    public String predicate;
    public List<TlParam> params;
    public String type;
    private Logger console = Logger.getInstance();
    public TlObject(){
        this.id     = 0;
        this.predicate = "unknown";
        this.params = new ArrayList<>();
        this.type   =  "unknown";
    }
    public TlObject(String predicate) throws IOException{

        TlSchemaProvider schemaManagerService = TlSchemaProvider.getInstance();
        TlObject object = schemaManagerService.getConstructor(predicate);
        this.id        = object.id;
        this.predicate = object.predicate;
        this.params    = object.params;
        this.type      = object.type;
    }
    public TlObject put(String field, Object o) {

        for (TlParam param : params) {
            if(param.name.equals(field)){
                param.setValue(o);
                return this;
            }
        }
        return this;
    }

    public <T> T get(String field){
        for (TlParam param : params) {
            if(param.name.equals(field)){

                return param.getValue() ;
            }
        }
        return null;
    }

    @Override
    public byte[] serialize() throws IOException,InvalidTlParamException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writeInt(os,id,predicate);
        writeParams(os,params,predicate+" params");
        return os.toByteArray();
    }




    @Override
    public void deSerialize(InputStream is) throws IOException {
        int id = readInt(is);
        TlObject object = TlSchemaProvider.getInstance().getConstructor(id);
        if(object == null) console.error("not found",id);
        this.id        = object.id;
        this.predicate = object.predicate;
        this.type      = object.type;
        this.params = readParams(is, object.params);
    }

    @Override
    public String toString() {
        return String.format("%s#%s  %s  = %s",predicate, HexBin.encode( ByteBuffer.allocate(4).putInt(id).array()),params.toString(),type);
    }

    public void toSystemOut(){
        console.log();
    }
}
