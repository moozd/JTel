package com.jtel.common.log;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.common
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class Logger implements ILogger {
    private static Logger ins;



    @Override
    public void log(Object... os) {
        context.log(os);
    }



    @Override
    public void warn(Object... os) {
        context.warn(os);
    }



    @Override
    public void error(Object... os) {
        context.error(os);
    }

    private ILogger context;


    private Logger(){
        setContext(new SystemLogger());
    }


    public void setContext(ILogger context) {
        this.context = context;
    }


    public static Logger getInstance() {
        if (ins == null) {
            ins = new Logger();
        }
        return ins;
    }
}
