var passportUrl = "http://www.sso.com:8080";//认证中心地址
var redirectUrl = document.getElementById('login').getAttribute('redirectUrl');
var gameid = document.getElementById('login').getAttribute('gameid');
var htmml = "<form id='login' method=\"post\" action=\""+passportUrl+"/login\">\n" +
    "    <input type=\"hidden\" name=\"redirectUrl\" value="+redirectUrl+">\n" +
    "    <input type=\"hidden\" name=\"gameid\" value="+gameid+">\n" +
    "    <div>游戏类型：<span id='gameName'></span></div>\n"+
    "    <label>账户</label><input type=\"text\" name=\"username\">\n" +
    "    <label>密码</label><input type=\"password\" name=\"password\">\n" +
    "    <input type=\"submit\" value=\"登录\">\n" +
    "</form>";


var errMsg = getQueryVariable("errMsg");
if(errMsg!=''&&errMsg!=null){
    htmml+="<div>"+errMsg+"</div>";
}

var SSO_Ajax = {
    get: function (url, fn) {
        // XMLHttpRequest对象用于在后台与服务器交换数据
        var xhr = new XMLHttpRequest();
        //每当readyState改变时就会触发onreadystatechange函数
        //0: 请求未初始化
        //1: 服务器连接已建立
        //2: 请求已接收
        //3: 请求处理中
        //4: 请求已完成，且响应已就绪
        xhr.open('GET', url, true)
        xhr.setRequestHeader("dataType", "json");
        xhr.onreadystatechange = function () {
            //readyStatus == 4说明请求已经完成
            if(xhr.readyState == 4 && xhr.status ==200) {
                //从服务器获得数据
                fn.call(this, xhr.responseText);
            }
        };
        //发送数据
        xhr.send();
    },
    // datat应为'a=a1&b=b1'这种字符串格式，在jq里如果data为对象会自动将对象转成这种字符串格式
    post: function (url, data, fn) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", url, true);
        // 添加http头，发送信息至服务器时内容编码类型
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 304)) {
                fn.call(this, xhr.responseText);
            }
        };
        //发送数据
        xhr.send(data);
    }

}
/**
 * 游戏id
 */
if (gameid!=''&&gameid!=null){

    SSO_Ajax.get(passportUrl+"/getGameType?gameid="+gameid,function (data) {
        var obj = JSON.parse(data);

        //TODO  请求游戏类型
        if (obj.code==200){

            document.getElementById("gameName").innerText= obj.data.gameName;
        }
    })
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






//显示登录框
document.write(htmml)