<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>OurChat 2.0 聊天室</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chatroom.css">
</head>
<body>
<div id="header">
    <span>欢迎, ${nickname} (ID: ${userId})</span>
    <a href="${pageContext.request.contextPath}/logout" id="logoutButton">退出登录</a>
</div>

<div id="main">
    <div id="chatContainer">
        <div id="chatArea">
            <c:forEach var="msg" items="${chatMessages}">
                <c:set var="sender" value="${userMap[msg.senderId]}"/>
                <c:set var="nicknameClass">
                    <c:choose>
                        <c:when test="${msg.senderId == userId}">me</c:when>
                        <c:when test="${sender.online}">online</c:when>
                        <c:otherwise>offline</c:otherwise>
                    </c:choose>
                </c:set>
                <div class="chat-message">
                    <span class="nickname ${nicknameClass}">${sender.nickname}:</span>
                    <span class="message-content"><c:out value="${msg.content}" /></span>
                    <span class="message-time">${msg.sendTime}</span>
                </div>
            </c:forEach>
        </div>


        <div id="inputArea">
            <select id="userSelect">
                <option value="">全员可见</option>
                <c:forEach var="u" items="${userMap.values()}">
                    <c:if test="${u.userId != userId}">
                        <option value="${u.userId}" class="${u.online ? 'online' : 'offline'}">
                                ${u.nickname}
                        </option>
                    </c:if>
                </c:forEach>
            </select>
            <textarea id="messageInput" rows="3" placeholder="输入消息..."></textarea>
            <button id="sendButton">发送</button>
        </div>
    </div>

    <div id="userContainer">
        <div id="onlineCountArea">在线人数: <span id="onlineCount">${onlineCount}</span></div>

        <div id="onlineUsersBox">
            <h4>在线用户</h4>
            <ul id="onlineUserList">
                <c:forEach var="u" items="${userMap.values()}">
                    <c:if test="${u.online}">
                        <li class="online">${u.nickname}</li>
                    </c:if>
                </c:forEach>
            </ul>
        </div>

        <div id="offlineUsersBox">
            <h4>离线用户</h4>
            <ul id="offlineUserList">
                <c:forEach var="u" items="${userMap.values()}">
                    <c:if test="${!u.online}">
                        <li class="offline">${u.nickname}</li>
                    </c:if>
                </c:forEach>
            </ul>
        </div>

        <div id="systemMessageArea">
            最新状态: <span id="systemMessage"></span>
        </div>
    </div>
</div>

<input type="hidden" id="userId" value="${userId}">
<input type="hidden" id="nickname" value="${nickname}">
<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script src="${pageContext.request.contextPath}/js/chatroom.js"></script>
</body>
</html>

