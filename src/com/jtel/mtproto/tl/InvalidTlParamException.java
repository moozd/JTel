package com.jtel.mtproto.tl;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/10/16
 * Package : com.jtel.mtproto.tl
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class InvalidTlParamException extends Exception {

    public InvalidTlParamException(TlParam param){
        super(String.format("%s : Invalid type %s .expecting %s",param.name,param.getValue().getClass().getTypeName(),param.type));
    }

    public InvalidTlParamException(TlParam param, String givenType){
        super(String.format("%s : Invalid type %s .expecting %s",param.name,givenType,param.type));
    }

    public InvalidTlParamException(String param,String expectedType, String givenType){
        super(String.format("%s : Invalid type %s .expecting %s",param,givenType,expectedType));
    }
}
