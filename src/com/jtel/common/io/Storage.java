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


import com.jtel.mtproto.auth.AuthCredentials;

import java.io.File;
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


    protected void initialize() {
        pairs = new HashMap<>();
        File cache = new File(getPath());
        if (cache.exists()){
            load();
        }
        else {
            initialItems();
        }

    }




    public Storage(){
        initialize();
    }

    public boolean isCached(){
        File file = new File(getPath());
        return file.exists() ;
    }

    public boolean hasCache(String key){
        return pairs.containsKey(key);
    }

    public void restore(Storage storage){
        pairs = (HashMap<String, Object>) storage.pairs.clone();
    }

    public  void clear(){
        pairs.clear();
        initialItems();
        save();
    }

    public abstract void initialItems();

    public <T> T getItem(String key){
        return (T)pairs.get(key);
    }

    public <T> void setItem(String key , T o){
        pairs.put(key,o);
    }

    public abstract String getPath();

    public abstract void save();

    public abstract void load();

}
