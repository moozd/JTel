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

package com.jtel.mtproto.transport;

import com.jtel.common.log.Logger;
import com.jtel.mtproto.tl.InvalidTlParamException;
import com.sun.istack.internal.Nullable;

import java.io.IOException;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/6/16
 * Package : com.jtel.mtproto.services
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public abstract class   Transport {

   protected boolean errorFlag;
   protected String  errorMessage;
   protected int     errorCode;
   protected byte[] responseAsBytes;
   Logger console = Logger.getInstance();
   protected  abstract void createConnection(int dcId) throws IOException;
   @Nullable

   public byte[] send(int dc , byte[] message) throws IOException,InvalidTlParamException,TransportException {

      errorFlag = false;
      createConnection(dc);
      setResponseAsBytes(onSend(message));
      if (getErrorCode() != 200){
         errorFlag =true;
         errorMessage =  new String(getResponseAsBytes(),"UTF-8");
         throw new TransportException(getErrorCode(),getErrorMessage());
      }

      return getResponseAsBytes();
   }



   protected void setResponseAsBytes(byte[] responseAsBytes) {
      this.responseAsBytes = responseAsBytes;
   }

   public byte[] getResponseAsBytes() {
      return responseAsBytes;
   }

   public boolean isAnErrorOccurred() {
      return errorFlag;

   }

   public int getErrorCode() {
      return errorCode;
   }

   public String getErrorMessage() {
      return errorMessage;
   }

   protected abstract byte[] onSend(byte[] message) throws IOException,InvalidTlParamException;

}
