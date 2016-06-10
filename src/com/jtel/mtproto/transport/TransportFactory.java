package com.jtel.mtproto.transport;

import com.jtel.mtproto.Config;
import com.jtel.mtproto.tl.Streams;

import java.io.IOException;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.mtproto.transport
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class TransportFactory {

    public static Transport Create(String address) throws IOException {
        switch (Config.Transport){
            case "http":
                return new PlainHttpTransport(address);

        }
        return null;
    }
}
