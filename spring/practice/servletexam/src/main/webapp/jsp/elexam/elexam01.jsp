<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-07-09
  Time: 오후 5:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    pageContext.setAttribute("p1", "page scope value");
    request.setAttribute("r1", "request scope value");
    session.setAttribute("s1", "session scope value");
    application.setAttribute("a1", "application scope value");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    pageContext.getAttribute("p1") : <%=pageContext.getAttribute("p1")%> <br>
    request.getAttribute("r1") : <%=request.getAttribute("r1")%> <br>
    session.getAttribute("s1") : <%=session.getAttribute("s1")%> <br>
    application.getAttribute("a1") : <%=application.getAttribute("a1")%> <br>

    <br>
    pageContext.getAttribute("p1") : ${pageScope.p1} <br>
    request.getAttribute("r1") : ${requestScope.r1} <br>
    session.getAttribute("s1") : ${sessionScope.s1} <br>
    application.getAttribute("a1") : ${applicationScope.a1} <br>

    <br>
    pageContext.getAttribute("p1") : ${p1} <br>
    request.getAttribute("r1") : ${r1} <br>
    session.getAttribute("s1") : ${s1} <br>
    application.getAttribute("a1") : ${a1}

</body>
</html>
