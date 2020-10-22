package org.acme.resteasy.dto;

import java.io.Serializable;

public class MessageTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String userName;
    private String msg;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
