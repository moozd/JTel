package com.jtel.mtproto.tl;


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
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
    }

    public static void writeBool(OutputStream os, boolean b) throws IOException {
        if (b) {
            writeInt32(os, 0x997275b5);
        }
        else {
            writeInt32(os, 0x997275b5);
        }
    }


    public static int readInt32(InputStream is) throws IOException{
        byte[] b = new byte[4];
        is.read(b);
        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static long readInt64(InputStream is) throws IOException{
        byte[] b = new byte[8];
        is.read(b);
        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getLong();
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
                    writeInt32(os,param.getValue());
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
                    TlObject s = param.getValue();
                    os.write(s.serialize());
            }

        }
    }

    public static void printHexTable(byte[] data){
        String a = HexBin.encode(data);
        for(int i=0;i<a.length();i++){
            if (i%2 == 0)System.out.print(" ");
            if (i%64 == 0) System.out.println();

            System.out.print(a.charAt(i));

        }
    }

}
