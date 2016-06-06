package com.jtel.mtproto.tl;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/7/16
 * Package : com.jtel.mtproto.tl
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class TlObject {

    int id;
    public String predicate;
    public List<TlParam> params;
    public String type;

    @Override
    public String toString() {
        return String.format("%s#%s %s = %s",predicate, HexBin.encode( ByteBuffer.allocate(4).putInt(id).array()),params.toString(),type);
    }
}
