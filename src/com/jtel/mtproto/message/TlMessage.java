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

import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.tl.Tl;
import com.jtel.mtproto.tl.TlMethod;

import java.io.IOException;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/16/16
 * Package : com.jtel.mtproto.message
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public abstract class TlMessage implements Tl {

    private TlMethod method;

    private RpcHeaders header;
    private RpcResponse response;


    public TlMessage(TlMethod method, RpcHeaders header) {
        this.method = method;
        this.header = header;
    }

    public TlMessage() {
    }


    protected void setResponse(RpcResponse response) {
        this.response = response;
    }

    protected void setHeader(RpcHeaders header) {
        this.header = header;
    }



    public byte[] toByteArray() throws IOException, InvalidTlParamException {
        return method.serialize();
    }

    public RpcHeaders getRpcHeaders() {
        return header;
    }

    public void setRpcHeader(RpcHeaders header) {
        this.header = header;
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
