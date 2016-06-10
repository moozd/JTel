package com.jtel;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.secure.Crypto;
import com.jtel.mtproto.secure.PublicKeyHolder;
import com.jtel.mtproto.secure.Randoms;
import com.jtel.mtproto.services.AuthManagerService;
import com.jtel.mtproto.services.MtprotoService;
import com.jtel.mtproto.pq.Pq;
import com.jtel.mtproto.pq.PqSolver;
import com.jtel.mtproto.tl.Streams;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.IOException;
import java.security.MessageDigest;

public class Main {

    public static void main(String[] args) throws Exception {
        AuthManagerService service = AuthManagerService.getInstance();
        service.authenticate(2);

       /* byte[] arr = Randoms.nextRandomBytes(50);
        Streams.printHexTable(arr);
        Streams.printHexTable(Crypto.subArray(arr,0,2));*/

 /*       TlObject p_q_inner_data = new TlObject("p_q_inner_data");
        p_q_inner_data.put("pq"          ,HexBin.decode("17ED48941A08F981"));
        p_q_inner_data.put("p"           ,HexBin.decode("494C553B"));
        p_q_inner_data.put("q"           ,HexBin.decode("53911073"));
        p_q_inner_data.put("nonce"       ,HexBin.decode("3E0549828CCA27E966B301A48FECE2FC"));
        p_q_inner_data.put("server_nonce",HexBin.decode("A5CF4D33F4A11EA877BA4AA573907330"));
        p_q_inner_data.put("new_nonce"   ,HexBin.decode("311C85DB234AA2640AFC4A76A735CF5B1F0FD68BD17FA181E1229AD867CC024D"));

       ;
        byte[] data = p_q_inner_data.serialize();
        byte[] hash = Crypto.SHA1(data);
        byte[] pad = Randoms.nextRandomBytes(255 - hash.length- data.length);
        byte[] data_with_hash = Crypto.concat(hash,data,pad);
        Streams.printHexTable(data_with_hash);
    *//*    byte[] e = Crypto.RSA(data_with_hash, PublicKeyHolder.modulus,PublicKeyHolder.exponent);
        byte[] a = Crypto.RSA(e,PublicKeyHolder.modulus,PublicKeyHolder.exponent);
        Logger.getInstance().log(HexBin.encode(data_with_hash));
        Logger.getInstance().log(HexBin.encode(e));
        Logger.getInstance().log(HexBin.encode(a));*/
    }

}
