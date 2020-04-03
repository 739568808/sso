var redirectUrl = document.getElementById('logOut').getAttribute('redirectUrl');
var passportUrl = "http://www.sso.com:8080";//认证中心地址
var url = passportUrl+'/logOut?redirectUrl='+redirectUrl;
var htmml = "<a href="+url+">退出系统</a>";
document.write(htmml)