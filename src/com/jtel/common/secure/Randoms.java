package com.jtel.common.secure;

import java.util.Random;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.common.secure
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class Randoms {
    static Random rand = new Random();
    public static byte[] nextRandomBytes(int byteSize){
        byte[] buff = new byte[byteSize];
        rand.nextBytes(buff);
        return buff;
    }
}
