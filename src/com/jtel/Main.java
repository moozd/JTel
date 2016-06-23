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
import com.jtel.mtproto.message.AckMessage;
import com.jtel.mtproto.message.TlMessage;
import com.jtel.mtproto.storage.MtpFileStorage;
import com.jtel.mtproto.tl.Streams;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;
import com.jtel.mtproto.tl.TlParameter;
import com.jtel.mtproto.transport.HttpTransport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Logger console = Logger.getInstance();

    public static void main(String[] args) throws Exception {
        MtpEngine engine = MtpEngine.getInstance();
        engine.createSession(new MtpFileStorage(),new HttpTransport());
        engine.invokeApiCall(new TlMethod("auth.checkPhone").put("phone_number","989118836748"));
        engine.invokeApiCall(new TlMethod("help.getConfig"));
        engine.invokeApiCall(new TlMethod("help.getConfig"));
    }
/*max_delay: 500,
      wait_after: 150,
      max_wait: maxWait*/

}
