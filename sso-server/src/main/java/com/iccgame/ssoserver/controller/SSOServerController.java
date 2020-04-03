package com.iccgame.ssoserver.controller;

import com.iccgame.ssoserver.util.MockDatabaseUtil;
import com.iccgame.ssoserver.util.ResultUtil;
import com.iccgame.ssoserver.vo.ClientInfoVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
public class SSOServerController {
    @Value("${white_list}")
    private String whiteList;
    @RequestMapping("/checkLogin")
    public String checklogin(String redirectUrl, HttpSession session,RedirectAttributes redirectAttributes){

        //白名单校验
        if (!whiteListVerify(redirectUrl)){
            return "404";
        }

        //1、判断是否有全局的会话
        String token = (String) session.getAttribute("token");
        if (StringUtils.isEmpty(token)){
            //没有全局会话
            //跳转到统一认证中心的登录界面
            //redirectAttributes.addAttribute("redirectUrl",redirectUrl);
            //return "login";
            return "redirect:"+redirectUrl+"login";
        } else {
            //有全局会话
            //取出令牌信息，重定向到redirectUrl,把token带上
            redirectAttributes.addAttribute("token",token);
            return "redirect:"+redirectUrl;
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
    public String login(String username, String password, String redirectUrl, HttpSession session, RedirectAttributes redirectAttributes){
        if ("admin".equals(username) && "123456".equals(password)){
            //登录验证成功
            //1、创建令牌信息
            String token = UUID.randomUUID().toString();
            //2、创建全局会话，将令牌放入会话中
            session.setAttribute("token",token);
            //TODO 3、将令牌信息放入数据库中（redis中）
            MockDatabaseUtil.addToken(token);
            //4、重定向到redirectUrl，并且把令牌信息带上
            redirectAttributes.addAttribute("token",token);
            redirectAttributes.addAttribute("username",username);
            return "redirect:"+redirectUrl;
        }
        //登录失败
        //redirectAttributes.addAttribute("redirectUrl",redirectUrl);

        //return "login";
        return "redirect:"+redirectUrl;
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verifyToken(String token,String clientUrl,String jsessionid){
        if (MockDatabaseUtil.T_TOKEN.contains(token)){
            //把客户端的登出地址记录起来
            List<ClientInfoVo> clientInfoList = MockDatabaseUtil.T_CLIENT_INFO.get(token);
            if (clientInfoList==null){
                clientInfoList = new ArrayList<>();
                //TODO 数据库或者缓存
                MockDatabaseUtil.T_CLIENT_INFO.put(token,clientInfoList);
            }
            ClientInfoVo vo = new ClientInfoVo();
            vo.setClientUrl(clientUrl);
            vo.setJsessionid(jsessionid);
            clientInfoList.add(vo);

            return "true";
        }
        return "false";
    }

    @RequestMapping("/logOut")
    public String logOut(String redirectUrl,HttpSession session){
        //销毁全局会话
        session.invalidate();
        return "redirect:"+redirectUrl;
    }
    @RequestMapping("/userInfo")
    @ResponseBody
    public String user(HttpSession session){
        String token = (String) session.getAttribute("token");
        if (StringUtils.isEmpty(token)){
            return ResultUtil.error("Token Can not be empty");
        }
        List<ClientInfoVo> clientInfoList = MockDatabaseUtil.T_CLIENT_INFO.get(token);
        if (clientInfoList == null){
            return ResultUtil.error("invalid Token");
        }
        return ResultUtil.success(clientInfoList);
    }
}
