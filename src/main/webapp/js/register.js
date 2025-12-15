const registerForm = document.getElementById('registerForm');

registerForm.addEventListener('submit', (e) => {
    const nickname = document.getElementById('nickname').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!nickname || !email || !password) {
        e.preventDefault();
        alert('请填写完整信息');
    } else if (password.length < 6) {
        e.preventDefault();
        alert('密码长度至少6位');
    }
});
