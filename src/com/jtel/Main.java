package com.jtel;

import com.jtel.mtproto.secure.Randoms;
import com.jtel.mtproto.services.MtprotoService;
import com.jtel.mtproto.pq.Pq;
import com.jtel.mtproto.pq.PqSolver;
import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

import java.io.IOException;

import static com.jtel.mtproto.tl.Streams.writeInt64;

public class Main {

    public static void main(String[] args) throws IOException {
        MtprotoService mtproto = MtprotoService.getInstance();
        mtproto.setCurrentDcID(1);

        TlObject resPq = mtproto.invokeApiCall
                (
                     new TlMethod("req_pq")
                    .put("nonce", Randoms.nextRandomBytes(16))
                );

        PqSolver.Solve(new Pq(resPq.get("pq")));
    }


}
