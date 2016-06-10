package com.jtel.mtproto.tl;


import com.jtel.common.log.Logger;
import com.jtel.mtproto.Config;
import com.jtel.mtproto.services.TlSchemaManagerService;
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

    private static Logger console = Logger.getInstance();
    private final static boolean DEBUG = Config.Debug;
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
        if (DEBUG)    console.log(String.format("%s<%s(padding=%s)>:%s ",field,"Bytes",pad,HexBin.encode(n)));
    }
    public static void writeParams(OutputStream os,  List<TlParam> params,String field ) throws IOException,InvalidTlParamException {
        for (TlParam param : params)
        {

            switch (param.type) {
                case "#":
                case "int":
                    if(! (param.getValue() instanceof Integer)){
                        throw new InvalidTlParamException(param);
                    }
                    writeInt(os,param.getValue(),param.name);
                    break;
                case "double":
                case "long":
                    if(! (param.getValue() instanceof Long)){
                        throw new InvalidTlParamException(param);
                    }
                    writeLong(os, param.getValue(),param.name);
                    break;
                case "int128":
                case "int256":
                case "int512":
                    if(! (param.getValue() instanceof byte[])){
                        throw new InvalidTlParamException(param);
                    }
                    writeIntBytes(os,param.getValue(),((byte[])param.getValue()).length,param.name);
                    break;
                case "string":
                case "bytes":
                    if(! (param.getValue() instanceof byte[])){
                        throw new InvalidTlParamException(param);
                    }
                    writeBytes(os,param.getValue(),param.name);
                    break;
                case "Bool":
                    if(! (param.getValue() instanceof Boolean)){
                        throw new InvalidTlParamException(param);
                    }
                    writeBool(os,param.getValue(),param.name);
                    break;
                case "true":   return;
                default:
                    if(param.type.startsWith("Vector") || param.type.startsWith("vector")) {
                        String type = param.type.substring(param.type.indexOf("<")+1,param.type.indexOf(">"));
                        TlObject vector = TlSchemaManagerService.getInstance().getConstructor("Vector");
                        if(! (param.getValue() instanceof List)){
                            throw new InvalidTlParamException(param);
                        }
                        List<TlObject> items = param.getValue();

                        writeInt(os,vector.id,type+ "(" +param.name+")");
                        writeInt(os,items.size(),"Length");
                        for (TlObject object : items) {
                            os.write(object.serialize());
                        }
                    } else {
                        TlObject s = param.getValue();
                        os.write(s.serialize());
                    }


            }

        }
        if (DEBUG) console.log("");
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
    public static List<TlParam> readParams(InputStream is,List<TlParam> params) throws IOException {

            for(TlParam param : params) {
                param = readParam(is,param);

            }
       return params;
    }
    public static TlParam readParam(InputStream is , TlParam param) throws IOException {
        switch (param.type) {
            case "#":
            case "int":
                param.setValue(readInt(is));
                break;
            case "long":
                param.setValue(readLong(is));
                break;
            case "int128":
                param.setValue(readInt128(is));
                break;
            case "int256":
                param.setValue(readInt256(is));
                break;
            case "int512":
                param.setValue(readIntBytes(is, 512));
                break;
            case "string":
            case "bytes":
                param.setValue(readBytes(is));
                break;
            case "double":
                param.setValue(readLong(is));
                break;
            case "Bool":
                param.setValue(readBool(is));
            default:
                if (param.type.startsWith("Vector") || param.type.startsWith("vector")) {

                    String condType = param.type.substring(param.type.indexOf("<") + 1, param.type.indexOf(">"));
                    int cons= readInt(is);
                    int len = readInt(is);
                    List<TlParam> t = new ArrayList<>(len);
                    for (int i = 0; i < len; i++) {
                        TlParam item = readParam(is,new TlParam(condType));
                        t.add(item.getValue());
                    }
                    param.setValue(t);
                } else {
                    TlObject o = new TlObject();
                    o.deSerialize(is);
                    param.setValue(o);
                }

        }
        return  param;
    }

    public static void printHexTable(byte[] data){

        System.out.print("len :" +data.length);
        for(int i=0;i<data.length;i++){
            if (i%1 == 0)System.out.print(" ");
            if (i%32 == 0) System.out.println();

            System.out.print(HexBin.encode(new byte[]{data[i]}));

        }
        System.out.println();
        System.out.println();
    }
}
