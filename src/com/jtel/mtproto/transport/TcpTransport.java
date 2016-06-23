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

package com.jtel.mtproto.transport;

import com.jtel.mtproto.secure.Util;
import com.jtel.mtproto.storage.ConfStorage;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.tl.Streams;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.zip.CRC32;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/22/16
 * Package : com.jtel.mtproto.transport
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class TcpTransport extends Transport {
    Socket socket;
    static boolean created = false;
    @Override
    protected void createConnection(int dcId) throws IOException {
        if(!created) {
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(ConfStorage.getInstance().getDc(dcId), 443));
            this.socket.setKeepAlive(true);
            this.socket.setTcpNoDelay(true);
          //  socket.getOutputStream().write(0xef);
            created=true;
        }
    }

    @Override
    protected byte[] onSend(byte[] message) throws IOException, InvalidTlParamException {
        socket.getOutputStream().write(message);
        CRC32 crc32 = new CRC32();
        crc32.reset();
        crc32.update(message);
        Streams.writeInt(socket.getOutputStream(),(int) (crc32.getValue() & 0xFFFFFFFF),"crc");
      //  socket.getOutputStream().flush();
       // socket.getOutputStream().close();
        errorCode = 200;
        return Util.toByteArray(socket.getInputStream());
    }
}
