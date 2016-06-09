package com.jtel.mtproto.services;

import com.jtel.mtproto.Config;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;
import com.jtel.mtproto.transport.Transport;
import com.jtel.mtproto.transport.TransportFactory;

import java.io.IOException;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class MtprotoService {
    private static MtprotoService instance;

    public static MtprotoService getInstance() {
        if (instance == null) {
            instance = new MtprotoService();
        }
        return instance;
    }

    private int currentDcID = 1;

    private MtprotoService(){

    }

    public void setCurrentDcID(int currentDcID) {
        this.currentDcID = currentDcID;
    }

    public int getCurrentDcID() {
        return currentDcID;
    }

    public TlObject invokeMtpCall(TlMethod method) throws IOException {


//        try {
            Transport transport = TransportFactory.Create("http",Config.dcAddresses.get(2));
            return transport.send(method);
//        }catch (Exception e) {
//            Logger.getInstance().error(e.getMessage());
//        }
//        return new TlObject();
    }
}
