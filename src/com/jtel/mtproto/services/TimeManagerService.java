package com.jtel.mtproto.services;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto.services
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public final class TimeManagerService {

    private static  TimeManagerService instance;

    public synchronized static TimeManagerService getInstance() {
        if (instance == null) {
            instance = new TimeManagerService();
        }
        return instance;
    }

    private int timeDelta;


    private TimeManagerService() {

    }

    public long getLocalTime() {
        return System.currentTimeMillis();
    }

    public void setTimeDelta(int timeDelta) {
        this.timeDelta = timeDelta;
    }

    public long generateMessageId() {
        return ((getLocalTime()+timeDelta)/1000) << 32;
    }



}
