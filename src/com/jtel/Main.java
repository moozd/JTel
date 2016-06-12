package com.jtel;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.auth.AuthCredentials;
import com.jtel.mtproto.services.MtpService;

public class Main {
    private static Logger console = Logger.getInstance();

    public static void main(String[] args) throws Exception {
        MtpService mtpService = MtpService.getInstance();
        mtpService.reset();
        mtpService.authenticateForAll();



    }

    public  static void print(int dc){
        MtpService mtpService = MtpService.getInstance();
        AuthCredentials credentials = mtpService.getCredentialsForDc(dc);
        console.log("dc "+dc+" credentials");
        console.log("server_time",credentials.getServerTime());
        console.table(credentials.getAuthKeyId(),"auth_key_id");
        console.table(credentials.getServerSalt(),"server_salt");
        console.table(credentials.getAuthKey(),"auth_key");
        System.out.println();
    }

}
