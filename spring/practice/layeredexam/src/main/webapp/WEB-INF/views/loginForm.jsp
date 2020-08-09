<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-07-17
  Time: 오후 8:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>loginForm</title>
</head>
<body>
    <h1>관리자 로그인</h1>
    <br>
    <br>${errorMessage}
    <br>

    <form action="/guestbook/login" method="post">
        암호 : <input type="password" name = "password"><br>
        <input type="submit">
    </form>
</body>
</html>
