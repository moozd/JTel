package com.jtel.mtproto.auth.pq;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.mtproto.auth.pq
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */


/**
 * This class store pq number
 * p and q are prime factors of pq number
 * where p < q
 */
public class Pq {

    /**
     * pq number in bytes
     */
    public byte[] pq;

    /**
     * p number in bytes
     */
    public byte[] p;

    /**
     *  q number in bytes
     */
    public byte[] q;

    /**
     * constructor accepts one parameter, to store p and q numbers
     * we will do the math elsewhere.
     * @param pq  one of the parameters of the req_pq method from tl schema is pq number in bytes
     *            . do not generate random numbers to pass as pq number, you must getAuth it from ser
     *            ver throw req_pq method.
     */
    public Pq(byte[] pq){
        this.pq = pq;
    }
}
