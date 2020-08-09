<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020-07-18
  Time: 오후 8:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>로그인</title>
    </head>
    <body>
        <div>
            <div>
                <form action="/authenticate" method="post">
                    <div>
                        <label>ID</label>
                        <input type="text" name="userId">
                    </div>

                    <div>
                        <label>암호</label>
                        <input type="text" name="password">
                    </div>

                    <div>
                        <label></label>
                        <input type="submit" value = "로그인">
                    </div>
                </form>
            </div>
        </div>
        <a href="/members/joinform">회원가입</a>
    </body>
</html>
