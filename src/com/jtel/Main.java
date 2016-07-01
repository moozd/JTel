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


import com.jtel.api.TelegramApi;
import com.jtel.common.log.Logger;
import com.jtel.mtproto.storage.ConfStorage;
import com.jtel.mtproto.storage.MtpFileStorage;
import com.jtel.mtproto.tl.Tl;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;
import com.jtel.mtproto.tl.TlParam;
import com.jtel.mtproto.tl.schema.TlSchemaProvider;
import com.jtel.mtproto.transport.HttpTransport;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Logger console = Logger.getInstance();
    public static void main(String[] args) throws Exception {

        try {
            TelegramApi api = new TelegramApi(new MtpFileStorage(), new HttpTransport());
            if(! api.isUserOn()) {
                String phone_number = "+98" + read("enter your phone number : +98");
                TlObject sentCode = api.auth.sendCode(phone_number, 0, "en");
                String phone_code = read("enter your code:");
                TlObject auth = api.auth.signIn(phone_number, sentCode.get("phone_code_hash"), phone_code);
                console.log(auth);
            } else {

                TlObject object = api.messages.getDialogs(0,100,new TlObject("inputPeerEmpty"),100);
                List<TlObject> dialogs = object.get("dialogs");

                for (TlObject dialog: dialogs) {
                    TlObject peer = dialog.get("peer");
                    console.log(dialog);
                }
            }

        }catch (Exception e){
           //console.error(e.getMessage(),e.getCause().getMessage());
            e.printStackTrace();
        }
    }



    public static String read (String message){
        System.out.print("[waiting for user input] "+message + " ");
        return scanner.next();
    }
}
