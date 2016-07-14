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


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.common
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class SystemLogger implements ILogger {

    final Colors  def_color = Colors.WHITE;
    static long number = 0;

    private String createLogNum(){
        number=(number%10000);

        int  offset = ("1000").length() -(number+"").length();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i< offset;i++){
            builder.append("0");
        }
        builder.append(number);
        return builder.toString();

    }
    protected String createMessage(Colors color,Colors color2,String superTag,Object tag,Object... o){
        number++;
        String t ="";
        for (Object l : o){
            t += String.format(" %s ", l );
        }
        return String.format(" > %s %s  %s " ,Colors.RESET.toString()+color.toString(),tag,t);
    }
    @Override
    public synchronized void log(Object... os) {
        Object[] o =Arrays.copyOfRange(os,1,os.length);
        System.out.println(createMessage(def_color,Colors.CYAN,"  LOG  ",os[0],o));
        ConsoleUtils.setForeColor(Colors.RESET);
    }


    @Override
    public synchronized void warn(Object... os) {
        Object[] o =Arrays.copyOfRange(os,1,os.length);
        System.out.println(createMessage(Colors.YELLOW,Colors.YELLOW,"WARNING",os[0],o));
        ConsoleUtils.setForeColor(Colors.RESET);
    }


    @Override
    public synchronized void error(Object... os) {
        Object[] o =Arrays.copyOfRange(os,1,os.length);
        System.out.println(createMessage(Colors.RED,Colors.RED," ERROR ",os[0],o));
    }

    @Override
    public synchronized void table(byte[] data ,String name) {
        System.out.println(createMessage(Colors.GREEN,Colors.BLUE," TABLE ", name , data.length + " Bytes"));
        System.out.print("\t\t\t\t  ");
        ConsoleUtils.setForeColor(Colors.PURPLE);
        for(int i=0;i<data.length;i++){

            System.out.print(HexBin.encode(new byte[]{data[i]}) + " ");
            if ((i+1)%30 == 0) {
                System.out.println();
                System.out.print("\t\t\t\t  ");
            }


        }
        System.out.println();
        ConsoleUtils.setForeColor(Colors.RESET);
    }

    @Override
    public void log(Colors color, Object... os) {
        Object[] o =Arrays.copyOfRange(os,1,os.length);
        System.out.println(createMessage(color,Colors.CYAN,"  LOG  ",os[0],o));
        ConsoleUtils.setForeColor(Colors.RESET);
    }
}
