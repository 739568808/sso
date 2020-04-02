package com.iccgame.ssoserver.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 在每次请求接口时需根据参数、apikey、token临时构建出signature签名，最大程度保障接口安全性，构建规则如下；
 * 对所有参数(除signature外)按照字段名的ASCII 码从小到大排序(字典排序)后，使用URL键值对的格式（即key1=value1&key2=value2… + token=xxx）拼接成字符串，最后再进行md5加密。字段名和字段值都采用原始值，请勿行URL转义。
 * 举例说明:
 * 接口A请求参数为： name、age、address
 */
public class SignatureTest {

    public static void main(String[] args) {

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("name", "xiaoming");
        params.put("age", "20");

        Set<String> keySet = params.keySet();
        Iterator<String> iter = keySet.iterator();

        String kv = "";
        while (iter.hasNext()) {
            String key = iter.next();
            kv += key+"="+params.get(key)+"&";
        }

        kv += "secretKey=SASDADSDDDFFFDGHGJHGFDVV";


        System.out.println("加密前："+kv);
        System.out.println("加密后："+md5(kv));
    }


    public static String md5(String res){
        //加密后的字符串
        return DigestUtils.md5Hex(res);
    }
}