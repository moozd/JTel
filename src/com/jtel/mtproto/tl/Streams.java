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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;


/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/7/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public final class Streams {

    private static ConfStorage  conf = ConfStorage.getInstance();
    private static Logger console = Logger.getInstance();
    private final static boolean DEBUG = conf.debug();
    public static void writeIntBytes(OutputStream os, byte[] b,int len,String field) throws IOException {

        if(b.length %4 != 0) {
            return;
        }

        os.write(b);

    if (DEBUG)       console.log(String.format("%s<%s%s>:%s",field,"Int",b.length*8,HexBin.encode(b)));
    }
    public static void writeInt(OutputStream os, int n, String field) throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(n);
        os.write(buffer.array());
        if(field.equals("")) return;
        if (DEBUG) console.log(String.format("%s<%s>:%s",field,"int",n));
    }
    public static void writeLong(OutputStream os, long num, String field) throws IOException {
        byte[] bu = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(num).array();
        os.write(bu);
        if (DEBUG)  console.log(String.format("%s<%s>:%s",field,"long",HexBin.encode(bu)));
    }

    public static void writeBool(OutputStream os, boolean b,String field) throws IOException {
        if (b) {
            writeInt(os, 0x997275b5,field+"[bool]");
        }
        else {
            writeInt(os, 0xbc799737,field+"[bool]");
        }
    }

    public static void writeString(OutputStream os, String n,String field) throws IOException{
        writeBytes(os,n.getBytes(),"");
        if (DEBUG)    console.log(String.format("%s<%s>:%s ",field,"String",n));

    }

    public static void writeBytes(OutputStream os , byte[] n,String field) throws IOException {
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
    public static void writeParams(OutputStream os, List<TlParam> params, String field ) throws IOException,InvalidTlParamException {
        for (TlParam param : params)
        {
            writeParam(os,param);


        }
        if (DEBUG) console.log("");
    }


    public static void writeParam(OutputStream os,TlParam param)throws IOException,InvalidTlParamException {
       // console.log(param.getName(),param.getType(),param.getValue());
        switch (param.getType()) {
            case "#":
            case "int":
                if(! (param.getValue() instanceof Integer)){
                    throw new InvalidTlParamException(param);
                }
                writeInt(os,param.getValue(),param.getName());
                break;
            case "double":
            case "long":
                if(! (param.getValue() instanceof Long)){
                    throw new InvalidTlParamException(param);
                }
                writeLong(os, param.getValue(),param.getName());
                break;
            case "int128":
            case "int256":
            case "int512":
                if(! (param.getValue() instanceof byte[])){
                    throw new InvalidTlParamException(param);
                }
                writeIntBytes(os,param.getValue(),((byte[])param.getValue()).length,param.getName());
                break;
            case "string":
                if( (param.getValue() instanceof String)){
                    writeString(os,param.getValue(),param.getName());
                    break;
                }
            case "bytes":
                if(! (param.getValue() instanceof byte[])){
                    throw new InvalidTlParamException(param);
                }
                writeBytes(os,param.getValue(),param.getName());
                break;
            case "Bool":
                if(! (param.getValue() instanceof Boolean)){
                    throw new InvalidTlParamException(param);
                }
                writeBool(os,param.getValue(),param.getName());
                break;
            case "true":   return;
            default:
                if(param.getType().startsWith("Vector") || param.getType().startsWith("vector")) {
                    String type = param.getType().substring(param.getType().indexOf("<")+1,param.getType().indexOf(">"));
                    TlObject vector = TlSchemaProvider.getInstance().getConstructor("vector");
                    if(! (param.getValue() instanceof List)){
                        throw new InvalidTlParamException(param);
                    }
                    List<TlObject> items = param.getValue();

                    writeInt(os,vector.getId(),type+ "(" +param.getName()+")");
                    writeInt(os,items.size(),"Length");
                    for(int i =0;i<items.size();i++) {
                        TlParam parameter = new TlParam(type);
                        parameter.setValue(items.get(i));
                        writeParam(os,parameter);
                    }
                } else {
                    Tl s = param.getValue();
                    os.write(s.serialize());
                }


        }
    }




    public static int readInt(InputStream is) throws IOException{

        byte[] b = new byte[4];
        is.read(b);
        int num = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
        return num ;
    }
    public static long readLong(InputStream is) throws IOException{
        byte[] b = new byte[8];
        is.read(b);
        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }
    public static byte[] readInt128(InputStream is) throws IOException{
       return readIntBytes(is,128);
    }
    public static byte[] readInt256(InputStream is) throws IOException{
        return readIntBytes(is,256);
    }
    public static byte[] readIntBytes(InputStream is,int bits) throws IOException{

        byte[] r = new byte[bits/8];
        for (int i=0;i<(bits/8);i++){
            r[i]=(byte) is.read();
        }
        return r;
    }
    public static byte[] readBytes(InputStream is) throws IOException{

           int len = is.read();
            int pad = 0;
           if (len == 254) {
               byte[] t = new byte[3];
               is.read(t);
              len =  t[0] | (t[1] << 8) |  (t[2] << 16);
               pad = (len) %4;
           }
            else {
               pad = (len +1) %4;
           }

           byte[] buff = new byte[len];

           is.read(buff);
           while (pad%4 != 0){
               is.read();
               pad++;
           }
           return buff;
    }
    public static boolean readBool(InputStream is) throws IOException{
        int b = readInt(is);
        if(b==0x997275b5) {
            return true;
        }
        return false;
    }
    public static TlParam readParam(InputStream is , TlParam param , int flags) throws IOException {
      //  console.log(param.type);
        switch (param.getType()) {
            case "#":
            case "int":
                param.setValue(readInt(is));
                return param;
            case "long":
                param.setValue(readLong(is));
                return param;
            case "int128":
                param.setValue(readInt128(is));
                return param;
            case "int256":
                param.setValue(readInt256(is));
                return param;
            case "int512":
                param.setValue(readIntBytes(is, 512));
                return param;
            case "string":
                param.setValue(new String(readBytes(is),"UTF-8"));
                return param;
            case "bytes":
                param.setValue(readBytes(is));
                return param;
            case "double":
                param.setValue(readLong(is));
                return param;
            case "Bool":
                param.setValue(readBool(is));
                return param;
            case "true":
                return param;
            default:
                if (param.getType().startsWith("Vector") || param.getType().startsWith("vector")) {

                    String condType = param.getType().substring(param.getType().indexOf("<") + 1, param.getType().indexOf(">"));
                    if(param.getType().startsWith("V")){
                        int cons = readInt(is);
                        if(cons == 0x3072cfa1){
                            console.error("gzip vector");
                        }
                    }
                    int len = readInt(is);
                    List<TlParam> t = new ArrayList<>(len);
                    for (int i = 0; i < len; i++) {
                        TlParam item = readParam(is, new TlParam(condType),0);
                        t.add(item.getValue());
                    }
                    param.setValue(t);
                    return param;
                } else if(param.getType().startsWith("%")) {
                    TlObject o = new TlObject();
                    o.deserializeBare(is,param.getType());
                    param.setValue(o);
                    return param;
                } else if(param.isConditionalType()) {
                    String type = param.getType();
                    String[] condType = type.split("\\?");
                    String[] fieldBit = condType[0].split("\\.");
                    int bit = 1 << Integer.parseInt(fieldBit[1]);
                    int pFlag = ((flags & bit));
                    if (pFlag == 0){
                     //   console.log("cond type skip",type);
                        param.setValue("skip");
                        return param;
                    }
                    if (condType[1].equals("true")) {
                       // console.log("cond type not skip",type);
                        param.setValue("true");
                        return param;
                    }
                   // console.log("cond object",condType[1],type);
                    param.setValue(readParam(is, new TlParam(condType[1]),0).getValue());
                    param.setType(condType[1]);
                    return param;
                }
                TlObject o = new TlObject();
                o.deSerialize(is);
                param.setValue(o);
                return  param;
        }

    }


}
