<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
</head>
<body>
<h2>Login Demo</h2>
<%
    if (request.getSession().getAttribute("user_id") == null || request.getSession().getAttribute("user_id").equals("")) {
        %>
        <h5>未登录,<a href="Login">登录</a></h5>
        <%
    }
    else {
        %>
        <h5>已登录为<%=request.getSession().getAttribute("user_id") %>,<a href="Logout">注销</a></h5>
        <%
    }
%>
</body>
</html>
