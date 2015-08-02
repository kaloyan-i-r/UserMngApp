package org.demo.userapp.error;

public class UserMngAppExceptionMessage extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String dsdMsg;

    public UserMngAppExceptionMessage(String msg) {
        this.dsdMsg = msg;
    }

    public String getDsdMsg() {
        return dsdMsg;
    }

}
