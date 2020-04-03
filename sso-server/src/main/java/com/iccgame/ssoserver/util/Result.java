package com.iccgame.ssoserver.util;

import java.io.Serializable;

/**
 * lhy
 * @param <T>
 */
public class Result<T> implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -6668726991155820382L;
    private int code;
    private String msg;
    /*具体内容*/
    private  T data;

    public Result() {
        super();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}