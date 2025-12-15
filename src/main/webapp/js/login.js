const loginForm = document.getElementById('loginForm');

loginForm.addEventListener('submit', (e) => {
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!email || !password) {
        e.preventDefault();
        alert('请输入邮箱和密码');
    }
});
