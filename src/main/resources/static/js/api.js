// api.js

const BASE_URL = '/api'; // 기본 API 경로 설정

async function fetchWithAuth(url, options = {}){

    // 기본 헤더에 Authorization 추가
    const headers = {
        ...options.headers,
        'Content-Type': 'application/json',
        'Authorization': `${localStorage.getItem('jwt')}`
    }

    const response = await fetch(url, {
        ...options,
        headers: headers,
    });

    if(response.status === 401){
        window.location.href = '/login';
        return Promise.reject(new Error('Unauthorized'));
    }

    return response;
}

async function get(endpoint, params = {}) {
    const url = new URL(`${BASE_URL}${endpoint}`);
    // Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

    const response = await fetchWithAuth(url, {
        method: 'GET'
    });

    if (!response.ok) {
        throw new Error(`GET 요청 실패: ${response.statusText}`);
    }

    return response;
}

async function post(endpoint, data = {}) {
    const response = await fetchWithAuth(`${BASE_URL}${endpoint}`, {
        method: 'POST',
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error(`POST 요청 실패: ${response.statusText}`);
    }

    return response;
}

async function patch(endpoint, data = {}) {
    const response = await fetchWithAuth(`${BASE_URL}${endpoint}`, {
        method: 'PATCH',
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error(`PATCH 요청 실패: ${response.statusText}`);
    }

    return response;
}

async function remove(endpoint) {
    const response = await fetchWithAuth(`${BASE_URL}${endpoint}`, {
        method: 'DELETE'
    });

    if (!response.ok) {
        throw new Error(`DELETE 요청 실패: ${response.statusText}`);
    }

    return response;
}

export { get, post, patch, remove };
