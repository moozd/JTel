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

import com.jtel.mtproto.secure.Utils;
import com.jtel.mtproto.storage.ConfStorage;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.tl.Streams;

import java.io.IOException;
import java.net.Socket;

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
    byte[] outPut;
    static boolean created = false;
    @Override
    protected void createConnection(int dcId) throws IOException {
       if(!created) {
            this.socket = new Socket(ConfStorage.getInstance().getDc(dcId),443);
         //   this.socket.connect();
            this.socket.setKeepAlive(true);
            this.socket.setTcpNoDelay(true);
           socket.getOutputStream().write(0xef);
            created=true;
      }
    }

    @Override
    protected byte[] onSend(byte[] message) throws IOException, InvalidTlParamException {

       Thread sender = new Thread(()->{
           try {
               console.log("writing ",message.length + "bytes" );
               socket.getOutputStream().write(message.length +15);
               Streams.writeInt(socket.getOutputStream(),message.length+12,"");
               Streams.writeInt(socket.getOutputStream(),0,"");
               socket.getOutputStream().write(message);
               socket.getOutputStream().flush();
           }catch (Exception e){
               console.error(e.getMessage());
           }
       });

       Thread receiver = new Thread(()->{
           try {
               console.log("reading ",message.length + "bytes" );
               outPut = Utils.toByteArray(socket.getInputStream());
           }catch (Exception e){
               console.error(e.getMessage());
           }

       });
//
//        CRC32 crc32 = new CRC32();
//        crc32.reset();
//        crc32.update(message);
      //  Streams.writeInt(socket.getOutputStream(),(int) (crc32.getValue()),"crc");
      //   socket.getOutputStream().flush();
       // socket.getOutputStream().close();
        sender.start();
        try {
            sender.join();
        }catch (Exception e){

        }
        receiver.start();
        try {
            receiver.join();
        }catch (Exception e){
            console.error(e.getMessage());
        }
        errorCode = 200;
     //   new Thread(()->{console.log(1);}).start();
      //  Thread.yield();
      //  socket.getInputStream();
        return outPut ;
    }


}
