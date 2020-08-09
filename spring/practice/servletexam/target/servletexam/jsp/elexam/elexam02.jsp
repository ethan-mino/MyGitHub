<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-07-09
  Time: 오후 6:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>

<%
    request.setAttribute("k", 10);
    request.setAttribute("m", true);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
k : ${k}<br>
k + 5 : ${k + 5}<br>
k - 5 : ${k - 5}<br>
k * 5 : ${k * 5}<br>
k / 5 : ${k div 5}<br>

k : ${k}<br>
m : ${m}<br>

k > 5 : ${k > 5}<br>
k < 5 : ${k < 5}<br>
k >= 5 : ${k >= 5}<br>
k <= 5 : ${k <= 5}<br>
!m : ${not m}<br>
!m : ${!m}<br>

</body>
</html>
