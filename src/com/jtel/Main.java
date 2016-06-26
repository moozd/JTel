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

import com.jtel.api.*;
import com.jtel.common.log.Logger;
import com.jtel.mtproto.MtpClient;
import com.jtel.mtproto.storage.ConfStorage;
import com.jtel.mtproto.storage.MtpFileStorage;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;
import com.jtel.mtproto.transport.HttpTransport;

import java.util.Scanner;

public class Main {
    private static Logger console = Logger.getInstance();
    private static MtpClient client = MtpClient.getInstance();
    public static void main(String[] args) throws Exception {


        client.createSession(new MtpFileStorage(),new HttpTransport());
//        TlObject users = client.invokeApiCall(new TlMethod("contacts.getContacts")
//        .put("hash",""));
        //signIn();

   /*    console.log( Auth.checkPhone("989118836748") );*/
        console.log( Auth.sendCode("989118836758",0,"en") );


    }

    public static void signIn() throws Exception{
        String phone_number = "989118836748";
        int    sms_type     = 0;
        int    api_id       = ConfStorage.getInstance().getItem("api-id");
        String api_hash     = ConfStorage.getInstance().getItem("api-hash");
        String lang_code    = "en";


        TlObject sentCode = client.invokeApiCall(
                new TlMethod("auth.sendCode")
                        .put("phone_number" ,phone_number)
                        .put("sms_type"     ,sms_type    )
                        .put("api_id"       ,api_id      )
                        .put("api_hash"     ,api_hash    )
                        .put("lang_code"    ,lang_code   )
        );

        if(sentCode.getPredicate().equals("rpc_error")) {
            return;
        }
        console.log("sms sent",sentCode);
        console.log("enter code");

        Scanner scanner = new Scanner(System.in);

        String phone_code_hash = sentCode.get("phone_code_hash");
        String phone_code      = scanner.next();

        TlObject auth = client.invokeApiCall(
                new TlMethod("auth.signIn")
                        .put("phone_number"     ,phone_number)
                        .put("phone_code_hash"  ,phone_code_hash)
                        .put("phone_code",phone_code)
        );

        console.log("signIn done",auth);
        client.saveSignIn(auth);

    }
/*max_delay: 500,
      wait_after: 150,
      max_wait: maxWait*/

}
