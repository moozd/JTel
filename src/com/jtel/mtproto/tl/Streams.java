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
import com.jtel.mtproto.storage.ConfStorage;
import com.jtel.mtproto.tl.schema.TlSchemaProvider;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;


/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/7/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 * Special thanks to ex3ndr ( @see https://github.com/ex3ndr )
 */

public final class Streams {

    private static ConfStorage  conf = ConfStorage.getInstance();
    private static Logger console = Logger.getInstance();
    private final static boolean DEBUG = false;

    public static void writeByte(byte v, OutputStream stream) throws IOException {
        stream.write(v);
    }

    /*-----------------------------------------------------------------------------*/
    public static void writeIntBytes(OutputStream os, byte[] b,int len,String field)    throws IOException {

        if(b.length %4 != 0) {
            return;
        }

        os.write(b);

    if (DEBUG)       console.log(String.format("%s<%s%s>:%s",field,"Int",b.length*8,HexBin.encode(b)));
    }


    public static void writeInt(OutputStream stream,int v,String field) throws IOException {
        writeByte((byte) (v & 0xFF), stream);
        writeByte((byte) ((v >> 8) & 0xFF), stream);
        writeByte((byte) ((v >> 16) & 0xFF), stream);
        writeByte((byte) ((v >> 24) & 0xFF), stream);
    }

    public static void writeLong(OutputStream stream,long v,String f) throws IOException {
        writeByte((byte) (v & 0xFF), stream);
        writeByte((byte) ((v >> 8) & 0xFF), stream);
        writeByte((byte) ((v >> 16) & 0xFF), stream);
        writeByte((byte) ((v >> 24) & 0xFF), stream);

        writeByte((byte) ((v >> 32) & 0xFF), stream);
        writeByte((byte) ((v >> 40) & 0xFF), stream);
        writeByte((byte) ((v >> 48) & 0xFF), stream);
        writeByte((byte) ((v >> 56) & 0xFF), stream);
    }

//    public static void writeInt     (OutputStream os, int n, String field)              throws IOException {
//            ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
//        buffer.putInt(n);
//        os.write(buffer.array());
//        if(field.equals("")) return;
//        if (DEBUG) console.log(String.format("%s<%s>:%s",field,"int",n));
//    }
//    public static void writeLong    (OutputStream os, long num, String field)           throws IOException {
//        byte[] bu = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(num).array();
//        os.write(bu);
//        if (DEBUG)  console.log(String.format("%s<%s>:%s",field,"long",HexBin.encode(bu)));
//    }
    public static void writeBool    (OutputStream os, boolean b,String field)           throws IOException {
        if (b) {
            writeInt(os, 0x997275b5,field+"[bool]");
        }
        else {
            writeInt(os, 0xbc799737,field+"[bool]");
        }
    }
    public static void writeString  (OutputStream os, String n,String field)            throws IOException {
        writeBytes(os,n.getBytes(),"");
        if (DEBUG)    console.log(String.format("%s<%s>:%s ",field,"String",n));

    }
    public static void writeBytes   (OutputStream os, byte[] n,String field)            throws IOException {
        int len =n.length;
        int pad =0;
        if ( len <= 253) {
            os.write((byte) len);
            pad = (len+1) %4;
        }
        else {
            os.write((byte)254);
            os.write((byte)(len & 0xff));
            os.write((byte)((len & 0xFF00) >> 8));
            os.write((byte)((len & 0xFF0000) >> 16));
            pad = (len) %4;
        }
        os.write(n);

        while (pad%4 !=0){
            os.write((byte) 0);
            pad++;
        }
        if (DEBUG && !field.equals(""))    console.log(String.format("%s<%s(padding=%s)>:%s ",field,"Bytes",pad,HexBin.encode(n)));
    }
    public static void writeObject  (OutputStream os, String type,Object obj)           throws IOException {
       GenericObject object = new GenericObject(obj);
        switch (type){
            case "#":
            case "int":
                if(! (object.getValue() instanceof Integer)){
                    throw new IOException("invalid type for int");
                }
                writeInt(os,object.getValue(),type);
                return;
            case "double":
            case "long":
                if(! (object.getValue() instanceof Long)){
                    throw new IOException("invalid type for Long/Double");
                }
                writeLong(os, object.getValue(),type);
                return;
            case "int128":
            case "int256":
            case "int512":
                if(! (object.getValue() instanceof byte[])){
                    throw new IOException("invalid type for int bytes");
                }
                writeIntBytes(os,object.getValue(),((byte[])object.getValue()).length,type);
                return;
            case "string":
                if( (object.getValue() instanceof String)){
                    writeString(os,object.getValue(),type);
                    return;
                }
            case "bytes":
//                if(! (object.getValue() instanceof byte[])){
//                    throw new IOException("invalid type for bytes/string");
//                }
                writeBytes(os,object.getValue(),type);
                return;
            case "Bool":
                if(! (object.getValue() instanceof Boolean)){
                    throw new IOException("invalid type for Bool");
                }
                writeBool(os,object.getValue(),type);
                return;
            case "true":return;
        }
     //   console.log(type);
        if(type.startsWith("Vector") || type.startsWith("vector")){
            String innerType = type.substring(type.indexOf("<")+1,type.indexOf(">"));
            if(!(object.getValue() instanceof  List)){
                throw new IOException("vector object must be array");
            }
            if(type.startsWith("Vector")){
                writeInt(os,0x1cb5c415,"vector");
            }
            List array =  object.getValue();

            writeInt(os,array.size(),"size");
            for (int i =0;i<array.size();i++) {
                writeObject(os, innerType,array.get(i));
            }
            return;
        }
        TlObject tlObject =  object.getValue();
        String predicate = tlObject.getName();
        boolean isBare = predicate.equals(type);

        if(type.startsWith("%")){
            type=type.substring(1);
        }


        if (!isBare){
            writeInt(os,tlObject.getId(),type);
        }
        for (int i =0;i<tlObject.getParams().size();i++){
       //     console.log(tlObject.getParams().get(i));
            TlParam param = tlObject.getParams().get(i);
            int  flags = tlObject.getFlags();
            String[] condType,fieldBit;
            type = param.getType();

            if (param.isConditionalType()) {
                condType = type.split("\\?");
                fieldBit = condType[0].split("\\.");
                if ((flags & 1 << Integer.parseInt(fieldBit[1])) == 0) {
                    continue;
                }
                type = condType[1];
            }
            writeObject(os,type,param.getValue());
        }

    }

    public static void writeMethod  (OutputStream os, TlMethod method) throws  IOException {
        writeInt(os,method.getId(),"method");
        for (int i =0; i<method.getParams().size();i++){
            TlParam param = method.getParams().get(i);
            int  flags = 0;
            String[] condType,fieldBit;
            String type = param.getType();

/*            if (param.isConditionalType()) {
                condType = type.split("\\?");
                fieldBit = condType[0].split("\\.");
                if ((flags & 1 << Integer.parseInt(fieldBit[1])) == 0) {
                    continue;
                }
                type = condType[1];
            }*/
            writeObject(os,type,param.getValue());
        }

    }


    /*----------------------------------------------------------------------------*/
    //@author : ex3ndr
    //thank you ex3ndr. [i did not know i have to yield to get bytes from stream].
    //these for method is taken from here
    //Source  : https://github.com/ex3ndr/telegram-tl-core/blob/master/src/main/java/org/telegram/tl/StreamingUtils.java
    //License : https://github.com/ex3ndr/telegram-tl-core/blob/master/LICENSE
    public static int    readInt  (InputStream is)      throws IOException {

       /* byte[] b = new byte[4];
        is.read(b);
        int num = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
        return num ;*/
        int a = (short)is.read();

        int b = (short)is.read();

        int c = (short)is.read();

        int d = (short)is.read();


        return a + (b << 8) + (c << 16) + (d << 24);
    }
    public static long   readUInt (InputStream stream)      throws IOException {
        long a = stream.read();
        if (a < 0) {
            throw new IOException();
        }
        long b = stream.read();
        if (b < 0) {
            throw new IOException();
        }
        long c = stream.read();
        if (c < 0) {
            throw new IOException();
        }
        long d = stream.read();
        if (d < 0) {
            throw new IOException();
        }

        return a + (b << 8) + (c << 16) + (d << 24);
    }
    public static long   readLong (InputStream is)      throws IOException {
        long a = readUInt(is);
        long b = readUInt(is);

        return a+(b<<32);
//        byte[] buff =new byte[8];
//        is.read(buff);
//        return  ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }
    public static byte[] readBytes(InputStream stream)  throws IOException {

        int count = stream.read();
        int startOffset = 1;
        if (count >= 254) {
            count = stream.read() + (stream.read() << 8) + (stream.read() << 16);
            startOffset = 4;
        }

        byte[] raw = new byte[count];
        int offset = 0;
        while (offset < raw.length) {
            int readed = stream.read(raw, offset, raw.length - offset);
            if (readed > 0) {
                offset += readed;
            } else if (readed < 0) {
                throw new IOException();
            } else {
                console.warn("waiting for connection..");
                Thread.yield();
            }
        }


        offset = (count + startOffset) % 4;
        if (offset != 0) {
            int offsetCount = 4 - offset;
            stream.skip(offsetCount);
        }

        return raw;
    }

    /*--------------------------------------------------------------------------*/

    public static boolean readBool    (InputStream is)          throws IOException {
        int b = readInt(is);
        if(b==0x997275b5) {
            return true;
        }
        return false;
    }
    public static byte[]  readInt128  (InputStream is)          throws IOException {
        return readIntBytes(is,128);
    }
    public static byte[]  readInt256  (InputStream is)          throws IOException {
        return readIntBytes(is,256);
    }
    public static byte[]  readIntBytes(InputStream is,int bits) throws IOException {

        byte[] r = new byte[bits/8];
        for (int i=0;i<(bits/8);i++){
            r[i]=(byte) is.read();
        }
        return r;
    }

    public static GenericObject readObject(InputStream is, String type) throws IOException {

        switch (type){
            case "#":
            case "int":   return new GenericObject(readInt(is));
            case "long":  return new GenericObject (readLong(is));
            case "int128":return new GenericObject(readInt128(is));
            case "int256":return new GenericObject(readInt256(is));
            case "int512":return new GenericObject(readIntBytes(is, 512));
            case "string":return new GenericObject(new String(readBytes(is),"UTF-8"));
            case "bytes": return new GenericObject(readBytes(is));
            case "double":return new GenericObject(readLong(is));
            case "Bool":  return new GenericObject(readBool(is));
            case "true":  return new GenericObject(true);
        }

        if(type.startsWith("Vector") || type.startsWith("vector")){
            if(type.charAt(0) == 'V'){
                int constructorCmp = readInt(is);
                if(constructorCmp == 0x3072cfa1){
                    byte[] gzip = readBytes(is);
                    GZIPInputStream compressed = new GZIPInputStream(new ByteArrayInputStream(gzip));
                    return readObject(compressed,type);
                }
                if (constructorCmp != 0x1cb5c415){
                    console.error("invalid vector",Integer.toHexString(constructorCmp));
                }
            }
            int len = readInt(is);
            List<Object> items = new ArrayList<>();
            if(len >0) {
                String itemType = type.substring(type.indexOf("<") + 1, type.indexOf(">"));
                for (int i = 0; i < len; i++) {
                    GenericObject object = readObject(is, itemType);

                    items.add(i,object.getValue());
                }
            }
            return new GenericObject(items);
        }

        TlObject constructorData = null;
        if(((type.length() >0)?type.charAt(0) : ' ') == '%'){
            String checkType = type.substring(1);
            constructorData = TlSchemaProvider.getInstance().getFirstDefinition(checkType);
        }
        else {

            int constructorCmp = readInt(is);
        //    console.log(Integer.toHexString(constructorCmp));
            if(constructorCmp == 0x3072cfa1){
                byte[] gzip = readBytes(is);
               // console.log("gzip unpacked.");
                GZIPInputStream compressed = new GZIPInputStream(new ByteArrayInputStream(gzip));
                return readObject(compressed,type);
            }
            constructorData = TlSchemaProvider.getInstance().getConstructor(constructorCmp);
        }

        TlObject result = new TlObject();
        result.setName(constructorData.getName());
        result.setId(constructorData.getId());

        int i,len;
        String[] condType,fieldBit;
        GenericObject value;
        TlParam param;

        len = constructorData.getParams().size();
        for(i=0;i<len;i++) {
            param =  constructorData.getParams().get(i);
            type  =  param.getType();
            if(type.equals("#")){
               result.setFlags(0);
            }
            if (param.isConditionalType()) {
                condType = type.split("\\?");
                fieldBit = condType[0].split("\\.");
                int flags = result.getFlags();
                if ((flags & 1 << Integer.parseInt(fieldBit[1])) == 0) {
                    continue;
                }
                type = condType[1];
            }

            value = readObject(is,type);

            if(param.isConditionalType() && type.equals("true")){
                result.getpFlags().addFlag(param.getName());

            }else if (type.equals("#")) {
                result.setFlags(value.getValue());
            }else {
                result.getParams().add(new TlParam(type,param.getName(),value.getValue()));
            }

        }
        return new GenericObject(result);

    }
}
