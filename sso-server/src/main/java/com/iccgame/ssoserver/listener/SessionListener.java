package com.iccgame.ssoserver.listener;

import com.alibaba.fastjson.JSON;
import com.iccgame.ssoserver.enums.EToken;
import com.iccgame.ssoserver.util.MockDatabaseUtil;
import com.iccgame.ssoserver.util.RedisUtils;
import com.iccgame.ssoserver.vo.ClientInfoVo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.util.List;
@Component
public class SessionListener implements HttpSessionListener {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("触发了session销毁事件......");
        HttpSession session = se.getSession();
        String token = (String)session.getAttribute("token");
        System.out.println("logOut token:"+token);
        //删除t_token表中的数据
        String tokenKey = redisUtils.getSSOKey(EToken.TOKEN.getName(), token);
        String tokenClientInfoKey = redisUtils.getSSOKey(EToken.TOKEN_CLIENT_INFO.getName(), token);

        //MockDatabaseUtil.T_TOKEN.remove(token);
        //List<ClientInfoVo> infoVoList = MockDatabaseUtil.T_CLIENT_INFO.remove(token);
        String tokenClientInfoStr = redisUtils.get(tokenClientInfoKey);
        List<ClientInfoVo> clientInfoList = JSON.parseArray(tokenClientInfoStr, ClientInfoVo.class);
        if (!CollectionUtils.isEmpty(clientInfoList)){
            //获取出注册的子系统，依次调用子系统的登出方法
            for (ClientInfoVo vo:clientInfoList){
                try {
                    sendHttpRequset(vo.getLogOutUrl(),vo.getSessionid(),vo.getSessionType());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        redisUtils.del(tokenKey);
        redisUtils.del(tokenClientInfoKey);
    }

    private void sendHttpRequset(String url,String sessionid,String sessionType) {
        try {
            Connection.Response response = Jsoup.connect(url)
                    //.header("Cookie", "JSESSIONID=" + jsessionid)
                    .header("Cookie", sessionType+"=" + sessionid)
                    .method(Connection.Method.POST).execute();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
