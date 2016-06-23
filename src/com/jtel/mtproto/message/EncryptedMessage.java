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

package com.jtel.mtproto.message;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.secure.Util;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.tl.Tl;
import com.jtel.mtproto.tl.TlObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.jtel.mtproto.tl.Streams.*;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/16/16
 * Package : com.jtel.mtproto.message
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class EncryptedMessage extends TlMessage {

    private Logger console = Logger.getInstance();

    public EncryptedMessage(Tl method, MessageHeaders header) {
        super(method, header);
    }

    @Override
    public byte[] serialize() throws IOException, InvalidTlParamException {
        MessageHeaders rpcHeaders = getHeaders();
        ByteArrayOutputStream mainStream = new ByteArrayOutputStream();

        //serializing method
        byte[] message = prepareBody(toByteArray());

        //message frame required fields
        byte[] auth_key_id   = rpcHeaders.getAuthKeyId();
        byte[] server_salt   = rpcHeaders.getServerSalt();

        int seq_no       = rpcHeaders.getSequenceId();
        long message_id  = rpcHeaders.getMessageId();


        //creating plain_text
        ByteArrayOutputStream plain_text = new ByteArrayOutputStream();

        //adding server_salt to stream
        writeIntBytes(plain_text,server_salt,64,"server_salt");
        writeIntBytes(plain_text, rpcHeaders.getSessionId(),64,"session_id");
        writeLong(plain_text, message_id,"message_id");
        writeInt(plain_text,seq_no,"seq_no");
        writeInt(plain_text,message.length,"message_length");
        plain_text.write(message);

        byte[] msg_key_sha = Util.SHA1(plain_text.toByteArray());
        byte[] msg_key = Util.subArray(msg_key_sha,msg_key_sha.length-16, 16);

        int pad = plain_text.toByteArray().length;
        while (pad%16 !=0){
            pad++;
            plain_text.write((byte) 0);
        }
      //  console.table(plain_text.toByteArray(),"send");
        byte[] encrypted_data = new byte[pad];
        Map<String,byte[]> keys = Util.getAESKeyIV(msg_key,rpcHeaders.getAuthKey(), true);
        Util.AES256IGEEncrypt(plain_text.toByteArray(),encrypted_data, keys.get("iv"),keys.get("key"));
        mainStream.write(auth_key_id);
        mainStream.write(msg_key);
        mainStream.write(encrypted_data);

        return mainStream.toByteArray();
    }

    protected byte[] prepareBody(byte[] baseMessage){
        return  baseMessage;
    }

    @Override
    public void deSerialize(InputStream is) throws IOException {
        byte[] auth_id = new byte[8];
        is.read(auth_id);
        if(!Util.bytesCmp(auth_id, getHeaders().getAuthKeyId())){
            console.warn("auth_key_id" ,"dose not match.");
        }
        byte[] msg_key = readIntBytes(is,128);
        byte[] encrypted = Util.toByteArray(is);
        Map<String,byte[]> aesKey =  Util.getAESKeyIV(msg_key, getHeaders().getAuthKey(),false);
        byte[] decrypted = new byte[encrypted.length];
        Util.AES256IGEDecrypt(encrypted,decrypted,aesKey.get("iv"),aesKey.get("key"));

        ByteArrayInputStream msgInputStream = new ByteArrayInputStream(decrypted);

        byte[] responed_server_salt = readIntBytes(msgInputStream,64);
        byte[] responed_session_id  = readIntBytes(msgInputStream,64);
        long respond_message_id     = readLong(msgInputStream);
        int  responed_seq_id        = readInt(msgInputStream);

        int length =readInt(msgInputStream);
        byte[] innerMessage = new byte[length];

        msgInputStream.read(innerMessage);
        TlObject object = new TlObject();
       // console.table(innerMessage,getContext().type);
        object.deSerialize(new ByteArrayInputStream(innerMessage));

        MessageResponse rpcResponse = new MessageResponse();
        rpcResponse.setAuthKeyId(auth_id);
        rpcResponse.setServerSalt(responed_server_salt);
        rpcResponse.setSessionId(responed_session_id);
        rpcResponse.setMessageId(respond_message_id);
        rpcResponse.setSequenceId(responed_seq_id);
        rpcResponse.setMessageBytes(innerMessage);
        rpcResponse.setObject(object);

        setResponse(rpcResponse);
    }

    public EncryptedMessage() {;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        TlMessage message = new EncryptedMessage();
        message.setContext(this.getContext());
        message.setHeader(this.getHeaders());
        message.setResponse(this.getResponse());
        return message;
    }
}
