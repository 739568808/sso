package com.iccgame.ssoserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iccgame.ssoserver.enums.EToken;
import com.iccgame.ssoserver.util.RedisUtils;
import com.iccgame.ssoserver.util.ResultUtil;
import com.iccgame.ssoserver.vo.ClientInfoVo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class SSOServerController {
    @Value("${white_list}")
    private String whiteList;
    @Value("${timeout}")
    private String timeout;

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("/checkLogin")
    public String checklogin(String redirectUrl, HttpSession session,RedirectAttributes redirectAttributes){

        //白名单校验
        if (!whiteListVerify(redirectUrl)){
            return "redirect:404";
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
            if (redirectUrl.startsWith(addr)){
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
    public String login(String username, String password,String gameid, String redirectUrl, HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request){
        //如果回调地址是空，则取referer
        redirectUrl = StringUtils.isEmpty(redirectUrl)?request.getHeader("referer"):redirectUrl;

        //TODO 查询数据库用户信息
        //this.getUserInfo(username,password,gameid);
        if ("admin".equals(username) && "123456".equals(password)){
            //登录验证成功
            //1、创建令牌信息
            String token = UUID.randomUUID().toString();
            //2、创建全局会话，将令牌放入会话中
            //System.out.println("login token:"+token);
            session.setAttribute("token",token);

            //3、将令牌信息放入数据库中（redis中）
            String key = redisUtils.getSSOKey(EToken.TOKEN.getName(), token);
            redisUtils.set(key,token,Long.valueOf(timeout), TimeUnit.DAYS);
            //MockDatabaseUtil.T_TOKEN.add(token);
            //4、重定向到redirectUrl，并且把令牌信息带上
            redirectAttributes.addAttribute("token",token);
            //redirectAttributes.addAttribute("username",username);
            return "redirect:"+redirectUrl;
        }
        //登录失败
        //redirectAttributes.addAttribute("redirectUrl",redirectUrl);

        //return "login";
        redirectAttributes.addAttribute("errMsg","Wrong account or password");
        String referer = request.getHeader("referer");
        return "redirect:"+redirectUrl;
    }
    @RequestMapping("/login2")
    @ResponseBody
    public String login2(String username, String password,String gameid, String redirectUrl, HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request){
        //如果回调地址是空，则取referer
        redirectUrl = StringUtils.isEmpty(redirectUrl)?request.getHeader("referer"):redirectUrl;

        //TODO 查询数据库用户信息
        //this.getUserInfo(username,password,gameid);
        if ("admin".equals(username) && "123456".equals(password)){
            //登录验证成功
            //1、创建令牌信息
            String token = UUID.randomUUID().toString();
            //2、创建全局会话，将令牌放入会话中
            //System.out.println("login token:"+token);
            session.setAttribute("token",token);
            //3、将令牌信息放入数据库中（redis中）
            String key = redisUtils.getSSOKey(EToken.TOKEN.getName(), token);
            redisUtils.set(key,token,Long.valueOf(timeout), TimeUnit.DAYS);
            //MockDatabaseUtil.T_TOKEN.add(token);
            //4、重定向到redirectUrl，并且把令牌信息带上
            redirectAttributes.addAttribute("token",token);
            //redirectAttributes.addAttribute("username",username);
            return ResultUtil.success(token);
        }
        //登录失败
        //redirectAttributes.addAttribute("redirectUrl",redirectUrl);

        //return "login";


        return ResultUtil.error("账户名或密码错误");
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verifyToken(String token,String logOutUrl,String sessionType,String sessionid){
        String tokenKey = redisUtils.getSSOKey(EToken.TOKEN.getName(), token);
        String tokenClientInfoKey = redisUtils.getSSOKey(EToken.TOKEN_CLIENT_INFO.getName(), token);
        if (redisUtils.hasKey(tokenKey)){
            //把客户端的登出地址记录起来
            String tokenClientInfoStr = redisUtils.get(tokenClientInfoKey);
            List<ClientInfoVo> clientInfoList = JSON.parseArray(tokenClientInfoStr, ClientInfoVo.class);
//            List<ClientInfoVo> clientInfoList = MockDatabaseUtil.T_CLIENT_INFO.get(token);
//            if (clientInfoList==null){
//                clientInfoList = new ArrayList<>();
//                //数据库或者缓存
//                MockDatabaseUtil.T_CLIENT_INFO.put(token,clientInfoList);
//            }
            if (CollectionUtils.isEmpty(clientInfoList)){
                clientInfoList = new ArrayList<ClientInfoVo>();
            }
            ClientInfoVo vo = new ClientInfoVo();
            vo.setLogOutUrl(logOutUrl);
            vo.setSessionid(sessionid);
            vo.setSessionType(sessionType);
            clientInfoList.add(vo);
            boolean boo = redisUtils.set(tokenClientInfoKey,JSON.toJSONString(clientInfoList),Long.valueOf(timeout), TimeUnit.DAYS);
            if (boo){
                return "true";
            }
        }
        return "false";
    }

    @RequestMapping("/logOut")
    public String logOut(String redirectUrl,HttpSession session,HttpServletRequest request){
        //销毁全局会话
//        String token = (String)session.getAttribute("token");
//        if (StringUtils.isEmpty(token)){
//            String cookie = request.getHeader("Cookie");
//            System.out.println(cookie);
//        }
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
        String tokenClientInfoKey = redisUtils.getSSOKey(EToken.TOKEN_CLIENT_INFO.getName(), token);

        //List<ClientInfoVo> clientInfoList = MockDatabaseUtil.T_CLIENT_INFO.get(token);
        String tokenClientInfoStr = redisUtils.get(tokenClientInfoKey);
        List<ClientInfoVo> clientInfoList = JSON.parseArray(tokenClientInfoStr, ClientInfoVo.class);
        if (CollectionUtils.isEmpty(clientInfoList)){
            return ResultUtil.error("invalid Token");
        }
        return ResultUtil.success(clientInfoList.get(0));
    }
    @RequestMapping("/getGameType")
    @ResponseBody
    public String getGameType(Integer gameid){
        List<Map<String ,Object>> games = new ArrayList<>();
        Map<String,Object> gameMap1 = new HashMap<>();
        gameMap1.put("gameid",1);
        gameMap1.put("gameName","破天一剑");
        games.add(gameMap1);

        Map<String,Object> gameMap2 = new HashMap<>();
        gameMap2.put("gameid",2);
        gameMap2.put("gameName","骑士3");
        games.add(gameMap2);


        if (CollectionUtils.isEmpty(games)){
            return ResultUtil.error("没有获取到游戏类型");
        }
        for (Map<String,Object> map :games){
            if (gameid==(Integer) map.get("gameid")){
                return ResultUtil.success(map);
            }
        }
        return ResultUtil.error("没有获取到游戏类型");
    }

    /**
     * 用户名密码认证
     * @param username
     * @param password
     * @return
     */
    private String getUserInfo(String username,String password,String gameid) {
        try {
            //http://passport.t50.bcyxgame.com/login/loginapi?gameid=100&accountname=pciktest120&password=123456
            Connection.Response response = Jsoup.connect("http://passport.t50.bcyxgame.com/login/loginapi")
                    .data("accountname",username)
                    .data("password",password)
                    .data("gameid",gameid)
                    .method(Connection.Method.GET)
                    .execute();
            String body = response.body();
            JSONObject object = JSONObject.parseObject(body);
            return null;
        }catch (IOException e){
            return null;
        }
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        List<Integer> collect = numbers.stream().map(i -> i * i).distinct().limit(10).sorted().collect(Collectors.toList());
        System.out.println(collect.toString());
    }

}
