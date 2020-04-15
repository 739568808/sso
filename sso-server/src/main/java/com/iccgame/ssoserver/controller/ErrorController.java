package com.iccgame.ssoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class ErrorController {
    @RequestMapping("/404")
    public String error404(){
        return "404";
    }
}
