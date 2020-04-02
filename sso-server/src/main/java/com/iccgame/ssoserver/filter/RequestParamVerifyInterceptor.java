package com.iccgame.ssoserver.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

//@Component
public class RequestParamVerifyInterceptor extends HandlerInterceptorAdapter {
    //这里是对springmvc中路径传参的加密，约定路径传参，在请求头加上(pathvariable: 路径)
    //在参数加密时也一起加密
    private static final String PATH_VARIABLE = "pathvariable";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();
            Iterator<String> iterator = parameterMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String[] valueArr = parameterMap.get(key);
                //这里约定参数不重复
                params.put(key, valueArr[0]);
            }
            String pathvariable = request.getHeader(PATH_VARIABLE);
            StringBuffer requestURL = request.getRequestURL();
            if(pathvariable != null) {
                if(!requestURL.toString().endsWith(pathvariable)) {
                    throw new Exception("error:路径传参错误!");
                }
                if(params.containsKey(PATH_VARIABLE))
                    params.put(PATH_VARIABLE, pathvariable);
            }

            if (request.getHeader("sign") == null || request.getHeader("timestamp") == null) {
                throw new Exception("error:参数错误，缺少必要参数!");
            }
            long timestamp = Long.parseLong(request.getHeader("timestamp"));
            long now = System.currentTimeMillis();
            long diffMillis = now - timestamp;
            /// 30秒
            if ( diffMillis >= 30*1000*100 ) {
                throw new Exception("error:请求失效!");
            }
            Set<String> keysSet = params.keySet();
            Object[] keys = keysSet.toArray();
            Arrays.sort(keys);
            StringBuffer temp = new StringBuffer();
            for (Object key : keys) {
                if(key.equals("sign") || key.equals("timestamp")){
                    continue;
                }

                temp.append(key).append("=");
                Object value = params.get(key);
                String valueString = "";
                if (null != value) {
                    valueString = String.valueOf(value).toLowerCase();
                }
                temp.append(valueString);
                temp.append("&");
            }
            String sign = request.getHeader("sign");
            String signString = temp.toString()+"timestamp="+timestamp+"&";
            String signedString = RequestCryptoHelper.crypto(signString);
            boolean result = sign.equals(signedString) ? true : false;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}

