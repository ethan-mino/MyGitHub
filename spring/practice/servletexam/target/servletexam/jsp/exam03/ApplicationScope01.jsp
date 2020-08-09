<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-07-08
  Time: 오전 5:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    try{
        int value = (int)application.getAttribute("value");
        value += 2;
        out.println("<h1>" + value + "</h1>");
    }catch(NullPointerException e){
        e.printStackTrace();
        out.print("<h1>설정된 값이 없습니다.</h1>");
    }
%>

</body>
</html>
