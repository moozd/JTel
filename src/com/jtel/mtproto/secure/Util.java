package com.jtel.mtproto.secure;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.MtpClient;
import com.jtel.mtproto.auth.AuthCredentials;
import com.jtel.mtproto.secure.aes.AESFastEngine;
import com.jtel.mtproto.secure.aes.KeyParameter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date    : 6/9/2016
 * Package : com.jtel.mtproto.secure
 *
 * using aes ige from https://github.com/ex3ndr/telegram-mt/blob/master/src/main/java/org/telegram/mtproto/secure/aes/DefaultAESImplementation.java
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */
public class Util {


    public static byte[] SHA1(byte[] src) {
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");;
            crypt.reset();
            crypt.update(src);
            return crypt.digest();
        }catch (NoSuchAlgorithmException e){

        }
        return new byte[0];
    }
    public static byte[] RSA(byte[] src, BigInteger key, BigInteger exponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new RSAPublicKeySpec(key, exponent));
            Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(src);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void AES256IGEDecrypt(byte[] src, byte[] dest, byte[] iv, byte[] key) {
        int len = src.length;
        AESFastEngine engine = new AESFastEngine();
        engine.init(false, new KeyParameter(key));

        int blocksCount = len / 16;

        byte[] curIvX = iv;
        byte[] curIvY = iv;
        int curIvXOffset = 16;
        int curIvYOffset = 0;

        for (int i = 0; i < blocksCount; i++) {
            int offset = i * 16;

            for (int j = 0; j < 16; j++) {
                dest[offset + j] = (byte) (src[offset + j] ^ curIvX[curIvXOffset + j]);
            }
            engine.processBlock(dest, offset, dest, offset);
            for (int j = 0; j < 16; j++) {
                dest[offset + j] = (byte) (dest[offset + j] ^ curIvY[curIvYOffset + j]);
            }

            curIvY = src;
            curIvYOffset = offset;
            curIvX = dest;
            curIvXOffset = offset;

            if (i % 31 == 32) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void AES256IGEEncrypt(byte[] src, byte[] dest , byte[] iv, byte[] key) {
        int len = src.length;
        AESFastEngine engine = new AESFastEngine();
        engine.init(true, new KeyParameter(key));

        int blocksCount = len / 16;

        byte[] curIvX = iv;
        byte[] curIvY = iv;
        int curIvXOffset = 16;
        int curIvYOffset = 0;

        for (int i = 0; i < blocksCount; i++) {

            int offset = i * 16;

            for (int j = 0; j < 16; j++) {
                dest[offset + j] = (byte) (src[offset + j] ^ curIvY[curIvYOffset + j]);
            }
            engine.processBlock(dest, offset, dest, offset);
            for (int j = 0; j < 16; j++) {
                dest[offset + j] = (byte) (dest[offset + j] ^ curIvX[curIvXOffset + j]);
            }

            curIvX = src;
            curIvXOffset = offset;
            curIvY = dest;
            curIvYOffset = offset;
        }
    }


    /*  sha1_a = SHA1 (msg_key + substr (auth_key, x, 32));
        sha1_b = SHA1 (substr (auth_key, 32+x, 16) + msg_key + substr (auth_key, 48+x, 16));
        sha1_с = SHA1 (substr (auth_key, 64+x, 32) + msg_key);
        sha1_d = SHA1 (msg_key + substr (auth_key, 96+x, 32));
        aes_key = substr (sha1_a, 0, 8) + substr (sha1_b, 8, 12) + substr (sha1_c, 4, 12);
        aes_iv = substr (sha1_a, 8, 12) + substr (sha1_b, 0, 8) + substr (sha1_c, 16, 4) + substr (sha1_d, 0, 8);
    */

    public static Map<String, byte[]> getAESKeyIV(byte[] messageKey, byte[] authKey, boolean toServer){
        int x = 8;
        if(toServer) x  = 0;
     //   Logger.getInstance().log("Creating key for",(toServer)?"server":"client","x is", x);
        byte[] sha1_a   = SHA1(concat(messageKey,subArray(authKey,x,32)));
        byte[] sha1_b   = SHA1(concat(subArray(authKey, 32+x, 16), messageKey, subArray(authKey, 48+x,16)));
        byte[] sha1_c   = SHA1(concat(subArray(authKey, 64+x, 32),messageKey));
        byte[] sha1_d   = SHA1(concat(messageKey, subArray(authKey, 96+x, 32)));

        byte[] aes_key  = concat(subArray(sha1_a, 0, 8),subArray(sha1_b, 8, 12),subArray(sha1_c, 4, 12));
        byte[] aes_iv   = concat(subArray(sha1_a,8,12),subArray(sha1_b,0,8),subArray(sha1_c,16,4),subArray(sha1_d,0,8));

        Map<String,byte[]> ret = new HashMap<>();
        ret.put("key",aes_key);
        ret.put("iv", aes_iv);
        return ret;
    }

    public static byte[] concat(byte[]... arr){
        int len =0;
        for (byte[] a : arr ){
            len +=a.length;
        }
        byte[] buff = new byte[len];
        int offset =0;
        for (byte[] a : arr ){
            System.arraycopy(a,0,buff,offset,a.length);
            offset+=a.length;
        }
        return  buff;
    }

    public static byte[] subArray(byte[]src,int start, int limit){
        byte[] buff = new byte[limit];

        for (int i =0; i<limit;i++){
            buff[i]=src[start++];
        }
        return buff;
    }
    public static byte[] subArray(byte[]src,int start){
        if(start < 0){
            return subArray(src,src.length+start,-1*start);
        }
        return subArray(src,start,src.length-start);
    }

    public static byte[] bytesXor(byte[]a, byte[]b){
        byte[] buff = new byte[a.length];
        for(int i=0; i<buff.length;i++){
            buff[i]=(byte)(a[i] ^ b[i]);
        }
        return buff;
    }

    public static boolean bytesCmp(byte[] oi, byte[] oj){
        if(oi.length - oj.length != 0 ) return false;
        for (int i = 0;i<oi.length;i++){
            if(oi[i] != oj[i]) return false;
        }
        return true;
    }


    public static byte[] fromBigInt(BigInteger val) {
        byte[] res = val.toByteArray();
        if (res[0] == 0) {
            byte[] res2 = new byte[res.length - 1];
            System.arraycopy(res, 1, res2, 0, res2.length);
            return res2;
        } else {
            return res;
        }

    }

    public static byte[] toByteArray(InputStream in) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int next = in.read();
        while (next > -1) {
            bos.write(next);
            next = in.read();
        }
        bos.flush();
       return bos.toByteArray();
    }

    public  static void printDc(MtpClient engine, int dc){

        AuthCredentials credentials = engine.getAuth(dc);
        Logger.getInstance().log("dc "+dc+" credentials");
        Logger.getInstance().log("server_time",credentials.getServerTime());
        Logger.getInstance().table(credentials.getAuthKeyId() ,"auth_key_id");
        Logger.getInstance().table(credentials.getServerSalt(),"server_salt");
        Logger.getInstance().table(credentials.getAuthKey()   ,"auth_key");
        System.out.println();
    }



}
