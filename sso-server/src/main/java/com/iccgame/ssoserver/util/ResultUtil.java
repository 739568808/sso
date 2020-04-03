package com.iccgame.ssoserver.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ResultUtil {

    /**
     * * 请求成功返回
     * @param object
     * @return
     */
    public static  String success(Object object){
        Result result=new Result();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(object);
        return JSONObject.toJSONString(result,SerializerFeature.WriteMapNullValue);
    }
    public static String success(){
        return success(null);
    }

    public static  String error(Integer code,String msg){
        Result result=new Result();
        result.setCode(code);
        result.setMsg(msg);
        return JSONObject.toJSONString(result,SerializerFeature.WriteMapNullValue);
    }

    public static  String error(String msg){
        Result result=new Result();
        result.setCode(0);
        result.setMsg(msg);
        return JSONObject.toJSONString(result,SerializerFeature.WriteMapNullValue);
    }
}
