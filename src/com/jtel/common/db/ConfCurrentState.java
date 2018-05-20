package com.jtel.common.db;

/**
 * This file is part of jTelClient
 * IntelliJ idea.
 * Date     : 7/23/2016
 * Package : com.jtel.common.db
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class ConfCurrentState {

    String sessionName;
    int dcId;
    byte[] sessionId;
    boolean authenticationRequired;
    boolean signInRequired;

    public ConfCurrentState(String sessionName, int dcId, byte[] sessionId, boolean authenticationRequired, boolean signInRequired) {
        this.sessionName = sessionName;
        this.dcId = dcId;
        this.sessionId = sessionId;
        this.authenticationRequired = authenticationRequired;
        this.signInRequired = signInRequired;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getDcId() {
        return dcId;
    }

    public void setDcId(int dcId) {
        this.dcId = dcId;
    }

    public byte[] getSessionId() {
        return sessionId;
    }

    public void setSessionId(byte[] sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isAuthenticationRequired() {
        return authenticationRequired;
    }

    public void setAuthenticationRequired(boolean authenticationRequired) {
        this.authenticationRequired = authenticationRequired;
    }

    public boolean isSignInRequired() {
        return signInRequired;
    }

    public void setSignInRequired(boolean signInRequired) {
        this.signInRequired = signInRequired;
    }

}
