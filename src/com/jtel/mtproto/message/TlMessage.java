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

import com.jtel.mtproto.RpcResponse;
import com.jtel.mtproto.auth.AuthCredentials;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.tl.Tl;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/16/16
 * Package : com.jtel.mtproto.message
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public abstract class TlMessage implements Tl {

    private AuthCredentials credentials;
    private TlMethod method;

    private RpcResponse response;

    public TlMessage(AuthCredentials credentials, TlMethod method){
        this.credentials =credentials;
        this.method = method;
    }


    public AuthCredentials getCredentials() {
        return credentials;
    }

    public byte[] toByteArray() throws IOException, InvalidTlParamException {
        return method.serialize();
    }


    public RpcResponse getRpcResponse() {
        return response;
    }

    protected void saveResponse(RpcResponse response) {
        this.response = response;
    }

    public TlMethod getMethod() {
        return method;
    }

    protected void setMethod(TlMethod method) {
        this.method = method;
    }

    public TlMessage(AuthCredentials credentials) {
        this.credentials = credentials;
    }


}
