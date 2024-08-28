import { post } from './api.js';

document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    const formData = new FormData(this);
    const loginData = {
        loginId: formData.get('loginId'),
        password: formData.get('password'),
        // 'remember-me': formData.get('remember-me') ? true : false
    };

    try {
        const response = await post('/api/auth/login', loginData);

        // 서버에서 Authorization 헤더에 담겨 온 JWT 토큰을 sessionStorage에 저장
        const token = response.headers.get('Authorization');
        if (token) {
            sessionStorage.setItem('jwt', token);
        } else {
            throw new Error('Authorization token not found in response.');
        }

        // 로그인 성공 시 리다이렉트
        window.location.href = '/project';
    } catch (error) {
        console.error('Error:', error);

        // 실패 처리 (예: 메시지 표시)
        alert('Login failed: ' + error.message);
    }
});
