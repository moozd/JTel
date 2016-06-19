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

package com.jtel.mtproto;

import com.jtel.mtproto.tl.TlObject;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/19/16
 * Package : com.jtel.mtproto.message
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class RpcResponse {

    private boolean isApi = false;
    private byte[] authKeyId;
    private byte[] serverSalt;
    private long   messageId;
    private int    sequenceId;
    private byte[]   sessionId;
    private byte[]   messageBytes;
    private TlObject response;



    public RpcResponse(long messageId, byte[] messageBytes, TlObject response) {
        this.authKeyId = new byte[8];
        this.messageId = messageId;
        this.messageBytes = messageBytes;
        this.response = response;
        isApi = false;
    }

    public RpcResponse(byte[] authKeyId, byte[] serverSalt,byte[] sessionId ,long messageId, int sequenceId, byte[] messageBytes, TlObject response) {
        this.authKeyId = authKeyId;
        this.serverSalt = serverSalt;
        this.messageId = messageId;
        this.sequenceId = sequenceId;
        this.messageBytes = messageBytes;
        this.response = response;
        this.sessionId = sessionId;
        isApi = false;
    }

    public void setSessionId(byte[] sessionId) {
        this.sessionId = sessionId;
    }

    public void setResponse(TlObject response) {
        this.response = response;
    }

    public void setAuthKeyId(byte[] authKeyId) {
        this.authKeyId = authKeyId;
    }

    public void setMessageBytes(byte[] messageBytes) {
        this.messageBytes = messageBytes;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public void setServerSalt(byte[] serverSalt) {
        this.serverSalt = serverSalt;
    }


    public RpcResponse() {
    }

    public byte[] getSessionId() {
        return sessionId;
    }

    public byte[] getAuthKeyId() {
        return authKeyId;
    }

    public byte[] getMessageBytes() {
        return messageBytes;
    }

    public byte[] getServerSalt() {
        return serverSalt;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public long getMessageId() {
        return messageId;
    }

    public TlObject getObject() {
        return response;
    }

    public boolean isApi() {
        return isApi;
    }
}
