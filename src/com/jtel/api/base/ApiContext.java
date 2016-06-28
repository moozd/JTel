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

package com.jtel.api.base;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.MtpClient;
import com.jtel.mtproto.storage.ConfStorage;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/25/16
 * Package : com.jtel.api
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class ApiContext {


    protected MtpClient client = MtpClient.getInstance();

    protected int    apiId;
    protected String apiHash;

    public ApiContext() {
        this.client = client;
        this.apiId   = ConfStorage.getInstance().getItem("api-id");
        this.apiHash = ConfStorage.getInstance().getItem("api-hash");
    }



    public int getApiId() {
        return apiId;
    }

    public String getApiHash() {
        return apiHash;
    }

    protected TlObject generate(Pair... pairs) {
        try {
            StackTraceElement element = Thread.currentThread().getStackTrace()[2];
            String className = getClass().getSimpleName().toLowerCase();
            String methodName = element.getMethodName();
            String tlMet = className + "." + methodName;
            TlMethod method = new TlMethod(tlMet);
            for (Pair parameter : pairs) {
                method.put(parameter.getName(), parameter.getValue());
            }
            return client.invokeApiCall(method);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }


}
