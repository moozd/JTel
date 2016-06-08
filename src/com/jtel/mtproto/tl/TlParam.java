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
            val = "= " + "[len: "+ ((byte[]) getValue()).length + "]" + getValue();
        }
        return String.format(" %s:%s %s  ",name,type,val);
    }
}
