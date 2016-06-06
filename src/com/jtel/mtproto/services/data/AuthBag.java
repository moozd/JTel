package com.jtel.mtproto.services.data;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto.services.data
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class AuthBag {

    public byte[] auth_key;

    public byte[] server_salt;

    public AuthBag( byte[] auth_key, byte[] server_salt) {
        this.auth_key = auth_key;
        this.server_salt = server_salt;
    }

}
