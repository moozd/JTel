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
import com.jtel.mtproto.MtpEngine;
import com.jtel.mtproto.MtpFileStorage;
import com.jtel.mtproto.secure.Util;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.transport.HttpTransport;

public class Main {
    private static Logger console = Logger.getInstance();

    public static void main(String[] args) throws Exception {

        //ConfStorage.getInstance().setDebugging(false);

        MtpEngine engine = MtpEngine.getInstance();

        engine.setVerboseEnabled(true);
        engine.setVerboseHexTablesEnabled(false);

        engine.setStorage(new MtpFileStorage());
        engine.setTransport(new HttpTransport());

        //engine.authenticate(2);
        //engine.reset();
        if(!engine.isNetworkReady()){
            engine.authenticate();
        }

        engine.invokeApiCall(new TlMethod("auth.checkPhone").put("phone_number","989118836748"));

    }


}
