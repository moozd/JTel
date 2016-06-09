package com.jtel.mtproto.secure;

import com.jtel.mtproto.secure.aes.AESFastEngine;
import com.jtel.mtproto.secure.aes.KeyParameter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date    : 6/9/2016
 * Package : com.jtel.mtproto.secure
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */
public class Crypto {


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


    public void AES256IGEDecrypt(byte[] src, byte[] dest, int len, byte[] iv, byte[] key) {
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


    public void AES256IGEEncrypt(byte[] src, byte[] dest, int len, byte[] iv, byte[] key) {
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



}
