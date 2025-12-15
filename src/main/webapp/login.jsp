<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>OurChat 2.0 登录</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<div id="loginContainer">
    <h2>登录聊天室</h2>

    <c:if test="${not empty error}">
        <div class="errorMsg">${error}</div>
    </c:if>

    <form id="loginForm" action="${pageContext.request.contextPath}/login" method="post">
        <div class="formRow">
            <label for="email">邮箱:</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div class="formRow">
            <label for="password">密码:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="formRow">
            <button type="submit">登录</button>
        </div>
    </form>

    <div class="registerLink">
        没有账号？<a href="${pageContext.request.contextPath}/register">去注册</a>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html>
