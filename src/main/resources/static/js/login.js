import { post } from './api.js';
import {parseJWT} from "./common.js";

const TRANSLATOR = 'ROLE_TRANSLATOR'

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
        // const token = response.headers.get('Authorization');

        // 2024.09.25 access & refresh 도입 후 변경
        const access = response.headers.get('access');
        if (access) {
            sessionStorage.setItem('access', access);
        } else {
            throw new Error('access token not found in response.');
        }

        // 로그인 성공 시 리다이렉트

        const obj = parseJWT(access);
        const role = obj.payload.roles[0];
        if(role === TRANSLATOR){
            window.location.href = '/projects/translator';
        }else {
            window.location.href = '/projects/admin';
        }

    } catch (error) {
        console.error('Error:', error);

        // 실패 처리 (예: 메시지 표시)
        alert('Login failed: ' + error.message);
    }
});
