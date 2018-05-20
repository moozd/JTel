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

package com.jtel.mtproto.message;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.storage.ConfStorage;
import com.jtel.mtproto.tl.TlMethod;


/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/16/16
 * Package : com.jtel.mtproto.tl
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class InitConnectionMessage extends EncryptedMessage {

    private ConfStorage conf = ConfStorage.getInstance();

    public InitConnectionMessage(MessageHeaders headers) {
        super(null,headers);
        try {
            setContext(new TlMethod("help.getNearestDc"));
        }catch (Exception e){
            //empty
        }
    }

    @Override
    protected byte[] prepareBody(byte[] baseMessage) {

        try{
            TlMethod invokeWithLayer = new TlMethod("invokeWithLayer");
            invokeWithLayer.put("layer", conf.getItem("schema-layer"));
            TlMethod initConnection = new TlMethod("initConnection");
            initConnection.put("api_id", conf.getItem("api-id"));
            initConnection.put("device_model","JTel author: Mohammad Mohammad Zade ");
            initConnection.put("system_version","Linux x86_64");
            initConnection.put("app_version","0.0.3");
            initConnection.put("lang_code","en-US");
            initConnection.put("query", getContext());
            invokeWithLayer.put("query", initConnection);

           //Logger.getInstance().table(invokeWithLayer.serialize(), "init connection");

            return invokeWithLayer.serialize();

        }
        catch (Exception e){
            //empty
           // e.printStackTrace();
            Logger.getInstance().error("InitConnection",e.getMessage());
        }

        return new byte[0];

    }

    /*
*
*  if (!isNetworkInit) {
            writeInt(mainStream,0xda9b0d0d, "invokeWithLayer");
            writeInt(mainStream, Config.schemaLayer,"Schema layer");
            writeInt(mainStream,0x69796de9, "initConnection");
            writeInt(mainStream,Config.apiId,"api id");
            writeString(mainStream,"Unknown UserAgent","user agent");
            writeString(mainStream,"Unknown Platform","platform");
            writeString(mainStream,"0.0.3","version");
            writeString(mainStream,"en","language");
            isNetworkInit = true;

        }
*
* */
}
