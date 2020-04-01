package com.iccgame.ssoserver.controller;

import com.iccgame.ssoserver.util.MockDatabaseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.UUID;

@Controller
public class SSOServerController {
    @Value("${white_list}")
    private String whiteList;
    @RequestMapping("/checkLogin")
    public String checklogin(String redirectUrl, HttpSession session, Model model){

        //白名单校验
        if (!whiteListVerify(redirectUrl)){
            return "404";
        }

        //1、判断是否有全局的会话
        String token = (String) session.getAttribute("token");
        if (StringUtils.isEmpty(token)){
            //没有全局会话
            //跳转到统一认证中心的登录界面
            model.addAttribute("redirectUrl",redirectUrl);
            return "login";
        } else {
            //有全局会话
            //取出令牌信息，重定向到redirectUrl,把token带上
            model.addAttribute("token",token);
            return "redirect:"+redirectUrl+"?token="+token;
        }
    }

    private boolean whiteListVerify(String redirectUrl){
        String[] whiteArr = whiteList.split("\\|");
        boolean isWhite = false;
        for (String addr:whiteArr){
            if (redirectUrl.contains(addr)){
                isWhite = true;
                break;
            }
        }
        return isWhite;
    }


    /**
     * 登录
     */
    @RequestMapping("/login")
    public String login(String username,String password,String redirectUrl,HttpSession session,Model model){
        if ("admin".equals(username) && "123456".equals(password)){
            //登录验证成功
            //1、创建令牌信息
            String token = UUID.randomUUID().toString();
            //2、创建全局会话，将令牌放入会话中
            session.setAttribute("token",token);
            //3、将令牌信息放入数据库中（redis中）
            MockDatabaseUtil.addToken(token);
            //4、重定向到redirectUrl，并且把令牌信息带上
            model.addAttribute("token",token);
            return "redirect:"+redirectUrl+"?token="+token;
        }
        //登录失败
        model.addAttribute("redirectUrl",redirectUrl);
        return "login";
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verifyToken(String token){
        if (MockDatabaseUtil.T_TOKEN.contains(token)){
            return "true";
        }
        return "false";
    }
}
