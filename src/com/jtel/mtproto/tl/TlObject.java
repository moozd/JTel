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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    private int      id;
    private int      flags;
    private String predicate;
    private String   type;
    private TlpFlags pFlags;
    private List<TlParam> params;


    private Logger console = Logger.getInstance();

    public TlObject(){
        this.id     = 0;
        this.predicate = "unknown";
        this.params = new ArrayList<>();
        this.type   =  "unknown";
        this.pFlags = new TlpFlags();
    }

    public TlObject(String predicate) throws IOException{

        TlSchemaProvider schemaManagerService = TlSchemaProvider.getInstance();
        TlObject object = schemaManagerService.getConstructor(predicate);
        this.id        = object.id;
        this.predicate = object.predicate;
        this.params    = object.params;
        this.type      = object.type;
        this.pFlags = object.getpFlags();
    }

    public TlObject(int id) throws IOException{

        TlSchemaProvider schemaManagerService = TlSchemaProvider.getInstance();
        TlObject object = schemaManagerService.getConstructor(id);
        this.id        = object.id;
        this.predicate = object.predicate;
        this.params    = object.params;
        this.type      = object.type;
        this.pFlags    = object.getpFlags();
    }

    public TlObject put(String field, Object o) {

        for (TlParam param : params) {
            if(param.getName().equals(field)){
                param.setValue(o);
                return this;
            }
        }
        return this;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return predicate;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getFlags() {
        return flags;
    }

    public TlpFlags getpFlags() {
        return pFlags;
    }

    public List<TlParam> getParams() {
        return params;
    }


    public <T> T get(String field){
        for (TlParam param : params) {
            if(param.getName().equals(field)){

                return param.getValue() ;
            }
        }
        return null;
    }


    public void setParams(List<TlParam> params) {
        this.params = params;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.predicate = name;
    }


    @Override
    public byte[] serialize() throws IOException,InvalidTlParamException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writeObject(os,type,this);
        return os.toByteArray();
    }



    @Override
    public void deSerialize(InputStream is) throws IOException {

        try {
            TlObject obj = (TlObject)((TlObject) readObject(is, "").getValue()).clone();
            this.id = obj.id;
            this.predicate = obj.predicate;
            this.type = obj.type;
            this.params = obj.params;
            this.pFlags = obj.pFlags;
            this.flags = obj.flags;
        }catch (Exception e){

        }
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        TlObject object = new TlObject();
        object.id= id;
        object.pFlags = pFlags;
        object.flags  = flags;
        object.predicate = predicate;
        object.params = params;
        object.type =type;
        return object;
    }


    @Override
    public String toString() {
        return String.format("%s#%s (flags:%s,%s) %s", predicate,Integer.toHexString(id),getFlags() ,getpFlags(),params);
    }

}
