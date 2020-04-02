package com.iccgame.ssoserver.util;

import java.util.*;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 在每次请求接口时需根据参数、apikey、token临时构建出signature签名，最大程度保障接口安全性，构建规则如下；
 * 对所有参数(除signature外)按照字段名的ASCII 码从小到大排序(字典排序)后，使用URL键值对的格式（即key1=value1&key2=value2… + token=xxx）拼接成字符串，最后再进行md5加密。字段名和字段值都采用原始值，请勿行URL转义。
 * 举例说明:
 * 接口A请求参数为： name、age、address
 */
public class SignUtil {

    public static void main(String[] args) {
        //sign();
        SortedMap<String, String> params = new TreeMap<String, String>();
        params.put("name", "xiaoming");
        params.put("age", "20");
        sign(params);

    }


    public static String md5(String res){
        //加密后的字符串
        return DigestUtils.md5Hex(res);
    }

    public static String sign(SortedMap<String,String> map){
        StringBuffer sb = new StringBuffer();
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            //空值不传递，不参与签名组串
            if (null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("secretKey=SASDADSDDDFFFDGHGJHGFDVV");

        System.out.println("加密前："+sb.toString());
        System.out.println("加密后："+md5(sb.toString()));
        return md5(sb.toString());
    }
}