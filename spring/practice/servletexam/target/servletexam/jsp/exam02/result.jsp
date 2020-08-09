<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-07-08
  Time: 오전 2:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    int v3 = 5;
%>
${v1} + ${v2} = ${result}, ${v3}
<%--<%
    int v1 = (int)request.getAttribute("v1");
    int v2 = (int)request.getAttribute("v2");
    int result = (int)request.getAttribute("result");
%>
<%=v1%> + <%=v2%> = <%=result%>--%>
</body>
</html>
