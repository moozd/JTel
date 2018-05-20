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

package com.jtel.common.log;


import java.util.ArrayList;
import java.util.List;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 7/15/16
 * Package : com.jtel.common.log
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class BroadcastLogger implements ILogger{

    public interface Callback {
        void status(String s);
    }

   static List<Callback> callback= new ArrayList<>();

    public void setCallback(Callback callback) {
     BroadcastLogger.callback.add(callback);
    }

    @Override
    public void log(Colors color, Object... os) {
        log(os);
    }

    @Override
    public  void log(Object... os) {

           String a = "";

           for (int i = 0; i < os.length ; i++) {
               a+=os[i] + " ";
           }
        for (int i = 0; i < callback.size() ; i++) {
            if(callback.get(i) !=null) callback.get(i).status(a);
        }


    }

    @Override
    public void warn(Object... os) {
        log(os);
    }

    @Override
    public void error(Object... os) {
        log(os);
    }

    @Override
    public void table(byte[] d, String name) {

    }


}
