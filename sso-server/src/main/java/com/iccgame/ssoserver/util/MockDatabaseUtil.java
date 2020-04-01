package com.iccgame.ssoserver.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MockDatabaseUtil {
    public static Set<String> T_TOKEN = new HashSet<String>();
    public static void addToken(String token){
        T_TOKEN.add(token);
    }
    public static void getToken(String token){
        T_TOKEN.add(token);
    }
}
