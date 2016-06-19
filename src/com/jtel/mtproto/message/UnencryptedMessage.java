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
import com.jtel.mtproto.ConfStorage;
import com.jtel.mtproto.MtpTimeManager;
import com.jtel.mtproto.RpcResponse;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.jtel.mtproto.tl.Streams.*;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/16/16
 * Package : com.jtel.mtproto.message
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class UnencryptedMessage extends TlMessage {


    public UnencryptedMessage(TlMethod method){
        super(null,method);
    }
    private Logger console = Logger.getInstance();
    private ConfStorage conf = ConfStorage.getInstance();


    @Override
    public byte[] serialize() throws IOException, InvalidTlParamException {
        byte[] message = toByteArray();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writeLong(os, 0L,"AuthStorage Key");
        writeLong(os,( MtpTimeManager.getInstance().generateMessageId()),"Message ID");
        writeInt(os,message.length,"Message length");
        os.write(message);
        if (conf.debug()){
            console.log(getMethod());
            console.table(os.toByteArray(), getMethod().method);
        }
        return os.toByteArray();
    }

    @Override
    public void deSerialize(InputStream is) throws IOException {
        long auth_id    = readLong(is);
        long message_id = readLong(is);
        int message_len = readInt(is);

        if(conf.debug()){
            console.log("auth_id",auth_id);
            console.log("msg_id",message_id);
            console.log("msg_len",message_len);
        }

        byte[] responseBytes = new byte[message_len];
        is.read(responseBytes);
        ByteArrayInputStream bis = new ByteArrayInputStream(responseBytes);
        TlObject responseObject = new TlObject();
        responseObject.deSerialize(bis);


        if(conf.debug()){
            console.log(responseObject);
            console.table(responseBytes,responseObject.predicate);
        }

        saveResponse(new RpcResponse(message_id,responseBytes,responseObject));
    }


}
