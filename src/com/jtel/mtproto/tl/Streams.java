package com.jtel.mtproto.tl;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

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

}
