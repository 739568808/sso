var redirectUrl = document.getElementById('login').getAttribute('redirectUrl');
var passportUrl = "http://www.sso.com:8080";//认证中心地址
var htmml = "<form id='login' method=\"post\" action=\""+passportUrl+"/login\">\n" +
    "    <input type=\"hidden\" name=\"redirectUrl\" value="+redirectUrl+">\n" +
    "    <label>账户</label><input type=\"text\" name=\"username\">\n" +
    "    <label>密码</label><input type=\"password\" name=\"password\">\n" +
    "    <input type=\"submit\" value=\"登录\">\n" +
    "</form>";
document.write(htmml)