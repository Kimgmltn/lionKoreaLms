// api.js

const BASE_URL = '/api'; // 기본 API 경로 설정

async function get(endpoint, params = {}) {
    const url = new URL(`${BASE_URL}${endpoint}`);
    Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `${localStorage.getItem('jwt')}` // JWT 토큰을 헤더에 포함
        }
    });

    if (!response.ok) {
        throw new Error(`GET 요청 실패: ${response.statusText}`);
    }

    return response;
}

async function post(endpoint, data = {}) {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `${localStorage.getItem('jwt')}` // JWT 토큰을 헤더에 포함
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error(`POST 요청 실패: ${response.statusText}`);
    }

    return response;
}

async function patch(endpoint, data = {}) {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `${localStorage.getItem('jwt')}` // JWT 토큰을 헤더에 포함
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error(`PATCH 요청 실패: ${response.statusText}`);
    }

    return response;
}

async function remove(endpoint) {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `${localStorage.getItem('jwt')}` // JWT 토큰을 헤더에 포함
        }
    });

    if (!response.ok) {
        throw new Error(`DELETE 요청 실패: ${response.statusText}`);
    }

    return response;
}

export { get, post, patch, remove };
