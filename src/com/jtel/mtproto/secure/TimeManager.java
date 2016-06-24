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

package com.jtel.mtproto.secure;

import com.jtel.common.log.Logger;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto.services
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public final class TimeManager {

    private static TimeManager instance;
    private static long lastMessageID =0;
    public synchronized static TimeManager getInstance() {
        if (instance == null) {
            instance = new TimeManager();
        }
        return instance;
    }

    private long timeDelta;
    private int  seqNo;
    private byte[] sessionid = Randoms.nextRandomBytes(8);

    private TimeManager() {
        seqNo =0;
    }
    public int generateSeqNo(boolean isContentRealated){
      //  int res = seqNo *2+1;
      //  seqNo++;
        int a = (isContentRealated)? seqNo++*2 : seqNo++ * 2 +1;
        return a;
    }

    public long getLocalTime() {
        return System.currentTimeMillis();
    }

    public void setTimeDelta(long timeDelta) {
        if(timeDelta == -1) return;
        this.timeDelta = timeDelta;
    }

    public long getUTCTime(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return cal.getTimeInMillis();
    }

    public long getUnixTime(){
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
            Date date = sdf.parse("1970/01/01");
            return date.getTime();
        }catch (Exception e){
            return 0;
        }
    }

    public long generateMessageId() {

            long unixtime = getUnixTime();
            long utctime  = getUTCTime();

            long time = (utctime );
            long newMessageId = ((time / 1000 + timeDelta) << 32) |
                    ((time % 1000) << 22) |
                    (new Random().nextInt(524288) << 2);
            if (newMessageId >= lastMessageID){
                newMessageId += 4;
            }
            lastMessageID =newMessageId;

            return newMessageId;


    }

    public long getTimeDelta() {
        return timeDelta;
    }

    public byte[] getSessionId(){
        return sessionid;
    }



}
