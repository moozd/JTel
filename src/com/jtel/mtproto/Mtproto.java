package com.jtel.mtproto;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.pq.Pq;
import com.jtel.mtproto.pq.PqSolver;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class Mtproto {
    private static Mtproto instance;

    public static Mtproto getInstance() {
        if (instance == null) {
            instance = new Mtproto();
        }
        return instance;
    }

    private int currentDcID = 1;

    private Mtproto(){

    }

    public void setCurrentDcID(int currentDcID) {
        this.currentDcID = currentDcID;
    }

    public int getCurrentDcID() {
        return currentDcID;
    }

    public TlObject invokeApiCall(TlMethod method)  {
        try {
            PlainHttpTransport transport = new PlainHttpTransport(Config.dcAddresses.get(1));
            transport.send(method);
            return transport.receive();
        }catch (Exception e){
            Logger.getInstance().error(e.getMessage());
        }
        return new TlObject();
    }
}
