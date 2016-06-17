package com.jtel.mtproto.auth.pq;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.ConfStorage;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.mtproto.auth.pq
 *
 * copied from https://github.com/ex3ndr/telegram-mt/blob/master/src/main/java/org/telegram/mtproto/secure/pq/PQLopatin.java
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Telegraph LLC
 *
 *Permission is hereby granted, free of charge, to any person obtaining a copy of
 *this software and associated documentation files (the "Software"), to deal in
 *the Software without restriction, including without limitation the rights to
 *use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *the Software, and to permit persons to whom the Software is furnished to do so,
 *subject to the following conditions:
 *
 *The above copyright notice and this permission notice shall be included in all
 *copies or substantial portions of the Software.
 *
 *THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 *
 * @author ex3ndr
 * Costumized by Mohammad Zade
 */

public class PqSolver {

    private static Logger console = Logger.getInstance();
    private long GCD(long a, long b) {
        while (a != 0 && b != 0) {
            while ((b & 1) == 0) {
                b >>= 1;
            }
            while ((a & 1) == 0) {
                a >>= 1;
            }
            if (a > b) {
                a -= b;
            } else {
                b -= a;
            }
        }
        return b == 0 ? a : b;
    }

    private long findP(long what) {
        Random r = new Random();
        long g = 0;
        int it = 0;
        for (int i = 0; i < 3; i++) {
            int q = (r.nextInt(128) & 15) + 17;
            long x = r.nextInt(1000000000) + 1, y = x;
            int lim = 1 << (i + 18);
            for (int j = 1; j < lim; j++) {
                ++it;
                long a = x, b = x, c = q;
                while (b != 0) {
                    if ((b & 1) != 0) {
                        c += a;
                        if (c >= what) {
                            c -= what;
                        }
                    }
                    a += a;
                    if (a >= what) {
                        a -= what;
                    }
                    b >>= 1;
                }
                x = c;
                long z = x < y ? y - x : x - y;
                g = GCD(z, what);
                if (g != 1) {
                    break;
                }
                if ((j & (j - 1)) == 0) {
                    y = x;
                }
            }
            if (g > 1) {
                break;
            }
        }

        long p = what / g;
        return Math.min(p, g);
    }

    protected PqSolver(){}

    public static Pq Solve(Pq pq){
        long time  = System.currentTimeMillis();
        PqSolver solver = new PqSolver();

        String x = HexBin.encode(ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).put(pq.pq).array());
        BigInteger pqInt = new BigInteger(x,16);

        BigInteger p = new BigInteger("" + solver.findP(pqInt.longValue()));
        BigInteger q = pqInt.divide(p);

        pq.p = p.toByteArray();
        pq.q = q.toByteArray();

        if(ConfStorage.getInstance().debug()) console.log("Pq solved ","Finished in " + ( System.currentTimeMillis() - time )/1000f + " s" ,pqInt.toString(), "=",p.toString()," x ",q.toString());
        return pq;
    }
}
