package com.iccgame.ssoserver.listener;

import com.iccgame.ssoserver.util.MockDatabaseUtil;
import com.iccgame.ssoserver.vo.ClientInfoVo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.util.List;
@Component
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("触发了session销毁事件......");
        HttpSession session = se.getSession();
        String token = (String)session.getAttribute("token");
        //删除t_token表中的数据
        MockDatabaseUtil.T_TOKEN.remove(token);
        List<ClientInfoVo> infoVoList = MockDatabaseUtil.T_CLIENT_INFO.remove(token);
        //获取出注册的子系统，依次调用子系统的登出方法
        for (ClientInfoVo vo:infoVoList){
            try {
                sendHttpRequset(vo.getClientUrl(),vo.getJsessionid());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void sendHttpRequset(String url,String jsessionid) throws IOException {
        Jsoup.connect(url)
                .header("Cookie","JSESSIONID="+jsessionid)
                .method(Connection.Method.POST).execute();
    }
}
