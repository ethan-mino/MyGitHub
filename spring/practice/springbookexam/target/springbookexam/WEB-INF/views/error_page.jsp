<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-08-08
  Time: 오후 10:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="false" import="java.util.*" %>
<html>
<head>
    <title>error</title>
</head>
<body>
    <h3>${exception}</h3>
    <h4><c:out value="${exception.getMessage()}"></c:out></h4>
    <ul>
        <c:forEach items = "${exception.getStackTrace()}" var = "stack">
            <li><c:out value = "${stack}" ></c:out></li>
        </c:forEach>

    </ul>
    <h1>요청 처리중 서버에서 에러가 발생했습니다.</h1>
</body>
</html>
