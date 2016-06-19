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

package com.jtel.mtproto.auth;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.ConfStorage;
import com.jtel.mtproto.auth.pq.Pq;
import com.jtel.mtproto.auth.pq.PqSolver;
import com.jtel.mtproto.secure.Crypto;
import com.jtel.mtproto.secure.PublicKeyStorage;
import com.jtel.mtproto.secure.Randoms;
import com.jtel.mtproto.MtpEngine;
import com.jtel.mtproto.MtpTimeManager;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

import static com.jtel.mtproto.secure.Crypto.*;

import java.io.*;
import java.math.BigInteger;
import java.util.List;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto.services
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public final class AuthManager {


    /**
     * private constructor class is available throw getInstance()
     */
    public AuthManager() {

    }

    /**
     * logger object for debugging
     */
    private Logger console = Logger.getInstance();

    private ConfStorage conf = ConfStorage.getInstance();

    /**
     * stores authenticated data centers auth_key and server_salt
     */



    /**
     * authenticate to given data center
     * @param dcid data center id
     * @throws AuthFailedException if network fails or if invalid parameter is supplied
     */

    public AuthCredentials authenticate(int dcid) throws AuthFailedException{
        try {
            return authAttempt(dcid);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new AuthFailedException(e.getMessage());
        }
    }


    /**
     * authenticate to given data center
     * @param dcid data center id
     * @throws IOException if network fails
     * @throws InvalidTlParamException if invalid parameter is supplied
     */
    protected AuthCredentials authAttempt(int dcid) throws IOException,InvalidTlParamException{

        //configuring MTProto service and starting it
        MtpEngine mtproto = MtpEngine.getInstance();
        mtproto.setDc(dcid);

        //step 1 ,calling req_pq request
        TlObject resPq = mtproto.invokeMtpCall
                (
                        new TlMethod("req_pq")
                                .put("nonce", Randoms.nextRandomBytes(16)) // 128 bit random number
                );

        //server responds with resPq object
        //saving resPq Object parameters to variables
        byte[]      nonce                   =  resPq.get("nonce");
        byte[]      server_nonce            =  resPq.get("server_nonce");
        Pq          pq                      =  new Pq(resPq.get("pq"));
        List<Long>  public_key_fingerprints =  resPq.get("server_public_key_fingerprints");

        //solving pq number using prim factoring , pq = p*q where p < q
        pq =  PqSolver.Solve(pq);

        //creating 256 bit random number
        byte[] new_nonce = Randoms.nextRandomBytes(32);

        //creating p_q_inner_data.remember we wont sendSync this object yet so
        //we should create it by using TlObject constructor
        TlObject p_q_inner_data = new TlObject("p_q_inner_data")
                .put("pq"          ,pq.pq)          //pq bytes from first step
                .put("p"           ,pq.p)           //calculated number from first step
                .put("q"           ,pq.q)           //calculated number from first step
                .put("nonce"       ,nonce)          //nonce 128 bit random bytes from first step
                .put("server_nonce", server_nonce)  //server_nonce 128 bit random bytes from first step
                .put("new_nonce"   ,new_nonce);     //new_nonce 256 bit random bytes

        //serializing p_q_inner_data to tl language byte array
        // we should calculate sha1 hash of given
        //byte array then we will concat hash and pqInnerData and
        //at the end we should add padding to to this new array
        //after that result array will be exactly 256 bytes.
        byte[] pqInner = p_q_inner_data.serialize();
        byte[] hash = Crypto.SHA1(pqInner);
        byte[] padd = Randoms.nextRandomBytes(255 - hash.length- pqInner.length);
        byte[] data_with_hash = Crypto.concat(hash,pqInner,padd);

        //after registering api_id on Telegram server they will give you a public key
        //they have limited count of public keys that are available at any Telegram
        //official open source projects if you take look at first step (req_pq)
        //you will see that server sends list of public key fingerprints, to find your
        //public key you must iterate list of keys that telegram gave you to match
        //fingerprints (the one server gave you and one your public key has) then you
        //can encrypt p_q_inner data by using RSA  and public key
        byte[] encrypted_data = Crypto.RSA(data_with_hash, PublicKeyStorage.modulus, PublicKeyStorage.exponent);

        //step 2 starts from here , starting dh key exchange
        //by invoking req_DH_params method
        TlObject Server_Dh_Params = mtproto.invokeMtpCall
                (
                        new TlMethod("req_DH_params")       //method
                        .put("nonce",nonce)                 //nonce 128 bit random number from step 1
                        .put("server_nonce",server_nonce)   //server_nonce  128 bit random number from step 1
                        .put("p",pq.p)                      //calculated p number from pq , step 1
                        .put("q",pq.q)                      //calculated q number from pq , step 1
                        .put("public_key_fingerprint",public_key_fingerprints.get(0)) // selected fingerprint from list of fingerprints of step 1
                        .put("encrypted_data",encrypted_data) // encrypted sha1(pqInner)+pqInner+padding using RSA
                );

        //if req_dh_params fail server will respond with object "server_DH_params_fail"
        //if not it will respond with "server_DH_params_ok" . in case it fails i will restart
        // the whole operation

        if(Server_Dh_Params.predicate.equals("server_DH_params_fail")){
            console.error("dh key exchange failed","retrying...");
            return authAttempt(dcid);

        }

        //server sends an encrypted message this time we should decrypt it using
        //aes.ige
        byte[] encrypted_answer = Server_Dh_Params.get("encrypted_answer");

        //to decrypt aes.ige we should generate a key and iv.
        byte[] tmp_key = concat(Crypto.SHA1(concat(new_nonce,server_nonce)), subArray(Crypto.SHA1(concat(server_nonce,new_nonce)),0,12));
        byte[] tmp_iv  = concat(subArray(Crypto.SHA1(concat(server_nonce,new_nonce)),12),Crypto.SHA1(concat(new_nonce,new_nonce)),subArray(new_nonce,0,4));
        byte[] answer_with_hash = new byte[encrypted_answer.length];

        //decrypting...
        Crypto.AES256IGEDecrypt(encrypted_answer,answer_with_hash,tmp_iv,tmp_key);

        //first 20 bytes of encrypted answer is answer hash and the rest of it
        //is answer itself
        byte[] answer_hash = subArray(answer_with_hash,0,20);
        byte[] answer      = subArray(answer_with_hash,20);

        //deserialize the answer
        TlObject server_DH_inner_data = new TlObject();
        server_DH_inner_data.deSerialize(new ByteArrayInputStream(answer));

        //these are parameters we can get from this object
        //also we need to create new random number     256
        //bit to continue the auth_key calculation
        int    g            = server_DH_inner_data.get("g");
        byte[] b            = Randoms.nextRandomBytes(256);
        byte[] dh_prime     = server_DH_inner_data.get("dh_prime");
        byte[] g_a          = server_DH_inner_data.get("g_a");
        // use server time to calculate time offset server_time - local time
        //time offset will be used to generate message id
        long    server_time  = System.currentTimeMillis() - (int)server_DH_inner_data.get("server_time");
        MtpTimeManager.getInstance().setTimeDelta(System.currentTimeMillis() - server_time);

        //these numbers are really big so we must use BigInteger to do calculations on theme
        BigInteger bInt       = new BigInteger(1,b);
        BigInteger dhPrimeInt = new BigInteger(1,dh_prime);
        BigInteger gaInt      = new BigInteger(1,g_a);

        //g_b = g^b % dh_prime
        byte[] g_b            = fromBigInt(new BigInteger(""+g).modPow(bInt,dhPrimeInt));

        //create client_DH_inner_data
        TlObject client_DH_inner_data = new TlObject("client_DH_inner_data")
                .put("nonce",nonce) //nonce 128 bit random number from step 1
                .put("server_nonce",server_nonce) //server_nonce 128 bit random number from step 1
                .put("retry_id",0L) //retry id in case server dose not respond with dh_gen_ok
                .put("g_b",g_b); //newly generated number g_b

        //serialize this object and create sha1(data)+data+padding
        byte[] clientInnerData = client_DH_inner_data.serialize();
        byte[] data_with_sha   = concat(Crypto.SHA1(clientInnerData),clientInnerData);

        //creating padding bytes length,padding bytes are random
        int pad =data_with_sha.length;
        while (pad % 16 !=0){
            pad++;
        }
        pad-=data_with_sha.length;

        byte[] data_with_sha_pa= concat(data_with_sha,Randoms.nextRandomBytes(pad));

        encrypted_data  = new byte[data_with_sha_pa.length];

        //encrypt generated data_with_hash using aes.ige by tmp_key and tmp_iv from step 2
        AES256IGEEncrypt(data_with_sha_pa,encrypted_data,tmp_iv,tmp_key);

        //step 3 sendSync set_client_DH_params
        TlObject set_client_DH_params=  mtproto.invokeMtpCall(
                    new TlMethod("set_client_DH_params")
                            .put("nonce",nonce) //nonce 128 bit random number from step 1
                            .put("server_nonce",server_nonce) //server_nonce 128 bit random number from step 1
                            .put("encrypted_data",encrypted_data) //client_DH_inner_data that we encrypt using aes.ige
                );

        //creating auth_key -> (g^b) % dh_prime
        byte[] auth_key = fromBigInt(gaInt.modPow(bInt,dhPrimeInt));
        byte[] auth_key_sha = Crypto.SHA1(auth_key);
        byte[] auth_key_sha_aux = subArray(auth_key_sha,0,8);
        byte[] auth_key_id =subArray(auth_key_sha,auth_key_sha.length-8,8);

        byte[] new_nonce_hash_1 = subArray(SHA1(concat(new_nonce,new byte[]{1},auth_key_sha_aux)),4,16);
        byte[] new_nonce_hash_2 = subArray(SHA1(concat(new_nonce,new byte[]{2},auth_key_sha_aux)),4,16);
        byte[] new_nonce_hash_3 = subArray(SHA1(concat(new_nonce,new byte[]{3},auth_key_sha_aux)),4,16);

        //if server returns dh_gen_ok object then dh key exchange is done successfully
        switch(set_client_DH_params.predicate) {
            case "dh_gen_ok":

                if(!bytesCmp(new_nonce_hash_1,set_client_DH_params.get("new_nonce_hash1"))){
                    console.warn("new_nonce_hash1" , "failed");
                    console.table(new_nonce_hash_1,"client");
                    console.table(set_client_DH_params.get("new_nonce_hash1"),"server");
                    return new AuthCredentials();
                }

                //server_salt is bytesXor of first eight byte of new_nonce and first eight byte of server_nonce
                byte[] server_salt = bytesXor(subArray(new_nonce,0,8),subArray(server_nonce,0,8));

                console.log("Done.");
                if(conf.debug()) {
                    //printing hex table of server salt and auth_key
                    console.table(server_salt, "server_salt");
                    console.table(auth_key, "auth_key");
                }
                //saving auth_key and server_salt for this data center id
               return  new AuthCredentials(dcid,auth_key,server_salt,server_time,auth_key_id);
            case "dh_gen_retry":
                if(!bytesCmp(new_nonce_hash_2,set_client_DH_params.get("new_nonce_hash2"))){
                    console.warn("new_nonce_hash2" , "failed");
                }
            break;
            case "dh_gen_failed" :
                if(!bytesCmp(new_nonce_hash_3,set_client_DH_params.get("new_nonce_hash3"))){
                    console.warn("new_nonce_hash3" , "failed");
                }
            break;
        }

        return new AuthCredentials();
    }


}
