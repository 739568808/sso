package com.iccgame.ssoserver.util;

import com.iccgame.ssoserver.vo.ClientInfoVo;

import java.util.*;

public class MockDatabaseUtil {
    public static Set<String> T_TOKEN = new HashSet<String>();
    public static Map<String, List<ClientInfoVo>> T_CLIENT_INFO = new HashMap<>();

    public static void addToken(String token){
        T_TOKEN.add(token);
    }
    public static void getToken(String token){
        T_TOKEN.add(token);
    }

}
