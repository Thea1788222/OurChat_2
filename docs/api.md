## **1. 包结构与实体类**

### **1.1 com.example.ourchat_2.model.User**

* `Long userId`
* `String nickname`
* `String email`
* `String passwordHash`

### **1.2 com.example.ourchat_2.model.ChatMessage**

* `Long messageId`
* `Long senderId`
* `String senderNickname`
* `String content`
* `String visibility` // "ALL" 或 "PRIVATE"
* `Long receiverId` // 私聊接收人，可 null
* `Timestamp sendTime`

### **1.3 com.example.ourchat_2.model.SystemMessage**

* `Long systemMessageId`
* `Long userId`
* `String nickname`
* `String eventType` // "ONLINE" / "OFFLINE"
* `Timestamp eventTime`

### **1.4 com.example.ourchat_2.dto.UserDTO**

* `Long userId`
* `String nickname`
* `boolean online` // 在线状态

---

## **2. DAO 接口**

### **2.1 com.example.ourchat_2.dao.IUserDao**

* `int addUser(User user) throws SQLException`
* `User getUserById(Long userId) throws SQLException`
* `List<User> getAllUsers() throws SQLException`
* `int updateUser(User user) throws SQLException`
* `int deleteUser(Long userId) throws SQLException`

### **2.2 com.example.ourchat_2.dao.IChatMessageDao**

* `int addMessage(ChatMessage message) throws SQLException`
* `ChatMessage getMessageById(Long messageId) throws SQLException`
* `List<ChatMessage> getAllMessages() throws SQLException`
* `int deleteMessage(Long messageId) throws SQLException`

### **2.3 com.example.ourchat_2.dao.ISystemMessageDao**

* `int addSystemMessage(SystemMessage message) throws SQLException`
* `List<SystemMessage> getAllSystemMessages() throws SQLException`
* `int deleteSystemMessage(Long systemMessageId) throws SQLException`

---
## **3. Service 接口（更新版）**

### **3.1 com.example.ourchat_2.service.IUserService**（原有）

* `boolean register(String nickname, String rawPassword, String email)`
* `User login(String email, String rawPassword)`
* `void logout(Long userId)`
* `User getUserById(Long userId) throws SQLException`
* `List<UserDTO> getAllUsersWithOnlineStatus() throws SQLException`
* `int updateUser(User user) throws SQLException`
* `int deleteUser(Long userId) throws SQLException`
* `void setUserOnline(Long userId)`
* `void setUserOffline(Long userId)`
* `boolean isUserOnline(Long userId)`
* `int getOnlineUserCount()`

---

### **3.2 com.example.ourchat_2.service.IChatMessageService**（新增）

* `int addMessage(ChatMessage message) throws SQLException`
* `ChatMessage getMessageById(Long messageId) throws SQLException`
* `List<ChatMessage> getAllMessages() throws SQLException`
* `int deleteMessage(Long messageId) throws SQLException`
* `List<ChatMessage> getVisibleMessagesForUser(Long userId) throws SQLException`

    * 功能说明：获取指定用户可见的消息列表，包含全体消息、私聊接收的消息以及自己发送的私聊消息

---

### **3.3 com.example.ourchat_2.service.ISystemMessageService**（新增）

* `int addSystemMessage(SystemMessage message) throws SQLException`
* `List<SystemMessage> getAllSystemMessages() throws SQLException`
* `int deleteSystemMessage(Long systemMessageId) throws SQLException`
* `SystemMessage getLatestSystemMessage() throws SQLException`

 
---

## **4. Service 实现类（更新版）**

### **4.1 com.example.ourchat_2.service.UserServiceImpl**（原有）

* 实现 `IUserService`
* 内部直接 `new UserDaoImpl()`
* 内存维护在线用户 `Set<Long> onlineUserIds`
* 方法实现：注册邮箱检查、密码加密、在线状态管理、DTO 封装等

---

### **4.2 com.example.ourchat_2.service.ChatMessageServiceImpl**（新增）

* 实现 `IChatMessageService`
* 内部直接 `new ChatMessageDaoImpl()`
* 方法实现：

    * 添加消息、删除消息
    * 查询全部消息或按消息ID查询
    * 获取指定用户可见消息（全体消息 + 私聊接收消息 + 自己发送的私聊消息）

---

### **4.3 com.example.ourchat_2.service.SystemMessageServiceImpl**（新增）

* 实现 `ISystemMessageService`
* 内部直接 `new SystemMessageDaoImpl()`
* 方法实现：

    * 添加系统消息
    * 查询全部系统消息
    * 删除系统消息
    * 获取最新一条系统消息（按时间排序）
---

## **5. Util 类**

### **5.1 com.example.ourchat_2.util.PasswordUtil**

* `String hashPassword(String plainPassword)` // SHA-256 加密
* `boolean verifyPassword(String plainPassword, String hashedPassword)` // 校验明文与加密密码是否匹配

---

## **6. Controller 接口**

### **6.1 com.example.ourchat_2.controller.LoginServlet**

1. **URL**：`/login`
2. **方法**：`POST`
3. **请求参数**：

    1. `email`（String，必填）
    2. `password`（String，必填）
4. **功能说明**：

    1. 用户登录
    2. 验证邮箱和密码
    3. 若用户已在线，则强制下线旧会话
    4. 创建 Session 并设置用户在线状态
5. **返回**：

    1. 成功：重定向到 `/chatroom.jsp`
    2. 失败：转发回 `/login.jsp` 并带错误信息 `error`

---

### **6.2 com.example.ourchat_2.controller.LogoutServlet**

1. **URL**：`/logout`
2. **方法**：`GET`
3. **请求参数**：无
4. **功能说明**：

    1. 用户登出
    2. 清理当前 Session
    3. 设置用户离线状态
5. **返回**：重定向到 `/login.jsp`

---

### **6.3 com.example.ourchat_2.controller.ChatroomServlet**

1. **URL**：`/chatroom`
2. **方法**：`GET`
3. **请求参数**：无（依赖 Session 中 `userId` 和 `nickname`）
4. **功能说明**：

    1. 校验 Session，未登录重定向 `/login.jsp`
    2. 获取全部历史聊天消息
    3. 获取全部用户列表及在线状态
    4. 将数据设置到 Request 属性，转发到 JSP 渲染页面
5. **Request 属性**：

    1. `chatMessages`：`List<ChatMessage>` 历史消息
    2. `users`：`List<UserDTO>` 用户列表（含在线状态）
    3. `userId`：当前用户 ID
    4. `nickname`：当前用户昵称
6. **返回**：转发到 `/chatroom.jsp`

---

### **6.4 com.example.ourchat_2.websocket.ChatWebSocket**

1. **URL**：`/chat`
2. **方法**：`WebSocket`
3. **功能说明**：

    1. 用户上线时：

        1. 设置在线状态
        2. 广播上线系统消息
        3. 广播在线用户列表
    2. 用户发送消息：

        1. 广播聊天消息
    3. 用户下线时：

        1. 设置离线状态
        2. 广播下线系统消息
        3. 广播在线用户列表
4. **消息格式**：

    1. 聊天消息：`CHAT:senderNickname:content`
    2. 系统消息：`SYSTEM:nickname:ONLINE|OFFLINE`
    3. 在线用户列表：`ONLINE_USERS:userId1,userId2,...`

---

### **6.5 Session 与在线状态约定**

1. 用户登录成功后，Session 保存：

    1. `userId`（Long）
    2. `nickname`（String）
2. 在线用户由 `IUserService` 内部维护 `Set<Long> onlineUserIds`
3. 在线人数动态计算，不持久化
4. 系统消息由 Listener 或 WebSocket `onOpen/onClose` 触发
---

## **7. Listener 与 Filter**

### **7.1 com.example.ourchat_2.listener.OnlineUserListener**

* **功能说明**：

    1. 监听用户 Session 生命周期
    2. Session 被销毁时（用户下线或浏览器关闭、Session 过期）：

        * 设置用户离线状态（调用 `IUserService.setUserOffline`）
        * 添加系统消息记录（“OFFLINE” 类型，调用 `ISystemMessageService.addSystemMessage`）
* **实现接口**：`HttpSessionListener`
* **关键字段**：

    * `IUserService userService = new UserServiceImpl()`
    * `ISystemMessageService systemMessageService = new SystemMessageServiceImpl()`
* **方法**：

    * `sessionCreated(HttpSessionEvent se)`：空实现
    * `sessionDestroyed(HttpSessionEvent se)`：处理用户下线逻辑

---

### **7.2 com.example.ourchat_2.filter.LoginFilter**

* **功能说明**：

    1. 拦截用户请求，验证是否已登录
    2. 未登录用户重定向到 `/login.jsp`
* **实现接口**：`Filter`
* **方法**：

    * `init(FilterConfig filterConfig)`：空实现
    * `doFilter(ServletRequest request, ServletResponse response, FilterChain chain)`：

        * 判断 `HttpSession` 中是否存在 `userId`
        * 已登录：放行请求
        * 未登录：重定向到登录页
    * `destroy()`：空实现

---
