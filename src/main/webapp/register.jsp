<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>OurChat 2.0 注册</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>
<div id="registerContainer">
    <h2>注册新用户</h2>

    <c:if test="${not empty error}">
        <div class="errorMsg">${error}</div>
    </c:if>

    <form id="registerForm" action="${pageContext.request.contextPath}/register" method="post">
        <div class="formRow">
            <label for="nickname">昵称:</label>
            <input type="text" id="nickname" name="nickname" required>
        </div>
        <div class="formRow">
            <label for="email">邮箱:</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div class="formRow">
            <label for="password">密码:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="formRow">
            <button type="submit">注册</button>
        </div>
    </form>

    <div class="loginLink">
        已有账号？<a href="${pageContext.request.contextPath}/login">去登录</a>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/register.js"></script>
</body>
</html>
