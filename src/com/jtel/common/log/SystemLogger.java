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


import java.util.Arrays;


/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.common
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class SystemLogger implements ILogger {

    protected String createMessage(String superTag,Object tag,Object... o){
        String t ="";
        for (Object l : o){
            t += String.format(" %s ", l );
        }
        return String.format("[ %s ] %s %s " ,superTag,tag,t);
    }

    @Override
    public void log(Object... os) {
        Object[] o =Arrays.copyOfRange(os,1,os.length);
        System.out.println(createMessage("LOG",os[0],o));
    }


    @Override
    public void warn(Object... os) {
        Object[] o =Arrays.copyOfRange(os,1,os.length);
        System.out.println(createMessage("WARNING",os[0],o));
    }


    @Override
    public void error(Object... os) {
        Object[] o =Arrays.copyOfRange(os,1,os.length);
        System.out.println(createMessage("ERROR",os[0],o));
    }


}
