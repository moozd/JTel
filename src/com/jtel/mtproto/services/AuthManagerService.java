package com.jtel.mtproto.services;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.pq.Pq;
import com.jtel.mtproto.pq.PqSolver;
import com.jtel.mtproto.secure.Crypto;
import com.jtel.mtproto.secure.PublicKeyStorage;
import com.jtel.mtproto.secure.Randoms;
import com.jtel.mtproto.services.data.AuthBag;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.tl.Streams;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

import static com.jtel.mtproto.secure.Crypto.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto.services
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public final class AuthManagerService {

    private static  AuthManagerService instance;

    private AuthManagerService() {
        initialize();
    }

    public static AuthManagerService getInstance() {

        if (instance == null) {
            instance = new AuthManagerService();
        }

        return instance;
    }

    private Logger console = Logger.getInstance();


    private HashMap<Integer,AuthBag> authBags;

    private void initialize() {
        authBags = new HashMap<>();
    }

    public AuthBag get(int dc) {

        if (! authBags.containsKey(dc)) {
            return null;
        }

        return authBags.get(dc);
    }

    public void save(int dc, AuthBag auth) {
        authBags.put(dc, auth);
    }


    public void authenticate(int dcid) throws IOException,InvalidTlParamException{

        MtprotoService mtproto = MtprotoService.getInstance();
        mtproto.setCurrentDcID(dcid);

        TlObject resPq = mtproto.invokeMtpCall
                (
                        new TlMethod("req_pq")
                                .put("nonce", Randoms.nextRandomBytes(16))
                );
        byte[]      nonce                   =  resPq.get("nonce");
        byte[]      server_nonce            =  resPq.get("server_nonce");
        Pq          pq                      =  new Pq(resPq.get("pq"));
        resPq.get("pq");

        List<Long>  public_key_fingerprints =  resPq.get("server_public_key_fingerprints");

        pq =  PqSolver.Solve(pq);

        byte[] new_nonce = Randoms.nextRandomBytes(32);
        TlObject p_q_inner_data = new TlObject("p_q_inner_data")
                .put("pq"          ,pq.pq)
                .put("p"           ,pq.p)
                .put("q"           ,pq.q)
                .put("nonce"       ,nonce)
                .put("server_nonce", server_nonce)
                .put("new_nonce"   ,new_nonce);

        byte[] pqInner = p_q_inner_data.serialize();
        byte[] hash = Crypto.SHA1(pqInner);
        byte[] padd = Randoms.nextRandomBytes(255 - hash.length- pqInner.length);
        byte[] data_with_hash = Crypto.concat(hash,pqInner,padd);
        byte[] encrypted_data = Crypto.RSA(data_with_hash, PublicKeyStorage.modulus, PublicKeyStorage.exponent);

        TlObject Server_Dh_Params = mtproto.invokeMtpCall
                (
                        new TlMethod("req_DH_params")
                        .put("nonce",nonce)
                        .put("server_nonce",server_nonce)
                        .put("p",pq.p)
                        .put("q",pq.q)
                        .put("public_key_fingerprint",public_key_fingerprints.get(0))
                        .put("encrypted_data",encrypted_data)
                );

        if(Server_Dh_Params.predicate.equals("server_DH_params_fail")){
            console.error("dh key exchange failed","retrying...");
            authenticate(dcid);
            return;
        }

        byte[] encrypted_answer = Server_Dh_Params.get("encrypted_answer");

        byte[] tmp_key = concat(Crypto.SHA1(concat(new_nonce,server_nonce)), subArray(Crypto.SHA1(concat(server_nonce,new_nonce)),0,12));
        byte[] tmp_iv  = concat(subArray(Crypto.SHA1(concat(server_nonce,new_nonce)),12),Crypto.SHA1(concat(new_nonce,new_nonce)),subArray(new_nonce,0,4));
        byte[] answer_with_hash = new byte[encrypted_answer.length];

        Crypto.AES256IGEDecrypt(encrypted_answer,answer_with_hash,tmp_iv,tmp_key);

        byte[] answer_hash = subArray(answer_with_hash,0,20);
        byte[] answer      = subArray(answer_with_hash,20);

        TlObject server_DH_inner_data = new TlObject();
        server_DH_inner_data.deSerialize(new ByteArrayInputStream(answer));
        // console.log(server_DH_inner_data);

        int    g            = server_DH_inner_data.get("g");
        byte[] b            = Randoms.nextRandomBytes(256);
        byte[] dh_prime     = server_DH_inner_data.get("dh_prime");
        byte[] g_a          = server_DH_inner_data.get("g_a");
        int    server_time  = server_DH_inner_data.get("server_time");

        BigInteger bInt       = new BigInteger(b);
        BigInteger dhPrimeInt = new BigInteger(dh_prime).abs();
        BigInteger gaInt      = new BigInteger(g_a);


        byte[] g_b            = new BigInteger(""+g).modPow(bInt,dhPrimeInt).toByteArray();

        TlObject client_DH_inner_data = new TlObject("client_DH_inner_data")
                .put("nonce",nonce)
                .put("server_nonce",server_nonce)
                .put("retry_id",0L)
                .put("g_b",g_b);

        byte[] clientInnerData = client_DH_inner_data.serialize();
        byte[] data_with_sha   = concat(Crypto.SHA1(clientInnerData),clientInnerData);

        int pad =data_with_sha.length;
        while (pad % 16 !=0){
            pad++;
        }
        pad-=data_with_sha.length;

        byte[] data_with_sha_pa= concat(data_with_sha,Randoms.nextRandomBytes(pad));

        encrypted_data  = new byte[data_with_sha_pa.length];

        AES256IGEEncrypt(data_with_sha_pa,encrypted_data,tmp_iv,tmp_key);

        TlObject set_client_DH_params=  mtproto.invokeMtpCall(
                    new TlMethod("set_client_DH_params")
                            .put("nonce",nonce)
                            .put("server_nonce",server_nonce)
                            .put("encrypted_data",encrypted_data)
                );


        if (set_client_DH_params.predicate.equals("dh_gen_ok")) {
            byte[] auth_key = gaInt.modPow(bInt,dhPrimeInt).toByteArray();
            byte[] auth_key_sha = Crypto.SHA1(auth_key);
            byte[] auth_key_sha_aux = subArray(auth_key_sha,0,8);
            byte[] server_salt = xor(subArray(new_nonce,0,8),subArray(server_nonce,0,8));
            Streams.printHexTable(server_salt);
            Streams.printHexTable(auth_key);

            save(dcid,new AuthBag(auth_key,server_salt));
        }


    }


}
