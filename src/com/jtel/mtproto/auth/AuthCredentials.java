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


import java.io.Serializable;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/11/16
 * Package : com.jtel.mtproto.auth
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class AuthCredentials implements Serializable {

    private byte[] authKey;
    private byte[] serverSalt;
    private long   serverTime;
    private byte[] authKeyId;



    public AuthCredentials(byte[] authKey, byte[] serverSalt, long serverTime, byte[] authKeyId) {
        this.authKey = authKey;
        this.serverSalt = serverSalt;
        this.serverTime = serverTime;
        this.authKeyId = authKeyId;
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

}