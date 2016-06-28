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

package com.jtel.api;

import com.jtel.api.base.ApiContext;
import com.jtel.api.base.Pair;
import com.jtel.common.io.FileStorage;
import com.jtel.mtproto.MtpClient;
import com.jtel.mtproto.auth.AuthFailedException;
import com.jtel.mtproto.tl.TlObject;
import com.jtel.mtproto.transport.Transport;

import java.util.List;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/27/16
 * Package : com.jtel.api
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class TelegramApi {


    public final Account  account ;
    public final Auth     auth;
    public final Channels channels;
    public final Contacts contacts;
    public final Help     help;
    public final Messages messages;
    public final Updates  updates;
    public final Upload   upload;
    public final Users    users;


    public TelegramApi(FileStorage storage, Transport transport)  throws AuthFailedException{

       //MtpClient configuration
       MtpClient.getInstance().createSession(storage,transport);

       //loading methods
       this.account  = new Account();
       this.auth     = new Auth();
       this.channels = new Channels();
       this.contacts = new Contacts();
       this.help     = new Help();
       this.messages = new Messages();
       this.updates  = new Updates();
       this.upload   = new Upload();
       this.users    = new Users();
   }


    /* auto generted code from tl schema */


    /**
     * This file is part of JTel
     * IntelliJ idea.
     * Date     : 6/27/16
     * Package : com.jtel.api
     *
     * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
     */

    public class Account extends ApiContext {

        /**
         *
         * @param token_type  (TL Definition : int)
         * @param token  (TL Definition : string)
         * @param device_model  (TL Definition : string)
         * @param system_version  (TL Definition : string)
         * @param app_version  (TL Definition : string)
         * @param app_sandbox  (TL Definition : Bool)
         * @param lang_code  (TL Definition : string)
         * @return Bool
         */
        public TlObject registerDevice (int token_type, String token, String device_model, String system_version, String app_version, boolean app_sandbox, String lang_code) {
            return generate(

                    new Pair("token_type", token_type),
                    new Pair("token", token),
                    new Pair("device_model", device_model),
                    new Pair("system_version", system_version),
                    new Pair("app_version", app_version),
                    new Pair("app_sandbox", app_sandbox),
                    new Pair("lang_code", lang_code)
            );
        }

        /**
         *
         * @param token_type  (TL Definition : int)
         * @param token  (TL Definition : string)
         * @return Bool
         */
        public  TlObject unregisterDevice ( int token_type, String token) {
            return generate(

                    new Pair("token_type", token_type),
                    new Pair("token", token)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputNotifyPeer)
         * @param settings  (TL Definition : InputPeerNotifySettings)
         * @return Bool
         */
        public  TlObject updateNotifySettings ( TlObject peer, TlObject settings) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("settings", settings)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputNotifyPeer)
         * @return PeerNotifySettings
         */
        public  TlObject getNotifySettings ( TlObject peer) {
            return generate(

                    new Pair("peer", peer)
            );
        }

        /**
         *
         * @return Bool
         */
        public  TlObject resetNotifySettings () {
            return generate(

            );
        }

        /**
         *
         * @param first_name  (TL Definition : string)
         * @param last_name  (TL Definition : string)
         * @return User
         */
        public  TlObject updateProfile ( String first_name, String last_name) {
            return generate(

                    new Pair("first_name", first_name),
                    new Pair("last_name", last_name)
            );
        }

        /**
         *
         * @param offline  (TL Definition : Bool)
         * @return Bool
         */
        public  TlObject updateStatus ( boolean offline) {
            return generate(

                    new Pair("offline", offline)
            );
        }

        /**
         *
         * @return Vector<WallPaper>
         */
        public  TlObject getWallPapers () {
            return generate(

            );
        }

        /**
         *
         * @param peer  (TL Definition : InputPeer)
         * @param reason  (TL Definition : ReportReason)
         * @return Bool
         */
        public  TlObject reportPeer ( TlObject peer, TlObject reason) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("reason", reason)
            );
        }

        /**
         *
         * @param username  (TL Definition : string)
         * @return Bool
         */
        public  TlObject checkUsername ( String username) {
            return generate(

                    new Pair("username", username)
            );
        }

        /**
         *
         * @param username  (TL Definition : string)
         * @return User
         */
        public  TlObject updateUsername ( String username) {
            return generate(

                    new Pair("username", username)
            );
        }

        /**
         *
         * @param key  (TL Definition : InputPrivacyKey)
         * @return account.PrivacyRules
         */
        public  TlObject getPrivacy ( TlObject key) {
            return generate(

                    new Pair("key", key)
            );
        }

        /**
         *
         * @param key  (TL Definition : InputPrivacyKey)
         * @param rules  (TL Definition : Vector<InputPrivacyRule>)
         * @return account.PrivacyRules
         */
        public  TlObject setPrivacy ( TlObject key, List<TlObject> rules) {
            return generate(

                    new Pair("key", key),
                    new Pair("rules", rules)
            );
        }

        /**
         *
         * @param reason  (TL Definition : string)
         * @return Bool
         */
        public  TlObject deleteAccount ( String reason) {
            return generate(

                    new Pair("reason", reason)
            );
        }

        /**
         *
         * @return AccountDaysTTL
         */
        public  TlObject getAccountTTL () {
            return generate(

            );
        }

        /**
         *
         * @param ttl  (TL Definition : AccountDaysTTL)
         * @return Bool
         */
        public  TlObject setAccountTTL ( TlObject ttl) {
            return generate(

                    new Pair("ttl", ttl)
            );
        }

        /**
         *
         * @param phone_number  (TL Definition : string)
         * @return account.SentChangePhoneCode
         */
        public  TlObject sendChangePhoneCode ( String phone_number) {
            return generate(

                    new Pair("phone_number", phone_number)
            );
        }

        /**
         *
         * @param phone_number  (TL Definition : string)
         * @param phone_code_hash  (TL Definition : string)
         * @param phone_code  (TL Definition : string)
         * @return User
         */
        public  TlObject changePhone ( String phone_number, String phone_code_hash, String phone_code) {
            return generate(

                    new Pair("phone_number", phone_number),
                    new Pair("phone_code_hash", phone_code_hash),
                    new Pair("phone_code", phone_code)
            );
        }

        /**
         *
         * @param period  (TL Definition : int)
         * @return Bool
         */
        public  TlObject updateDeviceLocked ( int period) {
            return generate(

                    new Pair("period", period)
            );
        }

        /**
         *
         * @return account.Authorizations
         */
        public  TlObject getAuthorizations () {
            return generate(

            );
        }

        /**
         *
         * @param hash  (TL Definition : long)
         * @return Bool
         */
        public  TlObject resetAuthorization ( long hash) {
            return generate(

                    new Pair("hash", hash)
            );
        }

        /**
         *
         * @return account.Password
         */
        public  TlObject getPassword () {
            return generate(

            );
        }

        protected Account() {
            super();
        }

        /**
         *

         * @param current_password_hash  (TL Definition : bytes)
         * @return account.PasswordSettings
         */
        public  TlObject getPasswordSettings (byte[] current_password_hash) {
            return generate(

                    new Pair("current_password_hash", current_password_hash)
            );
        }

        /**
         *
         * @param current_password_hash  (TL Definition : bytes)
         * @param new_settings  (TL Definition : account.PasswordInputSettings)
         * @return Bool
         */
        public  TlObject updatePasswordSettings ( byte[] current_password_hash, TlObject new_settings) {
            return generate(

                    new Pair("current_password_hash", current_password_hash),
                    new Pair("new_settings", new_settings)
            );
        }
    }


    /**
     * This file is part of JTel
     * IntelliJ idea.
     * Date     : 6/25/16
     * Package : com.jtel.api
     *
     * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
     */

    public class Auth extends ApiContext {

        protected Auth() {
            super();
        }

        /**
         *
         * @param phone_number  (TL Definition : string)
         * @return auth.CheckedPhone
         */
        public  TlObject checkPhone ( String phone_number) {
            return generate(

                    new Pair("phone_number", phone_number)
            );
        }

        /**
         *
         * @param phone_number  (TL Definition : string)
         * @param sms_type  (TL Definition : int)
         * @param api_id  (TL Definition : int)
         * @param api_hash  (TL Definition : string)
         * @param lang_code  (TL Definition : string)
         * @return auth.SentCode
         */
        public  TlObject sendCode ( String phone_number, int sms_type, int api_id, String api_hash, String lang_code) {
            return generate(

                    new Pair("phone_number", phone_number),
                    new Pair("sms_type", sms_type),
                    new Pair("api_id", api_id),
                    new Pair("api_hash", api_hash),
                    new Pair("lang_code", lang_code)
            );
        }

        /**
         *
         * @param phone_number  (TL Definition : string)
         * @param sms_type  (TL Definition : int)
         * @param lang_code  (TL Definition : string)
         * @return auth.SentCode
         */
        public  TlObject sendCode ( String phone_number, int sms_type,String lang_code) {
            return generate(

                    new Pair("phone_number", phone_number),
                    new Pair("sms_type" , sms_type),
                    new Pair("api_id"   , getApiId()),
                    new Pair("api_hash" , getApiHash()),
                    new Pair("lang_code", lang_code)
            );
        }

        /**
         *
         * @param phone_number  (TL Definition : string)
         * @param phone_code_hash  (TL Definition : string)
         * @return Bool
         */
        public  TlObject sendCall ( String phone_number, String phone_code_hash) {
            return generate(

                    new Pair("phone_number", phone_number),
                    new Pair("phone_code_hash", phone_code_hash)
            );
        }

        /**
         *
         * @param phone_number  (TL Definition : string)
         * @param phone_code_hash  (TL Definition : string)
         * @param phone_code  (TL Definition : string)
         * @param first_name  (TL Definition : string)
         * @param last_name  (TL Definition : string)
         * @return auth.Authorization
         */
        public  TlObject signUp ( String phone_number, String phone_code_hash, String phone_code, String first_name, String last_name) {
            return generate(

                    new Pair("phone_number", phone_number),
                    new Pair("phone_code_hash", phone_code_hash),
                    new Pair("phone_code", phone_code),
                    new Pair("first_name", first_name),
                    new Pair("last_name", last_name)
            );
        }

        /**
         *
         * @param phone_number  (TL Definition : string)
         * @param phone_code_hash  (TL Definition : string)
         * @param phone_code  (TL Definition : string)
         * @return auth.Authorization
         */
        public  TlObject signIn ( String phone_number, String phone_code_hash, String phone_code) {
            return generate(

                    new Pair("phone_number", phone_number),
                    new Pair("phone_code_hash", phone_code_hash),
                    new Pair("phone_code", phone_code)
            );
        }

        /**
         *
         * @return Bool
         */
        public  TlObject logOut () {
            return generate(

            );
        }

        /**
         *
         * @return Bool
         */
        public  TlObject resetAuthorizations () {
            return generate(

            );
        }

        /**
         *
         * @param phone_numbers  (TL Definition : Vector<string>)
         * @param message  (TL Definition : string)
         * @return Bool
         */
        public  TlObject sendInvites ( List<String> phone_numbers, String message) {
            return generate(

                    new Pair("phone_numbers", phone_numbers),
                    new Pair("message", message)
            );
        }

        /**
         *
         * @param dc_id  (TL Definition : int)
         * @return auth.ExportedAuthorization
         */
        public  TlObject exportAuthorization ( int dc_id) {
            return generate(

                    new Pair("dc_id", dc_id)
            );
        }

        /**
         *
         * @param id  (TL Definition : int)
         * @param bytes  (TL Definition : bytes)
         * @return auth.Authorization
         */
        public  TlObject importAuthorization ( int id, byte[] bytes) {
            return generate(

                    new Pair("id", id),
                    new Pair("bytes", bytes)
            );
        }

        /**
         *
         * @param perm_auth_key_id  (TL Definition : long)
         * @param nonce  (TL Definition : long)
         * @param expires_at  (TL Definition : int)
         * @param encrypted_message  (TL Definition : bytes)
         * @return Bool
         */
        public  TlObject bindTempAuthKey ( long perm_auth_key_id, long nonce, int expires_at, byte[] encrypted_message) {
            return generate(

                    new Pair("perm_auth_key_id", perm_auth_key_id),
                    new Pair("nonce", nonce),
                    new Pair("expires_at", expires_at),
                    new Pair("encrypted_message", encrypted_message)
            );
        }

        /**
         *
         * @param phone_number  (TL Definition : string)
         * @param phone_code_hash  (TL Definition : string)
         * @return Bool
         */
        public  TlObject sendSms ( String phone_number, String phone_code_hash) {
            return generate(

                    new Pair("phone_number", phone_number),
                    new Pair("phone_code_hash", phone_code_hash)
            );
        }

        /**
         *
         * @param flags  (TL Definition : int)
         * @param api_id  (TL Definition : int)
         * @param api_hash  (TL Definition : string)
         * @param bot_auth_token  (TL Definition : string)
         * @return auth.Authorization
         */
        public  TlObject importBotAuthorization ( int flags, int api_id, String api_hash, String bot_auth_token) {
            return generate(

                    new Pair("flags", flags),
                    new Pair("api_id", api_id),
                    new Pair("api_hash", api_hash),
                    new Pair("bot_auth_token", bot_auth_token)
            );
        }

        /**
         *
         * @param password_hash  (TL Definition : bytes)
         * @return auth.Authorization
         */
        public  TlObject checkPassword ( byte[] password_hash) {
            return generate(

                    new Pair("password_hash", password_hash)
            );
        }

        /**
         *
         * @return auth.PasswordRecovery
         */
        public  TlObject requestPasswordRecovery () {
            return generate(

            );
        }

        /**
         *
         * @param code  (TL Definition : string)
         * @return auth.Authorization
         */
        public  TlObject recoverPassword ( String code) {
            return generate(

                    new Pair("code", code)
            );
        }


    }


    /**
     * This file is part of JTel
     * IntelliJ idea.
     * Date     : 6/27/16
     * Package : com.jtel.api
     *
     * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
     */

    public  class Channels extends ApiContext {
        protected Channels() {
            super();
        }
        /**
         *
         * @param offset  (TL Definition : int)
         * @param limit  (TL Definition : int)
         * @return messages.Dialogs
         */
        public  TlObject getDialogs ( int offset, int limit) {
            return generate(

                    new Pair("offset", offset),
                    new Pair("limit", limit)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param offset_id  (TL Definition : int)
         * @param add_offset  (TL Definition : int)
         * @param limit  (TL Definition : int)
         * @param max_id  (TL Definition : int)
         * @param min_id  (TL Definition : int)
         * @return messages.Messages
         */
        public  TlObject getImportantHistory ( TlObject channel, int offset_id, int add_offset, int limit, int max_id, int min_id) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("offset_id", offset_id),
                    new Pair("add_offset", add_offset),
                    new Pair("limit", limit),
                    new Pair("max_id", max_id),
                    new Pair("min_id", min_id)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param max_id  (TL Definition : int)
         * @return Bool
         */
        public  TlObject readHistory ( TlObject channel, int max_id) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("max_id", max_id)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param id  (TL Definition : Vector<int>)
         * @return messages.AffectedMessages
         */
        public  TlObject deleteMessages ( TlObject channel, List<Integer> id) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("id", id)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param user_id  (TL Definition : InputUser)
         * @return messages.AffectedHistory
         */
        public  TlObject deleteUserHistory ( TlObject channel, TlObject user_id) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("user_id", user_id)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param user_id  (TL Definition : InputUser)
         * @param id  (TL Definition : Vector<int>)
         * @return Bool
         */
        public  TlObject reportSpam ( TlObject channel, TlObject user_id, List<Integer> id) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("user_id", user_id),
                    new Pair("id", id)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param id  (TL Definition : Vector<int>)
         * @return messages.Messages
         */
        public  TlObject getMessages ( TlObject channel, List<Integer> id) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("id", id)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param filter  (TL Definition : ChannelParticipantsFilter)
         * @param offset  (TL Definition : int)
         * @param limit  (TL Definition : int)
         * @return channels.ChannelParticipants
         */
        public  TlObject getParticipants ( TlObject channel, TlObject filter, int offset, int limit) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("filter", filter),
                    new Pair("offset", offset),
                    new Pair("limit", limit)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param user_id  (TL Definition : InputUser)
         * @return channels.ChannelParticipant
         */
        public  TlObject getParticipant ( TlObject channel, TlObject user_id) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("user_id", user_id)
            );
        }

        /**
         *
         * @param id  (TL Definition : Vector<InputChannel>)
         * @return messages.Chats
         */
        public  TlObject getChannels ( List<TlObject> id) {
            return generate(

                    new Pair("id", id)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @return messages.ChatFull
         */
        public  TlObject getFullChannel ( TlObject channel) {
            return generate(

                    new Pair("channel", channel)
            );
        }

        /**
         *
         * param flags  (TL Definition : #)
         * param broadcast  (TL Definition : flags.0?true)
         * param megagroup  (TL Definition : flags.1?true)
         * @param title  (TL Definition : string)
         * @param about  (TL Definition : string)
         * @return Updates
         */
        public  TlObject createChannel ( String title, String about) {
            return generate(

                     //new Pair("flags", flags),
                    //new Pair("broadcast", broadcast),
                    //new Pair("megagroup", megagroup),
                    new Pair("title", title),
                    new Pair("about", about)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param about  (TL Definition : string)
         * @return Bool
         */
        public  TlObject editAbout ( TlObject channel, String about) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("about", about)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param user_id  (TL Definition : InputUser)
         * @param role  (TL Definition : ChannelParticipantRole)
         * @return Updates
         */
        public  TlObject editAdmin ( TlObject channel, TlObject user_id, TlObject role) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("user_id", user_id),
                    new Pair("role", role)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param title  (TL Definition : string)
         * @return Updates
         */
        public  TlObject editTitle ( TlObject channel, String title) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("title", title)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param photo  (TL Definition : InputChatPhoto)
         * @return Updates
         */
        public  TlObject editPhoto ( TlObject channel, TlObject photo) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("photo", photo)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param enabled  (TL Definition : Bool)
         * @return Updates
         */
        public  TlObject toggleComments ( TlObject channel, boolean enabled) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("enabled", enabled)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param username  (TL Definition : string)
         * @return Bool
         */
        public  TlObject checkUsername ( TlObject channel, String username) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("username", username)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param username  (TL Definition : string)
         * @return Bool
         */
        public  TlObject updateUsername ( TlObject channel, String username) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("username", username)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @return Updates
         */
        public  TlObject joinChannel ( TlObject channel) {
            return generate(

                    new Pair("channel", channel)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @return Updates
         */
        public  TlObject leaveChannel ( TlObject channel) {
            return generate(

                    new Pair("channel", channel)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param users  (TL Definition : Vector<InputUser>)
         * @return Updates
         */
        public  TlObject inviteToChannel ( TlObject channel, List<TlObject> users) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("users", users)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param user_id  (TL Definition : InputUser)
         * @param kicked  (TL Definition : Bool)
         * @return Updates
         */
        public  TlObject kickFromChannel (TlObject channel, TlObject user_id, boolean kicked) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("user_id", user_id),
                    new Pair("kicked", kicked)
            );
        }



        /**

         *
         * @param channel  (TL Definition : InputChannel)
         * @return ExportedChatInvite
         */
        public  TlObject exportInvite ( TlObject channel) {
            return generate(

                    new Pair("channel", channel)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @return Updates
         */
        public  TlObject deleteChannel ( TlObject channel) {
            return generate(

                    new Pair("channel", channel)
            );
        }
    }

    /**
     * This file is part of JTel
     * IntelliJ idea.
     * Date     : 6/27/16
     * Package : com.jtel.api
     *
     * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
     */

    public class Contacts extends ApiContext {
        protected Contacts() {
            super();
        }

        /**
         *
         * @return Vector<ContactStatus>
         */
        public  TlObject getStatuses () {
            return generate(

            );
        }

        /**
         *
         * @param hash  (TL Definition : string)
         * @return contacts.Contacts
         */
        public  TlObject getContacts ( String hash) {
            return generate(

                    new Pair("hash", hash)
            );
        }

        /**
         *
         * @param contacts  (TL Definition : Vector<InputContact>)
         * @param replace  (TL Definition : Bool)
         * @return contacts.ImportedContacts
         */
        public  TlObject importContacts ( List<TlObject> contacts, boolean replace) {
            return generate(

                    new Pair("contacts", contacts),
                    new Pair("replace", replace)
            );
        }

        /**
         *
         * @param limit  (TL Definition : int)
         * @return contacts.Suggested
         */
        public  TlObject getSuggested ( int limit) {
            return generate(

                    new Pair("limit", limit)
            );
        }

        /**
         *
         * @param id  (TL Definition : InputUser)
         * @return contacts.Link
         */
        public  TlObject deleteContact ( TlObject id) {
            return generate(

                    new Pair("id", id)
            );
        }

        /**
         *
         * @param id  (TL Definition : Vector<InputUser>)
         * @return Bool
         */
        public  TlObject deleteContacts ( List<TlObject> id) {
            return generate(

                    new Pair("id", id)
            );
        }

        /**
         *
         * @param id  (TL Definition : InputUser)
         * @return Bool
         */
        public  TlObject block ( TlObject id) {
            return generate(

                    new Pair("id", id)
            );
        }

        /**
         *
         * @param id  (TL Definition : InputUser)
         * @return Bool
         */
        public  TlObject unblock ( TlObject id) {
            return generate(

                    new Pair("id", id)
            );
        }

        /**
         *
         * @param offset  (TL Definition : int)
         * @param limit  (TL Definition : int)
         * @return contacts.Blocked
         */
        public  TlObject getBlocked ( int offset, int limit) {
            return generate(

                    new Pair("offset", offset),
                    new Pair("limit", limit)
            );
        }

        /**
         *
         * @return Vector<int>
         */
        public  TlObject exportCard () {
            return generate(

            );
        }

        /**
         *
         * @param export_card  (TL Definition : Vector<int>)
         * @return User
         */
        public  TlObject importCard ( List<Integer> export_card) {
            return generate(

                    new Pair("export_card", export_card)
            );
        }

        /**
         *
         * @param q  (TL Definition : string)
         * @param limit  (TL Definition : int)
         * @return contacts.Found
         */
        public  TlObject search ( String q, int limit) {
            return generate(

                    new Pair("q", q),
                    new Pair("limit", limit)
            );
        }

        /**
         *
         * @param username  (TL Definition : string)
         * @return contacts.ResolvedPeer
         */
        public  TlObject resolveUsername (String username) {
            return generate(

                    new Pair("username", username)
            );
        }
    }

    /**
     * This file is part of JTel
     * IntelliJ idea.
     * Date     : 6/27/16
     * Package : com.jtel.api
     *
     * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
     */

    public class Help extends ApiContext {
        protected Help() {
            super();
        }

        /**
         *
         * @return Config
         */
        public  TlObject getConfig () {
            return generate(

            );
        }

        /**
         *
         * @return NearestDc
         */
        public  TlObject getNearestDc () {
            return generate(

            );
        }

        /**
         *
         * @param device_model  (TL Definition : string)
         * @param system_version  (TL Definition : string)
         * @param app_version  (TL Definition : string)
         * @param lang_code  (TL Definition : string)
         * @return help.AppUpdate
         */
        public  TlObject getAppUpdate ( String device_model, String system_version, String app_version, String lang_code) {
            return generate(

                    new Pair("device_model", device_model),
                    new Pair("system_version", system_version),
                    new Pair("app_version", app_version),
                    new Pair("lang_code", lang_code)
            );
        }

        /**
         *
         * @param events  (TL Definition : Vector<InputAppEvent>)
         * @return Bool
         */
        public  TlObject saveAppLog ( List<TlObject> events) {
            return generate(

                    new Pair("events", events)
            );
        }

        /**
         *
         * @param lang_code  (TL Definition : string)
         * @return help.InviteText
         */
        public  TlObject getInviteText ( String lang_code) {
            return generate(

                    new Pair("lang_code", lang_code)
            );
        }

        /**
         *
         * @return help.Support
         */
        public  TlObject getSupport () {
            return generate(

            );
        }

        /**
         *
         * @param device_model  (TL Definition : string)
         * @param system_version  (TL Definition : string)
         * @param app_version  (TL Definition : string)
         * @param lang_code  (TL Definition : string)
         * @return help.AppChangelog
         */
        public  TlObject getAppChangelog ( String device_model, String system_version, String app_version, String lang_code) {
            return generate(

                    new Pair("device_model", device_model),
                    new Pair("system_version", system_version),
                    new Pair("app_version", app_version),
                    new Pair("lang_code", lang_code)
            );
        }

        /**
         *
         * @param lang_code  (TL Definition : string)
         * @return help.TermsOfService
         */
        public  TlObject getTermsOfService (String lang_code) {
            return generate(

                    new Pair("lang_code", lang_code)
            );
        }
    }

    /**
     * This file is part of JTel
     * IntelliJ idea.
     * Date     : 6/27/16
     * Package : com.jtel.api
     *
     * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
     */

    public class Messages extends ApiContext {
        protected Messages() {
            super();
        }

        /**
         *
         * @param id  (TL Definition : Vector<int>)
         * @return messages.Messages
         */
        public  TlObject getMessages ( List<Integer> id) {
            return generate(

                    new Pair("id", id)
            );
        }

        /**
         *
         * @param offset_date  (TL Definition : int)
         * @param offset_id  (TL Definition : int)
         * @param offset_peer  (TL Definition : InputPeer)
         * @param limit  (TL Definition : int)
         * @return messages.Dialogs
         */
        public  TlObject getDialogs ( int offset_date, int offset_id, TlObject offset_peer, int limit) {
            return generate(

                    new Pair("offset_date", offset_date),
                    new Pair("offset_id", offset_id),
                    new Pair("offset_peer", offset_peer),
                    new Pair("limit", limit)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputPeer)
         * @param offset_id  (TL Definition : int)
         * @param add_offset  (TL Definition : int)
         * @param limit  (TL Definition : int)
         * @param max_id  (TL Definition : int)
         * @param min_id  (TL Definition : int)
         * @return messages.Messages
         */
        public  TlObject getHistory ( TlObject peer, int offset_id, int add_offset, int limit, int max_id, int min_id) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("offset_id", offset_id),
                    new Pair("add_offset", add_offset),
                    new Pair("limit", limit),
                    new Pair("max_id", max_id),
                    new Pair("min_id", min_id)
            );
        }

        ////// TODO: 6/27/16 fix flags
    //    /**
    //     *
    //     * @param flags  (TL Definition : #)
    //     * @param important_only  (TL Definition : flags.0?true)
    //     * @param peer  (TL Definition : InputPeer)
    //     * @param q  (TL Definition : string)
    //     * @param filter  (TL Definition : MessagesFilter)
    //     * @param min_date  (TL Definition : int)
    //     * @param max_date  (TL Definition : int)
    //     * @param offset  (TL Definition : int)
    //     * @param max_id  (TL Definition : int)
    //     * @param limit  (TL Definition : int)
    //     * @return messages.Messages
    //     */
    //    public  TlObject search ( # flags, flags.0?true important_only, TlObject peer, String q, TlObject filter, int min_date, int max_date, int offset, int max_id, int limit) {
    //        return generate(
    //
    //                new Pair("flags", flags),
    //                new Pair("important_only", important_only),
    //                new Pair("peer", peer),
    //                new Pair("q", q),
    //                new Pair("filter", filter),
    //                new Pair("min_date", min_date),
    //                new Pair("max_date", max_date),
    //                new Pair("offset", offset),
    //                new Pair("max_id", max_id),
    //                new Pair("limit", limit)
    //        );
    //    }

        /**
         *
         * @param peer  (TL Definition : InputPeer)
         * @param max_id  (TL Definition : int)
         * @return messages.AffectedMessages
         */
        public  TlObject readHistory ( TlObject peer, int max_id) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("max_id", max_id)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputPeer)
         * @param max_id  (TL Definition : int)
         * @return messages.AffectedHistory
         */
        public  TlObject deleteHistory ( TlObject peer, int max_id) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("max_id", max_id)
            );
        }

        /**
         *
         * @param id  (TL Definition : Vector<int>)
         * @return messages.AffectedMessages
         */
        public  TlObject deleteMessages ( List<Integer> id) {
            return generate(

                    new Pair("id", id)
            );
        }

        /**
         *
         * @param max_id  (TL Definition : int)
         * @return Vector<ReceivedNotifyMessage>
         */
        public  TlObject receivedMessages ( int max_id) {
            return generate(

                    new Pair("max_id", max_id)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputPeer)
         * @param action  (TL Definition : SendMessageAction)
         * @return Bool
         */
        public  TlObject setTyping ( TlObject peer, TlObject action) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("action", action)
            );
        }

        // TODO: 6/27/16 fix flags
    //    /**
    //     *
    //     * @param flags  (TL Definition : #)
    //     * @param no_webpage  (TL Definition : flags.1?true)
    //     * @param broadcast  (TL Definition : flags.4?true)
    //     * @param peer  (TL Definition : InputPeer)
    //     * @param reply_to_msg_id  (TL Definition : flags.0?int)
    //     * @param message  (TL Definition : string)
    //     * @param random_id  (TL Definition : long)
    //     * @param reply_markup  (TL Definition : flags.2?ReplyMarkup)
    //     * @param entities  (TL Definition : flags.3?Vector<MessageEntity>)
    //     * @return Updates
    //     */
    //    public  TlObject sendMessage ( # flags, flags.1?true no_webpage, flags.4?true broadcast, TlObject peer, flags.0?int reply_to_msg_id, String message, long random_id, flags.2?ReplyMarkup reply_markup, flags.3?Vector<MessageEntity> entities) {
    //        return generate(
    //
    //                new Pair("flags", flags),
    //                new Pair("no_webpage", no_webpage),
    //                new Pair("broadcast", broadcast),
    //                new Pair("peer", peer),
    //                new Pair("reply_to_msg_id", reply_to_msg_id),
    //                new Pair("message", message),
    //                new Pair("random_id", random_id),
    //                new Pair("reply_markup", reply_markup),
    //                new Pair("entities", entities)
    //        );
    //    }

        ////// TODO: 6/27/16  fix flags
    //    /**
    //     *
    //     * @param flags  (TL Definition : #)
    //     * @param broadcast  (TL Definition : flags.4?true)
    //     * @param peer  (TL Definition : InputPeer)
    //     * @param reply_to_msg_id  (TL Definition : flags.0?int)
    //     * @param media  (TL Definition : InputMedia)
    //     * @param random_id  (TL Definition : long)
    //     * @param reply_markup  (TL Definition : flags.2?ReplyMarkup)
    //     * @return Updates
    //     */
    //    public  TlObject sendMedia ( # flags, flags.4?true broadcast, TlObject peer, flags.0?int reply_to_msg_id, TlObject media, long random_id, flags.2?ReplyMarkup reply_markup) {
    //        return generate(
    //
    //                new Pair("flags", flags),
    //                new Pair("broadcast", broadcast),
    //                new Pair("peer", peer),
    //                new Pair("reply_to_msg_id", reply_to_msg_id),
    //                new Pair("media", media),
    //                new Pair("random_id", random_id),
    //                new Pair("reply_markup", reply_markup)
    //        );
    //    }

        //// TODO: 6/27/16 fix flags
    //    /**
    //     *
    //     * @param flags  (TL Definition : #)
    //     * @param broadcast  (TL Definition : flags.4?true)
    //     * @param from_peer  (TL Definition : InputPeer)
    //     * @param id  (TL Definition : Vector<int>)
    //     * @param random_id  (TL Definition : Vector<long>)
    //     * @param to_peer  (TL Definition : InputPeer)
    //     * @return Updates
    //     */
    //    public  TlObject forwardMessages ( # flags, flags.4?true broadcast, TlObject from_peer, List<int> id, List<long> random_id, TlObject to_peer) {
    //        return generate(
    //
    //                new Pair("flags", flags),
    //                new Pair("broadcast", broadcast),
    //                new Pair("from_peer", from_peer),
    //                new Pair("id", id),
    //                new Pair("random_id", random_id),
    //                new Pair("to_peer", to_peer)
    //        );
    //    }

        /**
         *
         * @param peer  (TL Definition : InputPeer)
         * @return Bool
         */
        public  TlObject reportSpam ( TlObject peer) {
            return generate(

                    new Pair("peer", peer)
            );
        }

        /**
         *
         * @param id  (TL Definition : Vector<int>)
         * @return messages.Chats
         */
        public  TlObject getChats ( List<Integer> id) {
            return generate(

                    new Pair("id", id)
            );
        }

        /**
         *
         * @param chat_id  (TL Definition : int)
         * @return messages.ChatFull
         */
        public  TlObject getFullChat ( int chat_id) {
            return generate(

                    new Pair("chat_id", chat_id)
            );
        }

        /**
         *
         * @param chat_id  (TL Definition : int)
         * @param title  (TL Definition : string)
         * @return Updates
         */
        public  TlObject editChatTitle ( int chat_id, String title) {
            return generate(

                    new Pair("chat_id", chat_id),
                    new Pair("title", title)
            );
        }

        /**
         *
         * @param chat_id  (TL Definition : int)
         * @param photo  (TL Definition : InputChatPhoto)
         * @return Updates
         */
        public  TlObject editChatPhoto ( int chat_id, TlObject photo) {
            return generate(

                    new Pair("chat_id", chat_id),
                    new Pair("photo", photo)
            );
        }

        /**
         *
         * @param chat_id  (TL Definition : int)
         * @param user_id  (TL Definition : InputUser)
         * @param fwd_limit  (TL Definition : int)
         * @return Updates
         */
        public  TlObject addChatUser ( int chat_id, TlObject user_id, int fwd_limit) {
            return generate(

                    new Pair("chat_id", chat_id),
                    new Pair("user_id", user_id),
                    new Pair("fwd_limit", fwd_limit)
            );
        }

        /**
         *
         * @param chat_id  (TL Definition : int)
         * @param user_id  (TL Definition : InputUser)
         * @return Updates
         */
        public  TlObject deleteChatUser ( int chat_id, TlObject user_id) {
            return generate(

                    new Pair("chat_id", chat_id),
                    new Pair("user_id", user_id)
            );
        }

        /**
         *
         * @param users  (TL Definition : Vector<InputUser>)
         * @param title  (TL Definition : string)
         * @return Updates
         */
        public  TlObject createChat ( List<TlObject> users, String title) {
            return generate(

                    new Pair("users", users),
                    new Pair("title", title)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputPeer)
         * @param id  (TL Definition : int)
         * @param random_id  (TL Definition : long)
         * @return Updates
         */
        public  TlObject forwardMessage ( TlObject peer, int id, long random_id) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("id", id),
                    new Pair("random_id", random_id)
            );
        }

        /**
         *
         * @param contacts  (TL Definition : Vector<InputUser>)
         * @param random_id  (TL Definition : Vector<long>)
         * @param message  (TL Definition : string)
         * @param media  (TL Definition : InputMedia)
         * @return Updates
         */
        public  TlObject sendBroadcast ( List<TlObject> contacts, List<Long> random_id, String message, TlObject media) {
            return generate(

                    new Pair("contacts", contacts),
                    new Pair("random_id", random_id),
                    new Pair("message", message),
                    new Pair("media", media)
            );
        }

        /**
         *
         * @param version  (TL Definition : int)
         * @param random_length  (TL Definition : int)
         * @return messages.DhConfig
         */
        public  TlObject getDhConfig ( int version, int random_length) {
            return generate(

                    new Pair("version", version),
                    new Pair("random_length", random_length)
            );
        }

        /**
         *
         * @param user_id  (TL Definition : InputUser)
         * @param random_id  (TL Definition : int)
         * @param g_a  (TL Definition : bytes)
         * @return EncryptedChat
         */
        public  TlObject requestEncryption ( TlObject user_id, int random_id, byte[] g_a) {
            return generate(

                    new Pair("user_id", user_id),
                    new Pair("random_id", random_id),
                    new Pair("g_a", g_a)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputEncryptedChat)
         * @param g_b  (TL Definition : bytes)
         * @param key_fingerprint  (TL Definition : long)
         * @return EncryptedChat
         */
        public  TlObject acceptEncryption ( TlObject peer, byte[] g_b, long key_fingerprint) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("g_b", g_b),
                    new Pair("key_fingerprint", key_fingerprint)
            );
        }

        /**
         *
         * @param chat_id  (TL Definition : int)
         * @return Bool
         */
        public  TlObject discardEncryption ( int chat_id) {
            return generate(

                    new Pair("chat_id", chat_id)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputEncryptedChat)
         * @param typing  (TL Definition : Bool)
         * @return Bool
         */
        public  TlObject setEncryptedTyping ( TlObject peer, boolean typing) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("typing", typing)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputEncryptedChat)
         * @param max_date  (TL Definition : int)
         * @return Bool
         */
        public  TlObject readEncryptedHistory ( TlObject peer, int max_date) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("max_date", max_date)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputEncryptedChat)
         * @param random_id  (TL Definition : long)
         * @param data  (TL Definition : bytes)
         * @return messages.SentEncryptedMessage
         */
        public  TlObject sendEncrypted ( TlObject peer, long random_id, byte[] data) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("random_id", random_id),
                    new Pair("data", data)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputEncryptedChat)
         * @param random_id  (TL Definition : long)
         * @param data  (TL Definition : bytes)
         * @param file  (TL Definition : InputEncryptedFile)
         * @return messages.SentEncryptedMessage
         */
        public  TlObject sendEncryptedFile ( TlObject peer, long random_id, byte[] data, TlObject file) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("random_id", random_id),
                    new Pair("data", data),
                    new Pair("file", file)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputEncryptedChat)
         * @param random_id  (TL Definition : long)
         * @param data  (TL Definition : bytes)
         * @return messages.SentEncryptedMessage
         */
        public  TlObject sendEncryptedService ( TlObject peer, long random_id, byte[] data) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("random_id", random_id),
                    new Pair("data", data)
            );
        }

        /**
         *
         * @param max_qts  (TL Definition : int)
         * @return Vector<long>
         */
        public  TlObject receivedQueue ( int max_qts) {
            return generate(

                    new Pair("max_qts", max_qts)
            );
        }

        /**
         *
         * @param id  (TL Definition : Vector<int>)
         * @return messages.AffectedMessages
         */
        public  TlObject readMessageContents ( List<Integer> id) {
            return generate(

                    new Pair("id", id)
            );
        }

        /**
         *
         * @param emoticon  (TL Definition : string)
         * @param hash  (TL Definition : string)
         * @return messages.Stickers
         */
        public  TlObject getStickers ( String emoticon, String hash) {
            return generate(

                    new Pair("emoticon", emoticon),
                    new Pair("hash", hash)
            );
        }

        /**
         *
         * @param hash  (TL Definition : int)
         * @return messages.AllStickers
         */
        public  TlObject getAllStickers ( int hash) {
            return generate(

                    new Pair("hash", hash)
            );
        }

        /**
         *
         * @param message  (TL Definition : string)
         * @return MessageMedia
         */
        public  TlObject getWebPagePreview ( String message) {
            return generate(

                    new Pair("message", message)
            );
        }

        /**
         *
         * @param chat_id  (TL Definition : int)
         * @return ExportedChatInvite
         */
        public  TlObject exportChatInvite ( int chat_id) {
            return generate(

                    new Pair("chat_id", chat_id)
            );
        }

        /**
         *
         * @param hash  (TL Definition : string)
         * @return ChatInvite
         */
        public  TlObject checkChatInvite ( String hash) {
            return generate(

                    new Pair("hash", hash)
            );
        }

        /**
         *
         * @param hash  (TL Definition : string)
         * @return Updates
         */
        public  TlObject importChatInvite ( String hash) {
            return generate(

                    new Pair("hash", hash)
            );
        }

        /**
         *
         * @param stickerset  (TL Definition : InputStickerSet)
         * @return messages.StickerSet
         */
        public  TlObject getStickerSet ( TlObject stickerset) {
            return generate(

                    new Pair("stickerset", stickerset)
            );
        }

        /**
         *
         * @param stickerset  (TL Definition : InputStickerSet)
         * @param disabled  (TL Definition : Bool)
         * @return Bool
         */
        public  TlObject installStickerSet ( TlObject stickerset, boolean disabled) {
            return generate(

                    new Pair("stickerset", stickerset),
                    new Pair("disabled", disabled)
            );
        }

        /**
         *
         * @param stickerset  (TL Definition : InputStickerSet)
         * @return Bool
         */
        public  TlObject uninstallStickerSet ( TlObject stickerset) {
            return generate(

                    new Pair("stickerset", stickerset)
            );
        }

        /**
         *
         * @param bot  (TL Definition : InputUser)
         * @param peer  (TL Definition : InputPeer)
         * @param random_id  (TL Definition : long)
         * @param start_param  (TL Definition : string)
         * @return Updates
         */
        public  TlObject startBot ( TlObject bot, TlObject peer, long random_id, String start_param) {
            return generate(

                    new Pair("bot", bot),
                    new Pair("peer", peer),
                    new Pair("random_id", random_id),
                    new Pair("start_param", start_param)
            );
        }

        /**
         *
         * @param peer  (TL Definition : InputPeer)
         * @param id  (TL Definition : Vector<int>)
         * @param increment  (TL Definition : Bool)
         * @return Vector<int>
         */
        public  TlObject getMessagesViews ( TlObject peer, List<Integer> id, boolean increment) {
            return generate(

                    new Pair("peer", peer),
                    new Pair("id", id),
                    new Pair("increment", increment)
            );
        }

        /**
         *
         * @param chat_id  (TL Definition : int)
         * @param enabled  (TL Definition : Bool)
         * @return Updates
         */
        public  TlObject toggleChatAdmins ( int chat_id, boolean enabled) {
            return generate(

                    new Pair("chat_id", chat_id),
                    new Pair("enabled", enabled)
            );
        }

        /**
         *
         * @param chat_id  (TL Definition : int)
         * @param user_id  (TL Definition : InputUser)
         * @param is_admin  (TL Definition : Bool)
         * @return Bool
         */
        public  TlObject editChatAdmin ( int chat_id, TlObject user_id, boolean is_admin) {
            return generate(

                    new Pair("chat_id", chat_id),
                    new Pair("user_id", user_id),
                    new Pair("is_admin", is_admin)
            );
        }

        /**
         *
         * @param chat_id  (TL Definition : int)
         * @return Updates
         */
        public  TlObject migrateChat ( int chat_id) {
            return generate(

                    new Pair("chat_id", chat_id)
            );
        }

        /**
         *
         * @param q  (TL Definition : string)
         * @param offset_date  (TL Definition : int)
         * @param offset_peer  (TL Definition : InputPeer)
         * @param offset_id  (TL Definition : int)
         * @param limit  (TL Definition : int)
         * @return messages.Messages
         */
        public  TlObject searchGlobal ( String q, int offset_date, TlObject offset_peer, int offset_id, int limit) {
            return generate(

                    new Pair("q", q),
                    new Pair("offset_date", offset_date),
                    new Pair("offset_peer", offset_peer),
                    new Pair("offset_id", offset_id),
                    new Pair("limit", limit)
            );
        }

        /**
         *
         * @param order  (TL Definition : Vector<long>)
         * @return Bool
         */
        public  TlObject reorderStickerSets ( List<Long> order) {
            return generate(

                    new Pair("order", order)
            );
        }

        /**
         *
         * @param sha256  (TL Definition : bytes)
         * @param size  (TL Definition : int)
         * @param mime_type  (TL Definition : string)
         * @return Document
         */
        public  TlObject getDocumentByHash ( byte[] sha256, int size, String mime_type) {
            return generate(

                    new Pair("sha256", sha256),
                    new Pair("size", size),
                    new Pair("mime_type", mime_type)
            );
        }

        /**
         *
         * @param q  (TL Definition : string)
         * @param offset  (TL Definition : int)
         * @return messages.FoundGifs
         */
        public  TlObject searchGifs ( String q, int offset) {
            return generate(

                    new Pair("q", q),
                    new Pair("offset", offset)
            );
        }

        /**
         *
         * @param hash  (TL Definition : int)
         * @return messages.SavedGifs
         */
        public  TlObject getSavedGifs ( int hash) {
            return generate(

                    new Pair("hash", hash)
            );
        }

        /**
         *
         * @param id  (TL Definition : InputDocument)
         * @param unsave  (TL Definition : Bool)
         * @return Bool
         */
        public  TlObject saveGif ( TlObject id, boolean unsave) {
            return generate(

                    new Pair("id", id),
                    new Pair("unsave", unsave)
            );
        }

        /**
         *
         * @param bot  (TL Definition : InputUser)
         * @param query  (TL Definition : string)
         * @param offset  (TL Definition : string)
         * @return messages.BotResults
         */
        public  TlObject getInlineBotResults ( TlObject bot, String query, String offset) {
            return generate(

                    new Pair("bot", bot),
                    new Pair("query", query),
                    new Pair("offset", offset)
            );
        }

    //    /**
    //     *
    //     * @param flags  (TL Definition : #)
    //     * @param gallery  (TL Definition : flags.0?true)
    //     * @param private  (TL Definition : flags.1?true)
    //     * @param query_id  (TL Definition : long)
    //     * @param results  (TL Definition : Vector<InputBotInlineResult>)
    //     * @param cache_time  (TL Definition : int)
    //     * @param next_offset  (TL Definition : flags.2?string)
    //     * @return Bool
    //     */
    //    public  TlObject setInlineBotResults ( # flags, flags.0?true gallery, flags.1?true private, long query_id, List<TlObject> results, int cache_time, flags.2?string next_offset) {
    //        return generate(
    //
    //                new Pair("flags", flags),
    //                new Pair("gallery", gallery),
    //                new Pair("private", private),
    //        new Pair("query_id", query_id),
    //                new Pair("results", results),
    //                new Pair("cache_time", cache_time),
    //                new Pair("next_offset", next_offset)
    //        );
    //    }

        /**
         *
         * @param flags  (TL Definition : #)
         * @param broadcast  (TL Definition : flags.4?true)
         * @param peer  (TL Definition : InputPeer)
         * @param reply_to_msg_id  (TL Definition : flags.0?int)
         * @param random_id  (TL Definition : long)
         * @param query_id  (TL Definition : long)
         * @param id  (TL Definition : string)
         * @return Updates
         */
        //// TODO: 6/27/16 fix optinal flags on methods
    //    public  TlObject sendInlineBotResult (#flags, flags.4?true broadcast, TlObject peer, flags.0?int reply_to_msg_id, long random_id, long query_id, String id) {
    //        return generate(
    //
    //                new Pair("flags", flags),
    //                new Pair("broadcast", broadcast),
    //                new Pair("peer", peer),
    //                new Pair("reply_to_msg_id", reply_to_msg_id),
    //                new Pair("random_id", random_id),
    //                new Pair("query_id", query_id),
    //                new Pair("id", id)
    //        );
    //    }
    }


    /**
     * This file is part of JTel
     * IntelliJ idea.
     * Date     : 6/27/16
     * Package : com.jtel.api
     *
     * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
     */

    public class Updates extends ApiContext {
        protected Updates() {
            super();
        }

        /**
         *
         * @return updates.State
         */
        public  TlObject getState () {
            return generate(

            );
        }

        /**
         *
         * @param pts  (TL Definition : int)
         * @param date  (TL Definition : int)
         * @param qts  (TL Definition : int)
         * @return updates.Difference
         */
        public  TlObject getDifference ( int pts, int date, int qts) {
            return generate(

                    new Pair("pts", pts),
                    new Pair("date", date),
                    new Pair("qts", qts)
            );
        }

        /**
         *
         * @param channel  (TL Definition : InputChannel)
         * @param filter  (TL Definition : ChannelMessagesFilter)
         * @param pts  (TL Definition : int)
         * @param limit  (TL Definition : int)
         * @return updates.ChannelDifference
         */
        public  TlObject getChannelDifference (TlObject channel, TlObject filter, int pts, int limit) {
            return generate(

                    new Pair("channel", channel),
                    new Pair("filter", filter),
                    new Pair("pts", pts),
                    new Pair("limit", limit)
            );
        }



    }


    /**
     * This file is part of JTel
     * IntelliJ idea.
     * Date     : 6/27/16
     * Package : com.jtel.api
     *
     * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
     */

    public class Upload extends ApiContext {

        /**
         *
         * @param file_id  (TL Definition : long)
         * @param file_part  (TL Definition : int)
         * @param bytes  (TL Definition : bytes)
         * @return Bool
         */
        public  TlObject saveFilePart ( long file_id, int file_part, byte[] bytes) {
            return generate(

                    new Pair("file_id", file_id),
                    new Pair("file_part", file_part),
                    new Pair("bytes", bytes)
            );
        }

        protected Upload() {
            super();
        }

        /**
         *
         * @param location  (TL Definition : InputFileLocation)
         * @param offset  (TL Definition : int)
         * @param limit  (TL Definition : int)
         * @return upload.File
         */
        public  TlObject getFile ( TlObject location, int offset, int limit) {
            return generate(

                    new Pair("location", location),
                    new Pair("offset", offset),
                    new Pair("limit", limit)
            );
        }

        /**
         *
         * @param file_id  (TL Definition : long)
         * @param file_part  (TL Definition : int)
         * @param file_total_parts  (TL Definition : int)
         * @param bytes  (TL Definition : bytes)
         * @return Bool
         */
        public  TlObject saveBigFilePart (long file_id, int file_part, int file_total_parts, byte[] bytes) {
            return generate(

                    new Pair("file_id", file_id),
                    new Pair("file_part", file_part),
                    new Pair("file_total_parts", file_total_parts),
                    new Pair("bytes", bytes)
            );
        }
    }

    /**
     * This file is part of JTel
     * IntelliJ idea.
     * Date     : 6/27/16
     * Package : com.jtel.api
     *
     * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
     */

    public   class Users extends ApiContext {

        protected Users() {
            super();
        }

        /**

         *
         * @param id  (TL Definition : Vector<InputUser>)
         * @return Vector<User>
         */
        public  TlObject getUsers (List<TlObject> id) {
            return generate(

                    new Pair("id", id)
            );
        }

        /**
         *
         * @param id  (TL Definition : InputUser)
         * @return UserFull
         */
        public  TlObject getFullUser ( TlObject id) {
            return generate(

                    new Pair("id", id)
            );
        }
    }
}
