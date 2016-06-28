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
import com.jtel.mtproto.storage.MtpFileStorage;
import com.jtel.mtproto.transport.HttpTransport;

public class Main {

    static Logger console = Logger.getInstance();
    public static void main(String[] args) throws Exception {

        TelegramApi api = new TelegramApi(new MtpFileStorage(),new HttpTransport());
        console.log( api.help.getConfig() );



    }
}
