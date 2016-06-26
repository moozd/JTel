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

package com.jtel.api;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.MtpClient;
import com.jtel.mtproto.storage.ConfStorage;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/25/16
 * Package : com.jtel.api
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class ApiContext {

    protected static MtpClient mtp = MtpClient.getInstance();
    protected static int    api_id   = ConfStorage.getInstance().getItem("api-id");
    protected static String api_hash = ConfStorage.getInstance().getItem("api-hash");
    protected static TlObject generate(Pair... pairs) {
        try {
            StackTraceElement element = Thread.currentThread().getStackTrace()[2];
            String className = element.getClassName();
            String methodName = element.getMethodName();
            className = className.substring(className.lastIndexOf('.') + 1, className.length()).toLowerCase();
            String tlMet = className + "." + methodName;
            TlMethod method = new TlMethod(tlMet);
            for (Pair parameter : pairs) {
                method.put(parameter.getName(), parameter.getValue());
            }
            return mtp.invokeApiCall(method);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
