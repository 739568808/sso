package com.iccgame.ssoclient3.controller;

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
}
