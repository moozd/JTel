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

package com.jtel.mtproto;

import com.jtel.common.io.Storage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public final class ConfStorage extends Storage {

    private static ConfStorage ins;

    public static ConfStorage getInstance() {
        if (ins == null) {
            ins = new ConfStorage();
        }
        return ins ;
    }


    public ConfStorage() {
        setItem("debug",false);

        setItem("dc1","149.154.175.50" ,true);
        setItem("dc2","149.154.167.51" ,true);
        setItem("dc3","149.154.175.100",true);
        setItem("dc4","149.154.167.91" ,true);
        setItem("dc5","149.154.171.5"  ,true);

        setItem("api-hash"  ,"6e7e20a046e44cb801dc1e8325233396",true);
        setItem("api-id"    ,37516,true);

        setItem("schema-layer",45,true);

        setItem("tl-schema-provide","json");


    }

    public final String getDc(int dc){
        return getItem("dc"+dc);
    }

    public final void setDebugging(boolean d){
        setItem("debug",d);
    }

    public final boolean debug(){
        return getItem("debug");
    }


}
