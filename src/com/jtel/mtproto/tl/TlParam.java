package com.jtel.mtproto.tl;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/7/16
 * Package : com.jtel.mtproto.tl
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class TlParam {

    public String type;

    public String name;

    @Override
    public String toString() {
        return String.format(" %s:%s ",name,type);
    }
}
