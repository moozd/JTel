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

package com.jtel.common.io;


import java.io.Serializable;
import java.util.HashMap;

/**
 * This file is part of JTel
 * IntelliJ idea.

 * Date     : 6/11/16
 * Package : com.jtel.mtproto.auth
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public abstract class Storage implements Serializable{

    protected HashMap<String,Object> pairs;
    protected HashMap<String,Boolean> locks;

    public Storage(){
        this.pairs = new HashMap<>();
        this.locks = new HashMap<>();
    }

    public boolean hasKey(String key){
        return pairs.containsKey(key);
    }

    public void restore(Storage storage){
        locks = (HashMap<String, Boolean>)storage.locks.clone();
        pairs = (HashMap<String, Object>) storage.pairs.clone();
    }

    public HashMap<String,Object> toHashMap(){
        if ( this.pairs != null){
            return (HashMap<String, Object>) this.pairs.clone();
        }
       return null;
    }

    public  void clear(){
        pairs.clear();
    }

    public <T> T getItem(String key){
       if (!pairs.containsKey(key)) return null;
        return (T)pairs.get(key);
    }

    public <T> void setItem(String key , T o){
        setItem(key,o,false);
    }

    @Override
    public String toString() {
        String ret = "\""+getClass().getSimpleName()+"\"\n ";
        for (HashMap.Entry<String,Object> dd : pairs.entrySet()) {
            ret +=
                    (locks.containsKey(dd.getKey()) && locks.get(dd.getKey()).equals(true) ? " [Readonly] " : " [Editable] ")
                    +"\""+ dd.getKey() + "\": \"" + dd.getValue()+"\"" + " \n ";
        }
        return ret;
    }

    public <T> void setItem(String key , T o, boolean readOnly){
        try {
            if (locks.containsKey(key)) {
                if (locks.get(key)) {
                    throw new ReadOnlyPropertyException(key);
                }
            }
            pairs.put(key, o);
            locks.put(key, readOnly);
        }catch (ReadOnlyPropertyException e){
            throw new RuntimeException(e);
        }


    }



}
