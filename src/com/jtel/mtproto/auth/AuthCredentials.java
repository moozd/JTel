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

package com.jtel.mtproto.auth;


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/11/16
 * Package : com.jtel.mtproto.auth
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class AuthCredentials implements Serializable {

    private int    dcId;
    private byte[] authKey;
    private byte[] serverSalt;
    private long   serverTime;
    private byte[] authKeyId;



    public AuthCredentials(int dc, byte[] authKey, byte[] serverSalt, long serverTime, byte[] authKeyId) {
        this.authKey = authKey;
        this.serverSalt = serverSalt;
        this.serverTime = serverTime;
        this.authKeyId = authKeyId;
        this.dcId =dc;
    }

    public AuthCredentials(byte[] auth_key, byte[] server_salt, long serverTime) {
        this.authKey = auth_key;
        this.serverSalt = server_salt;
        this.serverTime =serverTime;
    }



    public AuthCredentials() {
        this.authKey = new byte[0];
        this.serverSalt = new byte[0];
        this.serverTime = -1;
        this.authKeyId = new byte[0];
    }

    public int getDcId() {
        return dcId;
    }

    public byte[] getAuthKeyId() {
        return authKeyId;
    }


    public long getServerTime() {
        return serverTime;
    }

    public byte[] getAuthKey() {
        return authKey;
    }

    public byte[] getServerSalt() {
        return serverSalt;
    }

    public void setServerSalt(byte[] serverSalt) {
        this.serverSalt = serverSalt;
    }
    public void setServerSalt(long serverSalt) {
        this.serverSalt = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(serverSalt).array();
    }

    public void setAuthKey(byte[] authKey) {
        this.authKey = authKey;
    }

    public void setDcId(int dcId) {
        this.dcId = dcId;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public void setAuthKeyId(byte[] authKeyId) {
        this.authKeyId = authKeyId;
    }

    @Override
    public String toString() {
        return String.format("( dc: %s , server-time: %s , auth-key: %s , server-salt: %s )"
        ,getDcId(),getServerTime(),bytesToString(getAuthKey()),bytesToString(getServerSalt()));
    }

    private String bytesToString(byte[] b){
        return String.format("[len:%s / %s... ]",b.length, HexBin.encode(b).substring(0,(b.length >10)? 5:b.length));
    }
}