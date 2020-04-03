package com.iccgame.ssoserver.vo;


public class ClientInfoVo  {
    private String clientUrl;
    private String jsessionid;
    private User user;

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }
}
