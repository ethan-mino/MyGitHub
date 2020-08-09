<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-07-19
  Time: 오전 7:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
    <head>
        <title>Title</title>
    </head>

    <body>
        <div>
            <div>
                <h1>회원정보</h1>
                <p>로그인한 회원 정보를 표기합니다.</p>
            </div>

            <div>
                <label>id</label>
                <p>${member.id}</p>
            </div>
            <div>
                <label>이름</label>
                <p>${member.name}</p>
            </div>
            <div>
                <label>암호</label>
                <p>${member.password}</p>
            </div>
            <div>
                <label>등록일</label>
                <p>${member.createDate}</p>
            </div>
            <div>
                <label>수정일</label>
                <p>${member.modifyDate}</p>
            </div>
        </div>
    </body>
</html>
