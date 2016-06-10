package com.jtel.mtproto.services;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.Config;
import com.jtel.mtproto.tl.InvalidTlParamException;
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
    private Logger console = Logger.getInstance();
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

    public TlObject invokeMtpCall(TlMethod method) throws IOException,InvalidTlParamException {


//        try {
            long time = System.currentTimeMillis();
            console.log("invokeMtpCall:" , method.method);
            Transport transport = TransportFactory.Create(Config.dcAddresses.get(2));
            TlObject ret = transport.send(method);
            console.log("rpcResult    :",ret.predicate + " ("+ (System.currentTimeMillis() - time)/1000f+"s" +")."  );
            return ret;
//        }catch (Exception e) {
//            Logger.getInstance().error(e.getMessage());
//        }
//        return new TlObject();
    }
}
