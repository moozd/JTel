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

package com.jtel.mtproto.tl;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.auth.AuthCredentials;
import com.jtel.mtproto.secure.Crypto;
import com.jtel.mtproto.MtpTimeManager;

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

public class EncryptedMessage extends TlMessage{

    private Logger console = Logger.getInstance();
    private long session_id;

    public EncryptedMessage(long session_id,AuthCredentials credentials, TlMethod method) {
        super(credentials, method);
        this.session_id = session_id;
    }


    @Override
    public byte[] serialize() throws IOException, InvalidTlParamException {
        ByteArrayOutputStream mainStream = new ByteArrayOutputStream();

        //serializing method
        byte[] message = prepareBody(toByteArray());

        AuthCredentials credentials = getCredentials();
        //getting current dc credentials
        MtpTimeManager.getInstance().setTimeDelta(credentials.getServerTime());

        //message frame required fields
        byte[] auth_key_id = credentials.getAuthKeyId();
        byte[] server_salt   = credentials.getServerSalt();

        int seq_no       = MtpTimeManager.getInstance().generateSeqNo();
        long message_id  = MtpTimeManager.getInstance().generateMessageId();
        long session_id  = this.session_id;

        //creating encrypted_data_stream
        ByteArrayOutputStream encrypted_data_stream = new ByteArrayOutputStream();

        //adding server_salt to stream
        encrypted_data_stream.write(server_salt);
        writeLong(encrypted_data_stream, session_id,"session_id");
        writeLong(encrypted_data_stream, message_id,"message_id");
        writeInt(encrypted_data_stream,seq_no,"seq_no");
        writeInt(encrypted_data_stream,message.length,"message_length");
        encrypted_data_stream.write(message);



        byte[] msg_key_sha = Crypto.SHA1(encrypted_data_stream.toByteArray());
        byte[] msg_key = Crypto.subArray(msg_key_sha,msg_key_sha.length-16, 16);
        int pad = encrypted_data_stream.toByteArray().length;
        while (pad%16 !=0){
            pad++;
            encrypted_data_stream.write((byte) 0);

        }
        byte[] encrypted_data = new byte[pad];
        Map<String,byte[]> keys = Crypto.getAESKeyIV(msg_key,credentials.getAuthKey(), true);
        Crypto.AES256IGEEncrypt(encrypted_data_stream.toByteArray(),encrypted_data, keys.get("iv"),keys.get("key"));
        mainStream.write(auth_key_id);
        mainStream.write(msg_key);
        mainStream.write(encrypted_data);
        console.table(mainStream.toByteArray(),getMethod().method);
        return mainStream.toByteArray();
    }

    protected byte[] prepareBody(byte[] baseMessage){
        return  baseMessage;
    }

    @Override
    public void deSerialize(InputStream is) throws IOException {
        console.log("server_salt", readLong(is));
        saveResponse(new TlObject());
    }
}
