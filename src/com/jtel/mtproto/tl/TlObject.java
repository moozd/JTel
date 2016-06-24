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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

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

    private int     id;
    private String  predicate;
    private String  type;
    private TlpFlags pFlags;
    private int     flags;
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
        this.pFlags = object.getpFlags();
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

    public String getPredicate() {
        return predicate;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public TlpFlags getpFlags() {
        return pFlags;
    }

    public String getType() {
        return type;
    }

    public List<TlParam> getParams() {
        return params;
    }

    @Override
    public String getEntityName() {
        return predicate;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public TlType getEntityType() {
        return TlType.Object;
    }

    public <T> T get(String field){
        for (TlParam param : params) {
            if(param.getName().equals(field)){

                return param.getValue() ;
            }
        }
        return null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    @Override
    public byte[] serialize() throws IOException,InvalidTlParamException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writeInt(os,id,predicate);
        writeParams(os,params,predicate+" params");
        return os.toByteArray();
    }


    public void deserializeBare(InputStream is, String type) throws IOException{
        TlObject object = TlSchemaProvider.getInstance().getFirstDefinition(type.substring(1));
        if(object == null){
            console.error("not found",type);
            return;
        }
      //  console.log(object.predicate);
        this.id        = object.id;
        this.predicate = object.predicate;
        this.type      = object.type;
        this.params    = object.params;
        restoreParams(is);
    }

    @Override
    public void deSerialize(InputStream is) throws IOException {
        int  id = readInt(is);
        TlObject object = null;
        try {
            object = (TlObject) TlSchemaProvider.getInstance().getConstructor(id).clone();
        }catch (CloneNotSupportedException e){
            console.error(e);
        }
        if(object == null){
            console.error(getClass().getSimpleName(),"invalid constructor id","0x"+Integer.toHexString(id));
            return;
        }
        this.id        = object.id;
        this.predicate = object.predicate;
        this.type      = object.type;
        this.params    = object.params;
        this.pFlags    = new TlpFlags();
        setFlags(0);
        restoreParams(is);
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

    private void restoreParams(InputStream is) throws IOException{
        for (Iterator<TlParam> iterator = params.iterator(); iterator.hasNext(); ) {
            TlParam toProcess = iterator.next();
            TlParam processed = readParam(is, toProcess, getFlags());
            if (processed.isConditionalType() && processed.getValue().equals("skip")) {
                iterator.remove();
                continue;
            }
            if (processed.getType().equals("#")) {
                setFlags(processed.getValue());
                iterator.remove();

            } else if (processed.isConditionalType() && processed.getValue().equals("true")) {
                getpFlags().addFlag(processed.getName());
                iterator.remove();
            } else {
                toProcess = processed;
            }
        }
        checkAndReadGzip();
    }


    private void checkAndReadGzip() throws IOException {
        if(predicate.equals("gzip_packed")){
            ByteArrayInputStream bis = new ByteArrayInputStream(get("packed_data"));
            GZIPInputStream gis = new GZIPInputStream(bis);
            deSerialize(gis);
        }
    }


    @Override
    public String toString() {
        return String.format("%s#%s (flags:%s,%s) %s  = %s ",predicate ,Integer.toHexString(id),getFlags() ,getpFlags(),params.toString(),type);
    }

    public void toSystemOut(){
        console.log();
    }
}
