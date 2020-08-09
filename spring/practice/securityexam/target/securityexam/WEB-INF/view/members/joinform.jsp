<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-07-19
  Time: 오전 7:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>joinform</title>
</head>
<body>
    <form action="/members/join" method="POST">
        name : <input type="text" name = "name"><br>
        email : <input type="text" name = "email"><br>
        password : <input type="password" name = "password"><br>
        <input type="submit" value = "가입하기">
    </form>
</body>
</html>
