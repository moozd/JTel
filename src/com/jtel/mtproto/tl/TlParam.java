package com.jtel.mtproto.tl;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

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

    public String name = "primitive";

    Object value = null;

    public void setValue(Object value) {
        this.value = value;
    }

    public <T> T getValue() throws ClassCastException {
        return (T) this.value;
    }


    public TlParam(){

    }

    public TlParam(String type){
        this.type=type;
    }
    @Override
    public String toString() {

        String val = "= " + getValue();
        if( getValue() instanceof byte[]){
            StringBuilder builder = new StringBuilder();
            builder.append("=(len:");
            builder.append(((byte[]) getValue()).length);
            builder.append(")[");
            for (byte b : (byte[]) getValue()){
                builder.append(" ");
                builder.append(HexBin.encode(new byte[]{b}));
            }
            builder.append(" ]");
            val = builder.toString();
        }
        return String.format("\n\t %s:%s %s  \n",name,type,val);
    }
}
