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

package com.jtel.mtproto;

import com.jtel.mtproto.secure.Randoms;

import java.util.Random;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto.services
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public final class MtpTimeManager {

    private static MtpTimeManager instance;

    public synchronized static MtpTimeManager getInstance() {
        if (instance == null) {
            instance = new MtpTimeManager();
        }
        return instance;
    }

    private long timeDelta;
    private int  seqNo;
    private byte[] sessionid = Randoms.nextRandomBytes(8);

    private MtpTimeManager() {
        seqNo =0;
    }
    public int generateSeqNo(){
        return seqNo++;
    }

    public long getLocalTime() {
        return System.currentTimeMillis();
    }

    public void setTimeDelta(long timeDelta) {
        if(timeDelta == -1) return;
        this.timeDelta = timeDelta;
    }

    public long generateMessageId() {

    int a=        new Random().nextInt(0x0FFFFFFF);

        return  ((getLocalTime()+timeDelta)/1000) << 32 | a ;
    }

    public byte[] getSessionId(){
        return sessionid;
    }



}
