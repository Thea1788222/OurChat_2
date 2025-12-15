document.addEventListener('DOMContentLoaded', () => {
    const userId = document.getElementById('userId').value;
    const nickname = document.getElementById('nickname').value;
    const contextPath = document.getElementById('contextPath').value;

    const chatArea = document.getElementById('chatArea');
    const messageInput = document.getElementById('messageInput');
    const sendButton = document.getElementById('sendButton');
    const userSelect = document.getElementById('userSelect');
    const onlineUserList = document.getElementById('onlineUserList');
    const offlineUserList = document.getElementById('offlineUserList');
    const onlineCount = document.getElementById('onlineCount');
    const systemMessage = document.getElementById('systemMessage');

    // 缓存当前显示的用户列表
    let currentOnlineUsers = new Map();
    let currentOfflineUsers = new Map();

    // 初始化 WebSocket
    let wsProtocol = location.protocol === 'https:' ? 'wss' : 'ws';
    let ws = new WebSocket(`${wsProtocol}://${location.host}${contextPath}/chatSocket`);

    // 发送消息函数
    function sendMessage() {
        const content = messageInput.value.trim();
        if (!content) return;

        const targetUserId = userSelect.value; // ""表示全员
        const message = {
            type: 'chat',
            senderId: userId,
            senderNickname: nickname,
            targetUserId: targetUserId,
            content: content,
            time: new Date().toISOString()
        };

        ws.send(JSON.stringify(message));
        messageInput.value = '';
    }

    // 绑定发送事件
    sendButton.addEventListener('click', sendMessage);
    messageInput.addEventListener('keypress', e => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });

    // 增量更新在线/离线列表
    function updateUserLists(onlineUsers, offlineUsers) {
        const newOnlineUsers = new Map();
        const newOfflineUsers = new Map();

        onlineUsers.forEach(u => newOnlineUsers.set(u.userId, u));
        offlineUsers.forEach(u => newOfflineUsers.set(u.userId, u));

        updateListDOM(onlineUserList, currentOnlineUsers, newOnlineUsers, 'online');
        updateListDOM(offlineUserList, currentOfflineUsers, newOfflineUsers, 'offline');

        currentOnlineUsers = newOnlineUsers;
        currentOfflineUsers = newOfflineUsers;

        onlineCount.textContent = onlineUsers.length;

        // 更新下拉框
        userSelect.innerHTML = '<option value="">全员可见</option>';
        [...onlineUsers, ...offlineUsers].forEach(u => {
            if (u.userId != userId) {
                const opt = document.createElement('option');
                opt.value = u.userId;
                opt.textContent = u.nickname;
                opt.className = u.online ? 'online' : 'offline';
                userSelect.appendChild(opt);
            }
        });
    }

    // 辅助函数：增量更新 DOM
    function updateListDOM(container, currentMap, newMap, className) {
        currentMap.forEach((user, id) => {
            if (!newMap.has(id)) {
                const node = container.querySelector(`[data-userid='${id}']`);
                if (node) container.removeChild(node);
            }
        });

        newMap.forEach((user, id) => {
            let node = container.querySelector(`[data-userid='${id}']`);
            if (!node) {
                node = document.createElement('li');
                node.dataset.userid = id;
                node.className = className;
                node.textContent = user.nickname;
                container.appendChild(node);
            } else {
                node.textContent = user.nickname;
                node.className = className;
            }
        });
    }

    // 接收 WebSocket 消息
    ws.onmessage = event => {
        try {
            const data = JSON.parse(event.data);
            if (data.type === 'chat') {
                appendMessage(data); // msg.content 已经是消息文本
            } else if (data.type === 'system') {
                systemMessage.textContent = `${data.nickname} ${data.status}`;
                if (data.onlineUsers && data.offlineUsers) {
                    updateUserLists(data.onlineUsers, data.offlineUsers);
                }
            }
        } catch (e) {
            console.error('WebSocket消息解析失败', e);
        }
    };


    // WebSocket 打开时请求一次用户列表
    ws.onopen = () => {
        console.log('WebSocket 已连接');
        ws.send(JSON.stringify({ type: 'requestUserList' }));
    };

    ws.onerror = err => {
        console.error('WebSocket错误', err);
    };

    ws.onclose = () => {
        console.warn('WebSocket已关闭');
    };

    // 动态添加聊天消息
    function appendMessage(msg) {
        const msgDiv = document.createElement('div');
        msgDiv.className = 'chat-message';

        const nicknameSpan = document.createElement('span');
        nicknameSpan.className = 'nickname';
        nicknameSpan.textContent = msg.senderNickname + ':';
        if (msg.senderId == userId) nicknameSpan.classList.add('me');

        const contentSpan = document.createElement('span');
        contentSpan.className = 'message-content';
        contentSpan.textContent = msg.content;

        const timeSpan = document.createElement('span');
        timeSpan.className = 'message-time';
        timeSpan.textContent = new Date(msg.time).toLocaleTimeString();

        msgDiv.append(nicknameSpan, contentSpan, timeSpan);
        chatArea.appendChild(msgDiv);
        chatArea.scrollTop = chatArea.scrollHeight;
    }
});

