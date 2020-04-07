package com.iccgame.ssoserver.enums;

public enum EToken {




	TOKEN_CLIENT_INFO("SSO:CLIENT:INFO",1),












	//--------------------------------分割线此以下禁止写代码-------------------------------------------------
	TOKEN("SSO:TOKEN", 0);
    // 成员变量
    private String name;
    private int code;
    // 构造方法
    private EToken(String name, int code) {
        this.name = name;  
        this.code = code;  
    }  
    // 普通方法  
    public static String getName(int code) {  
        for (EToken c : EToken.values()) {
            if (c.getCode() == code) {  
                return c.name;  
            }  
        }  
        return null;  
    }  
    // get set 方法  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public int getCode() {  
        return code;  
    }  
    public void setCode(int code) {  
        this.code = code;  
    }  
}
