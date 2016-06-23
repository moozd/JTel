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

package com.jtel.mtproto.schedule;

import com.jtel.mtproto.message.MessageResponse;
import com.jtel.mtproto.message.TlMessage;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.jtel.mtproto.transport.Transport;
import com.jtel.mtproto.transport.TransportException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/21/16
 * Package : com.jtel.mtproto.schedule
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class MessageScheduler {

    private MessageQueue sentQueue;
    private Transport transport;

    public MessageScheduler() {
        sentQueue =new MessageQueue();
    }



    public void update(MessageQueue queue){
        this.sentQueue =queue;
    }


    public MessageQueue getSentQueue() {
        return sentQueue;
    }

    public void setTransport(Transport transport){
        this.transport = transport;
    }

    public MessageResponse send(long msgId, int dc) throws IOException, InvalidTlParamException,TransportException{
        TlMessage  message = sentQueue.get(msgId);
        byte[] msgBytes = message.serialize();
        byte[] resp     = transport.send(dc,msgBytes);
        message.deSerialize(new ByteArrayInputStream(resp));
        return message.getResponse();
    }

    public MessageResponse send(int dc) throws IOException, InvalidTlParamException,TransportException{
        long msgId = sentQueue.getTop().getHeaders().getMessageId();
        return send(msgId,dc);
    }


}
