package com.jtel.mtproto.services;

import com.jtel.mtproto.services.data.AuthBag;

import java.util.HashMap;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto.services
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public final class AuthManagerService {

    private static  AuthManagerService instance;

    private AuthManagerService() {
        initialize();
    }

    public static AuthManagerService getInstance() {

        if (instance == null) {
            instance = new AuthManagerService();
        }

        return instance;
    }


    private HashMap<Integer,AuthBag> authBags;

    private void initialize() {
        authBags = new HashMap<>();
    }

    public AuthBag get(int dc) {

        if (! authBags.containsKey(dc)) {
            return null;
        }

        return authBags.get(dc);
    }

    public void save(int dc, AuthBag auth) {
        authBags.put(dc, auth);
    }

}
