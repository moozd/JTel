package com.jtel.mtproto.tl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/7/16
 * Package : com.jtel.mtproto.tl
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */


/**
 * We are using Tl interface to encapsulate conversation of tl language raw data (bytes)
 * to java representation of it and vice versa. TlObject is java representation of tl
 * language actual object
 *
 */
public interface Tl {
    /**
     * converts TlObject data to tl language byte array.
     * @return converted result in byte array
     * @throws IOException when tl schema files such as mtp-schema.json or api-schema.json are missing.
     * @throws InvalidTlParamException when expected type and given type dose not match.
     *         ex) let`s say tl schema expected value format for object due to tl-schema
     *             is long but given type to value of that field is int.
     */
    byte[] serialize() throws IOException,InvalidTlParamException;

    /**
     * to convert InputStream like network InputStream to TlObject we will use this method
     * @param is InputStream if you have byte array convert it to ByteArrayInputStream and pass it throw method
     *           if you have Socket InputStream just pass Socket.getInputStream to it
     * @throws IOException when a socket or HttpUrlConnection or any other network socket impel
     *          cannot connect to given data center.
     */
    void  deSerialize(InputStream is) throws IOException;
}
