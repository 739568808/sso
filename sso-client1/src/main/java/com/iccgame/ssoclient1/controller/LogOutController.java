package com.iccgame.ssoclient1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LogOutController {

    @RequestMapping("/logOut")
    public void logOut(HttpSession session){
        //销毁全局会话
        session.invalidate();
    }
    @RequestMapping("/login")
    public String login(HttpSession session){
        //销毁全局会话
        return "login";
    }
}
