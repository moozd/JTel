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

public class TlParam {

    private String type;

    private String name;

    private Object value;


    public void setValue(Object value) {
        this.value = value;
    }

    public <T> T getValue(){
        try{
  /*          if(getType().equals("string")){
                return(T) new String((byte[])value,"UTF-8");
            }*/
            return (T) this.value;
        }catch (Exception  e){
            return null;
        }

    }


    public boolean isConditionalType() {
        return type.contains("?");
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TlParam(){
        this.name = "primitive";
        this.value = null;
  ;
    }

    public TlParam(String type){
        this.type=type;
        this.name = "primitive";
        this.value = null;
    }

    public TlParam(String type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public TlParam(TlParam param) {
        this.type = param.type;
        this.name = param.name;
        this.value = param.value;
    }



    @Override
    public String toString() {
        String val ="";
        if(getValue() != null){
         val = "= " + getValue();
        if( getType().equals("string")){

                val =getValue();
        }
        }
        return String.format("%s<%s> %s",name,type,val);
    }
}
