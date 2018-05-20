
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

import com.jtel.common.db.ConfCurrentState;
import com.jtel.common.db.DbContext;
import com.jtel.common.io.FileStorage;
import com.jtel.common.log.Colors;
import com.jtel.common.log.Logger;

import com.jtel.common.db.ConfCredentials;
import com.jtel.mtproto.auth.AuthFailedException;
import com.jtel.mtproto.auth.AuthManager;

import com.jtel.mtproto.message.*;

import com.jtel.mtproto.schedule.MessageQueue;
import com.jtel.mtproto.secure.Randoms;
import com.jtel.mtproto.secure.TimeManager;
import com.jtel.mtproto.tl.*;

import com.jtel.mtproto.transport.Transport;
import com.jtel.mtproto.transport.TransportException;
import com.sun.xml.internal.bind.v2.TODO;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.mtproto
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

/**
 * MTProto service saves api current state to invoke MTP  or API  rpc method
 * to Telegram servers
 * this class is singleton
 */

public class MtpClient {

    private final String TAG = "Client";
    /**
     * class instance to hold single instance of it
     */
    private static MtpClient instance;

    /**
     * method to getAuth current instance or create new one in case it is not created yet
     * @return object of this class
     */
    public static MtpClient getInstance() {
        if (instance == null) {
            instance = new MtpClient();

        }
        return instance;
    }



    /**
     * logger object to log operations for debugging
     */
    private Logger      console;

    
    /**
     *authenticates client for each dc
     * @see com.jtel.mtproto.auth.AuthManager 
     */
    private AuthManager authManager;

    /**
     * network layer ,controls the data transfer
     * @see com.jtel.mtproto.transport.Transport
     */
    private Transport   transport;


    /**
     * sent messages history
     */
    private MessageQueue sentQueue;







    /**
     * private constructor you cannot create instance of this class using new you must cull getInstance()
     */
    private MtpClient(){
        this.console         = Logger.getInstance();
        this.sentQueue       = new MessageQueue();
    }
    

    /**
     * checks if client is not authenticated to a dc then authenticates
     * @throws AuthFailedException
     */
    protected void checkCurrentDc() throws AuthFailedException{
        if (!isAuthenticatedOnDc(getDc())){
            saveAuth(getDc(),authManager.authenticate(getDc()));
        }
    }

    /**
     * save auth data for a dc
     * @param dc dc number
     * @param auth auth data to be saved
     *             @see ConfCredentials
     */
    protected void saveAuth(int dc, ConfCredentials auth) {
        new DbContext().updateCredentials(auth);
    }



    /**
     * is user authenticated to telegram service?
     * @return true if storage object auth_state is true
     *         this means client has completed the authentication
     */
    protected boolean isAuthenticatedOnDc(int dc){
        return new DbContext().getCredential(dc).getServerSalt() == new byte[0];
    }



    /**
     * setter
     * @param transport transport object to send messages over network
     */
    protected void setTransport(Transport transport) {
        this.transport = transport;
    }

    protected void setAuthManager(AuthManager authManager) {
        this.authManager = authManager;
    }

    /**
     * getter
     * @return true if client did authentication before
     */
    public  boolean isNetworkReady(){
        return ! new DbContext().getCurrentState().isAuthenticationRequired(); //storage.getItem("auth_state") == null ? false : storage.getItem("auth_state") ;
    }
    

    /**
     * setForeColor current dc id
     * @param currentDcID data center id
     */
    public void setDc(int currentDcID) {
        ConfCurrentState state = new DbContext().getCurrentState();
        state.setDcId(currentDcID);
        new DbContext().updateApiState(state);
    }


    /**
     *
     * @return getAuth current data center id
     */
    public int getDc() {
        return new DbContext().getCurrentState().getDcId();
    }



    /**
     * clear storage items
     */
    public void reset(){
        ////// TODO: 7/24/2016 fix this
    }

    /**
     * createSession client to start authenticate
     * @param transport transport object
     * @throws MtpException
     */
    public void createSession( Transport transport) throws MtpException{
        createSession(transport,false);
    }

    /**
     * 

     * @param transport transport object
     * @param reset rest if (true) storage will be removed
     * @throws MtpException
     */
    public void createSession( Transport transport, boolean reset) throws MtpException{

        setTransport(transport);
        setAuthManager(new AuthManager(transport));
        if(reset){
            reset();
        }
        if(!isNetworkReady()){
            try {
                authenticate();
            }catch (AuthFailedException e){
                throw  new MtpException(MtpStates.MTP_SERVER_HAND_SHAKE_FAILED,"client could not authenticate from server "+ getDc() ,e);

            }
           setDc(1);
        }


        ConfCurrentState state = new DbContext().getCurrentState();
        state.setSessionId(Randoms.nextRandomBytes(8));
        new DbContext().updateApiState(state);
        initConnection();

    }

    /**
     * getAuth AuthStorage object by data center id
     * @param dc data center id
     * @return auth_key and server_salt
     */
    public ConfCredentials getAuth(int dc) {
        return new DbContext().getCredential(dc);
    }

    /**
     * authenticates all dc ides and stores their values
     * @throws AuthFailedException
     */
    protected void authenticate() throws AuthFailedException{
        for (int i =1;i<6;i++){
            authenticate(i);
        }
    }

    protected void authenticate(int dc) throws AuthFailedException{
        saveAuth(dc,authManager.authenticate(dc));
    }


    public boolean isUserSignedIn(){
       return ! new DbContext().getCurrentState().isSignInRequired();
    }

    public  void setSignIn(boolean t){
        ConfCurrentState state = new DbContext().getCurrentState();
        state.setSignInRequired(false);
        new DbContext().updateApiState(state);
    }


    /**
     * creates rpc headers for encrypted messages
     * @param dc to get auth data and server time registered to this dc
     * @return rpc header object that contains everything that
     * a message serialization needs
     * @see MessageHeaders
     */
    public MessageHeaders createMessageHeaders(int dc,boolean contentRelated){
        MessageHeaders headers = new MessageHeaders();
        ConfCredentials credentials = getAuth(dc);
        TimeManager.getInstance().setTimeDelta(credentials.getServerTime());
        headers.setAuthKey(credentials.getAuthKey());
        headers.setAuthKeyId(credentials.getAuthKeyId());
        headers.setServerSalt(credentials.getServerSalt());
        headers.setSessionId(new DbContext().getCurrentState().getSessionId());
        headers.setSequenceId(TimeManager.getInstance().generateSeqNo(contentRelated));
        headers.setMessageId(TimeManager.getInstance().generateMessageId());
        return headers;
    }

    /**
     * init connection is a special method of telegram api it start with
     * invokeWithLayer method that determines layer number and initConnection
     * method that sends api id user agent and some other stuffs to servers
     */
    public void initConnection () throws MtpException{
        console.log(TAG ,"initializing connection");
        int currentDc = getDc();
        TlMessage message = new InitConnectionMessage(createMessageHeaders(currentDc,false));
        TlObject nearestDc =sendMessage(message);
       // TlMethod method = message.getContext();
        //console.log(method);
        console.log(Colors.CYAN,TAG,"","country="+nearestDc.get("country"),"dc="+nearestDc.get("nearest_dc"));
        setDc(nearestDc.get("nearest_dc"));

    }


    /**
     * sendSync a mtp rpc request known as low level request that  requires authentication
     * message that is created throw this method is known as encrypted message.
     * @param method @see com.JTel.mtproto.tl.TlMethod
     * @return @see com.JTel.mtproto.tl.TlObject
     */
    public TlObject invokeApiCall(TlMethod method) throws MtpException{
        try {
            checkCurrentDc();
        }
        catch (AuthFailedException e){
            // handle
        }
        int currentDc = getDc();
       return sendMessage(new EncryptedMessage(method, createMessageHeaders(currentDc,false)));

    }


    /**
     * sending messages starts from here Encrypted or Unencrypted
     *
     * @param message message to send to telegram server (RPC)
     *                @see com.jtel.mtproto.message.TlMessage
     * @return RPC result after deserialization
     *         @see com.jtel.mtproto.tl.TlObject
     */
    protected TlObject sendMessage(TlMessage message) throws MtpException{
        long t = System.currentTimeMillis();
        try {
            console.log(Colors.PURPLE,"requesting","(dc:"+getDc()+")",message.getContext().getName());
            byte[] msg = message.serialize();
            int currentDc = getDc();
            byte[] response = transport.send(currentDc, msg);
            message.deSerialize(new ByteArrayInputStream(response));
            console.log(Colors.GREEN,"Done in ",(System.currentTimeMillis()-t)/1000F+"s");
            /*if(!(message instanceof AckMessage))*/sentQueue.push(message);
        }
        catch (TransportException e){
            throw new MtpException(MtpStates.NETWORK_FAILED,  e.getMessage(),e);
        }
        catch (InvalidTlParamException e){
            console.error(TAG,"InvalidTlParam error :",e.getMessage());
            throw new MtpException(MtpStates.TL_BAD_PARAMETER_TYPE, e.getMessage(),e);

        }
        catch (IOException e){
            throw new MtpException(MtpStates.MTP_UNKNOWN_FAILURE, e.getMessage(),e);

        }

        TlObject ret = new TlObject();
        if( processResponse(message.getHeaders().getMessageId(),message.getResponse().getObject())){
            ret = rpcResult;
        }
        return ret;
    }

    protected TlObject rpcResult;

    static  final String MSG_UNKNOWN    = "unknown";
    static  final String MSG_ID         = "msg_id";
    static  final String MSG_CONTAINER  = "msg_container";
    static  final String MESSAGES       = "messages";
    static  final String MESSAGE       = "message";
    static  final String MSG_ACK        = "msgs_ack";
    static  final String MSG_IDS        = "msg_ids";
    static  final String BAD_MSG_ID     = "bad_msg_id";
    static  final String BAD_SERVER_SALT= "bad_server_salt";
    static  final String NEW_SERVER_SALT= "new_server_salt";
    static  final String RPC_RESULT     = "rpc_result";
    static  final String RESULT         = "result";
    static  final String BODY           = "body";
    static  final String RPC_ERROR      = "rpc_error";
    static  final String ERROR_MESSAGE  = "error_message";
    static  final String ERROR_CODE     = "error_code";
    static  final String FLOOD_WAIT     = "flood_wait";
    static  final String FILE_MIGRATE  = "FILE_MIGRATE_";
    static  final String PHONE_MIGRATE = "PHONE_MIGRATE_";
    static  final String MSG_DETAILED_INFO = "msg_detailed_info";

    protected boolean processResponse(long messageId,TlObject object) throws MtpException{

        String   predicate = object.getName();
     //   console.log("processing message", predicate,object.getParams());
        boolean res = true;
        switch (predicate){
            case MSG_UNKNOWN:
                throw new MtpException(MtpStates.MTP_UNKNOWN_FAILURE,"Object type is unknown.");
            case MSG_CONTAINER:
                List<TlObject> messages = object.get(MESSAGES);
                for(TlObject message : messages){
                 //  console.log(message);
                   res = processResponse(message.get(MSG_ID),message);
                }
              break;
            case MSG_ACK:
                List<Long> messageAck = object.get(MSG_IDS);
                res = handleMessageAck(messageAck);
                break;
            case BAD_SERVER_SALT:
                res = handleBadServerSalt(getDc(),object.get(NEW_SERVER_SALT));
                break;
            case MESSAGE:
                TlObject body = object.get(BODY);
                res = processResponse(messageId,body);
                break;
            case RPC_RESULT:
                res = handleRpc(messageId,object);
                break;
//            case MSG_DETAILED_INFO:
//                long answer = object.get("answer_msg_id");
//                res = handleMessageAck(Arrays.<Long>asList(answer));

        }
       // console.log("IDLE");
        return res;
    }

    protected boolean handleBadServerSalt(int dc, long salt) throws MtpException{
        console.log("BAD_SERVER_SALT " + salt);
        ConfCredentials credentials = getAuth(dc);
        credentials.setServerSalt(salt);
        saveAuth(dc, credentials);
        console.table(credentials.getServerSalt(),"salt");
        TlMessage lastMessage = sentQueue.getTop();

        lastMessage.getHeaders().setServerSalt(credentials.getServerSalt());
        TlObject resend = sendMessage(lastMessage);

        return processResponse(lastMessage.getHeaders().getMessageId(),resend);
    }

    protected boolean handleMessageAck(List<Long> messageIds) throws MtpException{
        TlMessage ack;
        try {
             ack = new AckMessage(createMessageHeaders(getDc(), false), new ArrayList<Long>(messageIds));
        }catch (InvalidTlParamException e){
            throw  new MtpException(MtpStates.TL_BAD_PARAMETER_TYPE,"ack message bad type.",e);
        }
        catch (IOException e){
            throw  new MtpException(MtpStates.NETWORK_FAILED,"could not send ack messages.",e);
        }
            TlObject res = sendMessage(ack);
            return processResponse(ack.getHeaders().getMessageId(),res);
    }

    protected boolean handleRpc(long messageId,TlObject rpc) throws MtpException{

        TlObject tempResult = rpc.get(RESULT);

        if(tempResult.getName().equals(RPC_ERROR)){
            String   error = tempResult.get(ERROR_MESSAGE);
       //     Dialogs.showError(error);
            switch ((int) tempResult.get(ERROR_CODE)){
                case 303:
                    String pattern = "/(\\w+_)(\\d+)/g";
                    Pattern r = Pattern.compile(pattern);
                    Matcher matcher = r.matcher(error);
                    String type = matcher.group(0);
                    int value   = Integer.parseInt(matcher.group(2));
                    if(type.equals(FILE_MIGRATE) || type.equals(PHONE_MIGRATE)){
                        console.error(error);
                        setDc(value);
                        TlMessage message = sentQueue.poll();
                       return processResponse(message.getHeaders().getMessageId(),sendMessage(message));
                    }
                    break;
                case 420:
                    int X =Integer.parseInt(error.substring(error.lastIndexOf("_")+1));
                    String tag = error.substring(0,error.lastIndexOf("_"));
                    switch (tag){
                        case FLOOD_WAIT:
                            console.error(TAG,"\\",tag,"\\");
                            console.error(TAG,"you have to wait about " +Math.floor( X/60 )+" minutes.");
                            break;
                    }
                case 401:
                    console.error(TAG,"\\",error,"\\");
                    break;

            }

        }

        rpcResult = tempResult;
        return true;

    }

    public TlObject sendLongPoll(){
        console.log(TAG,"Long poll sent.");
        try{
            return invokeApiCall(new TlMethod("http_wait").put("max_delay",500).put("wait_after",500).put("max_wait",25000));
        }catch (Exception e){
            //nothing
            return null;
        }

    }



}