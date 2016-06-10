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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public final class Config {


    public final static HashMap<Integer, String> dcAddresses;
    public final static String                   apiHash;
    public final static int                      apiId;
    public final static int                      schemaLayer;
    public final static boolean                  Debug;
    public final static String                   Transport;
    public final static String                   mtpSchema;
    public final static String                   apiSchema;

    static {

        dcAddresses = new HashMap<>();

        dcAddresses.put(1, "149.154.175.50");
        dcAddresses.put(2, "149.154.167.51");
        dcAddresses.put(3, "149.154.175.100");
        dcAddresses.put(4, "149.154.167.91");
        dcAddresses.put(5, "149.154.171.5");

        apiHash     = "6e7e20a046e44cb801dc1e8325233396";
        apiId       = 37516;
        schemaLayer = 45;

        Debug =false;
        Transport ="http";

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        mtpSchema=s+"/mtp_schema.json";
        apiSchema=s+"/api_schema.json";

    }




}
