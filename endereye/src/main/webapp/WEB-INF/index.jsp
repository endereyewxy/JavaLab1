<%--
  Created by endereyewxy@gmail.com, 2020.10.12
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="zh">
<head>
    <title>登录实验</title>
    <link crossorigin="anonymous" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-alpha1/dist/css/bootstrap.min.css"
          integrity="sha256-IdfIcUlaMBNtk4Hjt0Y6WMMZyMU0P9PN/pH+DFzKxbI=" rel="stylesheet">
    <script crossorigin="anonymous"
            integrity="sha256-u+Q/eQIe6P5wU4K8maihJOQkhqBbf7K1NN68GwTpNz0="
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-alpha1/dist/js/bootstrap.min.js"></script>
    <style>
        html, body {
            height: 100vh;
        }

        body {
            display: flex;
            background-color: #f5f5f5;
        }

        form {
            width: 100%;
            max-width: 400px;
            margin: auto;
        }

        #username {
            margin-bottom: -1px;
            border-bottom-left-radius: 0;
            border-bottom-right-radius: 0;
        }

        #password {
            border-top-left-radius: 0;
            border-top-right-radius: 0;
        }

        button {
            margin-top: 20px;
        }

        .alert {
            position: fixed;
            top: 10px;
            right: 10px;
        }
    </style>
</head>
<body class="text-center">
<form action="" method="post">
    <h1 class="h3 mb-3 font-weight-normal">登录</h1>
    <label class="sr-only" for="username">用户名</label>
    <input autofocus class="form-control" id="username" name="username" placeholder="用户名" required type="text">
    <label class="sr-only" for="password">密码</label>
    <input class="form-control" id="password" name="password" placeholder="密码" required type="password">
    <input name="from" type="hidden" value="<%=request.getParameter("from")%>">
    <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
</form>
<%
    final String error = request.getParameter("error");
    if (error != null) {
%>
<div class="alert alert-danger">
    <%=error%>
</div>
<%
    }
%>
</body>
</html>
