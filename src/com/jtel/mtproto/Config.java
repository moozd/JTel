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
