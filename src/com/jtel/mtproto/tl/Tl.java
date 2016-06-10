package com.jtel.mtproto.tl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/7/16
 * Package : com.jtel.mtproto.tl
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public interface Tl {
    byte[] serialize() throws IOException,InvalidTlParamException;
    void  deSerialize(InputStream is) throws IOException;
}
