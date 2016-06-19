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

import com.jtel.mtproto.tl.TlObject;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/20/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class RpcHeaders {
    private boolean isApi = false;
    private byte[] authKeyId;
    private byte[] serverSalt;
    private long   messageId;
    private int    sequenceId;
    private byte[]   sessionId;
    private byte[]   authKey;
    long servertime =0;

    public RpcHeaders(long messageId) {
        this.authKeyId = new byte[0];
        this.messageId = messageId;
    }

    public RpcHeaders(byte[] authKeyId, byte[] serverSalt, long messageId, int sequenceId, byte[] sessionId, byte[] authKey) {
        this.authKeyId = authKeyId;
        this.serverSalt = serverSalt;
        this.messageId = messageId;
        this.sequenceId = sequenceId;
        this.sessionId = sessionId;
        this.authKey = authKey;
    }

    public void setAuthKey(byte[] authKey) {
        this.authKey = authKey;
    }

    public byte[] getAuthKey() {
        return authKey;
    }

    public void setSessionId(byte[] sessionId) {
        this.sessionId = sessionId;
    }


    public void setAuthKeyId(byte[] authKeyId) {
        this.authKeyId = authKeyId;
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

    public void setTimeDelta(long servertime) {
        this.servertime = servertime;
    }

    public long getTimeDelta() {
        return servertime;
    }

    public RpcHeaders() {
    }

    public byte[] getSessionId() {
        return sessionId;
    }

    public byte[] getAuthKeyId() {
        return authKeyId;
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

    public boolean isApi() {
        return isApi;
    }
}
