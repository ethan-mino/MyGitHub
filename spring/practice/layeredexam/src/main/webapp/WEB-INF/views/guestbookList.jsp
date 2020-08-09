<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-06-30
  Time: 오전 9:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <!-- jstl -->

<html>
<head>
    <title>Title</title>
    <style>
        .guestbook{
            padding: 5px 0px 5px 5px;
            margin-bottom: 5px;
            border-bottom: 1px solid #efefef;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <h1>방명록</h1><br>
    방명록 전체 수 : ${guestbookNum} 방문한 수 : ${visitCount}<br>
    <br>
    <br>

    <div class = "guestboots">
        <c:forEach var = "guestbook" items = "${guestbookList}">
            <div class="guestbook">
                <div><lable>id : ${guestbook.id}</lable></div>
                <div><lable>name : ${guestbook.name}</lable></div>
                <div><p>${guestbook.content}</p></div>
                <div><label>regdate : ${guestbook.regdate}</label></div>
                <c:if test = "${sessionScope.isAdmin == 'true'}">
                    <a href="delete?id=${guestbook.id}">삭제</a><br>
                </c:if>
            </div>
        </c:forEach>
    </div>

    <c:forEach var = "pageIndex" items="${pageStartList}" varStatus="status">
        <a href="/guestbook/list?start=${pageIndex}">${status.index + 1}</a>&nbsp; &nbsp;   <!-- \${status.index\}는 0부터의 순서 -->
    </c:forEach><br>

    <form action="/guestbook/write" method="post">
        name : <input type="text" name = "name"><br>
        <textarea name = "content" cols = "60" rows = "6"></textarea><br>
        <input type="submit" value = "등록">
    </form>
</body>
</html>
