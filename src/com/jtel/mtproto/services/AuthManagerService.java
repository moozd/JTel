package com.jtel.mtproto.services;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.pq.Pq;
import com.jtel.mtproto.pq.PqSolver;
import com.jtel.mtproto.secure.Crypto;
import com.jtel.mtproto.secure.PublicKeyHolder;
import com.jtel.mtproto.secure.Randoms;
import com.jtel.mtproto.services.data.AuthBag;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

import java.io.IOException;
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


    public void authenticate(int dcid) throws IOException{
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

        TlObject p_q_inner_data = new TlObject("p_q_inner_data");
        p_q_inner_data.put("pq"          ,pq.pq);
        p_q_inner_data.put("p"           ,pq.p);
        p_q_inner_data.put("q"           ,pq.q);
        p_q_inner_data.put("nonce"       ,nonce);
        p_q_inner_data.put("server_nonce", server_nonce);
        p_q_inner_data.put("new_nonce"   ,Randoms.nextRandomBytes(32));

        byte[] pqInner = p_q_inner_data.serialize();
        byte[] hash = Crypto.SHA1(pqInner);
        byte[] padd = Randoms.nextRandomBytes(255 - hash.length- pqInner.length);
        byte[] data_with_hash = Crypto.concat(hash,pqInner,padd);
        byte[] encrypted_data = Crypto.RSA(data_with_hash, PublicKeyHolder.modulus,PublicKeyHolder.exponent);

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



    }

}
