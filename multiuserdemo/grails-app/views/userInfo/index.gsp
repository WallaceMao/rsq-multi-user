<%--
  Created by IntelliJ IDEA.
  User: czip
  Date: 2017/6/19
  Time: 15:35
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="user"/>
    <title>用户信息</title>
</head>

<body>

<sec:ifLoggedIn>
    Logged in as <sec:username/>
</sec:ifLoggedIn>
<sec:ifSwitched>
%{--<a href='${request.contextPath}/j_spring_security_exit_user'>--}%
    original user:<sec:switchedUserOriginalUsername/>
%{--</a>--}%
</sec:ifSwitched>

%{--<sec:ifNotSwitched>--}%
%{--<sec:ifAllGranted roles='ROLE_SWITCH_USER'>--}%

<form action='${request.contextPath}/userInfo/switchUser'
      method='POST'>
    Switch to user: <input type='text' name='j_username'/><br/>
    <input type='submit' value='Switch'/>
</form>

<div id="wx-qrcode" style="width: 200px;height:200px;border:solid 1px #f00;overflow:hidden;">
    %{--<iframe src="http://localhost:8080/multiuserdemo/userInfo/testBlock" frameborder="0"></iframe>--}%
</div>

%{--</sec:ifAllGranted>--}%
%{--</sec:ifNotSwitched>--}%

<asset:javascript src="wxtest.js" />
<script async type="text/javascript">
//    setTimeout(function(){
//        new WxLogin({
//            id: "wx-qrcode"
//        });
//    },3000);
</script>

</body>
</html>