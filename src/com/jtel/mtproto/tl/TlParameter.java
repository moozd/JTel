/*
 * This file is part of JTel.
 *
 *     JTel is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     JTel is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with JTel.  If not, see <http://www.gnu.org/licenses/>.
 */

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

public class TlParameter {

    public String type;

    public String name = "primitive";

    Object value = null;

    public void setValue(Object value) {
        this.value = value;
    }

    public <T> T getValue() throws ClassCastException {
        return (T) this.value;
    }


    public TlParameter(){

    }

    public TlParameter(String type){
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
            int count=0;
            for (byte b : (byte[]) getValue()){
                builder.append(" ");
                builder.append(HexBin.encode(new byte[]{b}));
                if(++count >6){
                    builder.append("...");
                    break;
                }
            }
            builder.append(" ]");
            val = builder.toString();
        }
        return String.format(" %s:%s %s  ",name,type,val);
    }
}
