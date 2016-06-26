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

import com.jtel.mtproto.tl.TlObject;

import java.util.ArrayList;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/25/16
 * Package : com.jtel.api
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public  class Auth extends ApiContext {

    /**
     *  check if phone_number registered?
     * @param phone_number phone number with country code without '+'
     * @return checkedPhone
     */
    public static TlObject checkPhone(String phone_number){
        return generate(new Pair("phone_number",phone_number));
    }

    /**
     * send sms or message to start authenticating user
     * @param phone_number user phone number (without + )
     * @param sms_type  1- send sms, 2- send message to telegram account
     * @param lang_code en, fa ...
     * @return sentCode
     */
    public static TlObject sendCode(String phone_number, int sms_type, String lang_code){
        return generate(
                new Pair("phone_number" ,phone_number),
                new Pair("sms_type"     , sms_type),
                new Pair("lang_code"    ,lang_code),
                new Pair("api_id"       ,api_id),
                new Pair("api_hash"     ,api_hash)
        );
    }

    /**
     * sign in user
     * @param phone_number the number that you have sent the code
     * @param phone_code_hash the hash that telegram sends when you send code to a user
     * @param phone_code the code that user has received from telegram
     * @return auth.Authorization
     */
    public  static TlObject signIn(String phone_number,String phone_code_hash,String phone_code){
        return generate(
                new Pair("phone_number",phone_number),
                new Pair("phone_code_hash",phone_code_hash),
                new Pair("phone_code",phone_code)
        );
    }


    /**
     * send sms to user
     * @param phone_number user phone number
     * @param phone_code_hash use phone code hash
     * @return sentSms
     */
    public static TlObject sendSms(String phone_number,String phone_code_hash){
        return generate(
                new Pair("phone_number",phone_number),
                new Pair("phone_code_hash",phone_code_hash)
        );
    }

    /**
     * send sms to user
     * @param phone_number user phone number
     * @param phone_code_hash use phone code hash
     * @return sentCall
     */
    public static TlObject sendCall(String phone_number,String phone_code_hash) {

        return generate(
                new Pair("phone_number",phone_number),
                new Pair("phone_code_hash",phone_code_hash)
        );
    }



}
