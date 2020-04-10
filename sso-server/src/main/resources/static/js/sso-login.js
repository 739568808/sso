var redirectUrl = document.getElementById('login').getAttribute('redirectUrl');
var passportUrl = "http://www.sso.com:8080";//认证中心地址
var htmml = "<form id='login' method=\"post\" action=\""+passportUrl+"/login\">\n" +
    "    <input type=\"hidden\" name=\"redirectUrl\" value="+redirectUrl+">\n" +
    "    <label>账户</label><input type=\"text\" name=\"username\">\n" +
    "    <label>密码</label><input type=\"password\" name=\"password\">\n" +
    "    <input type=\"submit\" value=\"登录\">\n" +
    "</form>";
var errMsg = getQueryVariable("errMsg");
if(errMsg!=''&&errMsg!=null){
    htmml+="<div>"+errMsg+"</div>";
}

function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return "";
}
document.write(htmml)