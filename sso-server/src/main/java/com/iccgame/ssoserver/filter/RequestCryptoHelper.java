package com.iccgame.ssoserver.filter;

import java.security.MessageDigest;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

public class RequestCryptoHelper {
    //密钥
    private static String signKeys = "abcdefg";

    public static String sha1(String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(value.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 这里对请求参数字符串进行处理，去掉所有的空格，和双引号
     * 以防止不同系统对于参数处理不同
     */
    public static String crypto(String signString){
        signString = signString + "signKeys="+signKeys;
        signString = signString.replaceAll("\"", "")
                .replaceAll(" ", "");
        return RequestCryptoHelper.sha1(signString.toLowerCase());
    }

    public static void main(String[] args) {
        String param = "password=123456&username=admin&timestamp=1585817037106&";
        String crypto = crypto(param);
        System.out.println(crypto);

    }
}

