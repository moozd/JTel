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

import com.jtel.mtproto.message.TlMessage;

import java.util.LinkedList;
import java.util.List;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/21/16
 * Package : com.jtel.mtproto.schedule
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class MessageQueue {

    protected List<MessageSet> messageSets;

    public MessageQueue() {
        messageSets = new LinkedList<>();
    }

    public int getCount(){
        return messageSets.size();
    }

    public void push(TlMessage message){
        messageSets.add(new MessageSet(message));
    }

    public TlMessage poll(){
        TlMessage message = messageSets.get(0).getMessage();
        messageSets.remove(0);
        return message;
    }

    public TlMessage get(long messageId){
        int i = find(messageId);
        if(i>=0){
           return messageSets.get(i).getMessage();
        }
        return null;
    }

    protected int find(long messageId){
        for (int i=0;i<getCount();i++){
            if(messageId == messageSets.get(i).getMessageId()) return i;
        }
        return -1;
    }

    public TlMessage getTop(){
        return messageSets.get(0).getMessage();
    }

    public void remove(long msgId){
        int i = find(msgId);
        if(i>=0){
            messageSets.remove(i);
        }
    }

    public boolean isConfirmed(long msgId){
        int i = find(msgId);
        if(i>=0){
            return messageSets.get(i).isConfirmed();
        }
        return false;
    }

    public void confirm(long messageId){
        int i = find(messageId);
        if(i>=0){
            messageSets.get(i).setConfirmed(true);
        }
    }


    private class MessageSet {

        private TlMessage message;
        private long      messageId;
        private boolean confirmed = false;

        public MessageSet(TlMessage message) {
            this.messageId = message.getHeaders().getMessageId();
            this.message = message;
        }

        public void setConfirmed(boolean confirmed) {
            this.confirmed = confirmed;
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public TlMessage getMessage() {
            return message;
        }

        public long getMessageId() {
            return messageId;
        }


    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (MessageSet message: messageSets) {
            builder.append(" [id : ");
            builder.append(message.getMessageId());
            builder.append("  ");
            builder.append(message.getMessage());
            builder.append("]");
        }
        return builder.toString();
    }


}
