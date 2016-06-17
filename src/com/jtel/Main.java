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

package com.jtel;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.ConfStorage;
import com.jtel.mtproto.auth.AuthCredentials;
import com.jtel.mtproto.MtpEngine;
import com.jtel.mtproto.MtpFileStorage;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.transport.HttpTransport;

public class Main {
    private static Logger console = Logger.getInstance();

    public static void main(String[] args) throws Exception {

        MtpEngine engine = MtpEngine.getInstance();
        engine.setStorage(new MtpFileStorage());
        engine.setTransport(new HttpTransport());

        /*engine.initConnection();*/
        engine.invokeApiCall(new TlMethod("help.getConfig"));


    }

    public  static void print(int dc){
        MtpEngine mtpService = MtpEngine.getInstance();
        AuthCredentials credentials = mtpService.getCredentialsForDc(dc);
        console.log("dc "+dc+" credentials");
        console.log("server_time",credentials.getServerTime());
        console.table(credentials.getAuthKeyId() ,"auth_key_id");
        console.table(credentials.getServerSalt(),"server_salt");
        console.table(credentials.getAuthKey()   ,"auth_key");
        System.out.println();
    }

}
