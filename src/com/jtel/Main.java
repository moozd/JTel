package com.jtel;

import com.jtel.mtproto.services.AuthManagerService;

public class Main {

    public static void main(String[] args) throws Exception {
        AuthManagerService service = AuthManagerService.getInstance();
        service.authenticate(2);

    }

}
