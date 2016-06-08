package com.jtel.mtproto.tl;


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

    private static void writeIntBytes(OutputStream os, byte[] b) throws IOException {
        if(b.length %4 != 0) {
            return;
        }

        os.write(b);
    }


    public static void writeInt32(OutputStream os, int n) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(n);
        os.write(buffer.array());
    }

    public static void writeInt64(OutputStream os, int high, int low) throws IOException {
        writeInt32(os,low);
        writeInt32(os,high);
    }

    public static void writeInt64(OutputStream os, long num) throws IOException {
        writeInt64(os,(int)(num >> 32),(int)num);
    }

    public static void writeInt128(OutputStream os, byte[] n) throws IOException {
        if (n.length  * 8 != 128 ) {
            return;
        }
        writeIntBytes(os,n);
    }

    public static void writeInt256(OutputStream os, byte[] n) throws IOException {
        if (n.length * 8 != 256 ) {
            writeIntBytes(os,n);
        }
    }

    public static void writeInt512(OutputStream os, byte[] n) throws IOException {
        if (n.length * 8 != 512 ) {
            writeIntBytes(os,n);
        }
    }

    public static void writeBytes(OutputStream os , byte[] n) throws IOException {
        int len =n.length;
        if ( len <= 253) {
            os.write((byte) len);
        }
        else {
            os.write((byte)254);
            os.write((byte)(len & 0xff));
            os.write((byte)((len & 0xFF00) >> 8));
            os.write((byte)((len & 0xFF0000) >> 16));
        }
        os.write(n);
        while (len%4 !=0){
            os.write((byte)0);
            len++;
        }
    }

    public static void writeBool(OutputStream os, boolean b) throws IOException {
        if (b) {
            writeInt32(os, 0x997275b5);
        }
        else {
            writeInt32(os, 0xbc799737);
        }
    }

    public static void writeParams(OutputStream os,  List<TlParam> params ) throws IOException {
        for (TlParam param : params)
        {

            switch (param.type) {
                case "#":
                case "int":
                    writeInt32(os,param.getValue());
                    break;
                case "long":
                    writeInt64(os, param.getValue());
                    break;
                case "int128":
                    writeInt128(os, param.getValue());
                    break;
                case "int256":
                    writeInt256(os,param.getValue());
                    break;
                case "int512":
                    writeInt512(os,param.getValue());
                    break;
                case "string":
                case "bytes":
                    writeBytes(os,param.getValue());
                case "double":
                    writeInt64(os,param.getValue());
                    break;
                case "Bool":
                    writeBool(os,param.getValue());
                case "true":   return;
                default:
                    if(param.type.startsWith("Vector") || param.type.startsWith("vector")) {
                        String type = param.type.substring(param.type.indexOf("<")+1,param.type.indexOf(">"));
                        TlObject vector = TlSchemaManagerService.getInstance().getConstructor("Vector");
                        List<TlObject> items = param.getValue();

                        writeInt32(os,vector.id);
                        writeInt32(os,items.size());
                        for (TlObject object : items) {
                            os.write(object.serialize());
                        }
                    } else {
                        TlObject s = param.getValue();
                        os.write(s.serialize());
                    }


            }


        }
    }





    public static int readInt32(InputStream is) throws IOException{

        byte[] b = new byte[4];
        is.read(b);
        int num = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
        return (num <0 ) ?  num*-1:num ;
    }

    public static long readInt64(InputStream is) throws IOException{
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
       if (len == 254) {
           byte[] t = new byte[3];
           is.read(t);
          len =  t[0] | (t[1] << 8) |  (t[2] << 16);
       }
       byte[] buff = new byte[len];
       is.read(buff);
        len++;
        while (len%4 !=0){
            len++;
            is.read();
        }
       return buff;
    }

    public static boolean readBool(InputStream is) throws IOException{
        int b = readInt32(is);
        if(b==0x997275b5) {
            return true;
        }
        return false;
    }


    public static void printHexTable(byte[] data){

        for(int i=0;i<data.length;i++){
            if (i%1 == 0)System.out.print(" ");
            if (i%32 == 0) System.out.println();

            System.out.print(HexBin.encode(new byte[]{data[i]}));

        }
        System.out.println();
        System.out.println();
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
                param.setValue(readInt32(is));
                break;
            case "long":
                param.setValue(readInt64(is));
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
                param.setValue(readInt64(is));
                break;
            case "Bool":
                param.setValue(readBool(is));
            default:
                if (param.type.startsWith("Vector") || param.type.startsWith("vector")) {

                    String condType = param.type.substring(param.type.indexOf("<") + 1, param.type.indexOf(">"));
                    int cons= readInt32(is);
                    int len = readInt32(is);
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
}
