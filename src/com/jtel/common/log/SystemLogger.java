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
            t += String.format(",%s ", l );
        }
        t= t.substring(t.indexOf(",")+1);
        return String.format("[%s] %s : %s " ,superTag,tag,t);
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
