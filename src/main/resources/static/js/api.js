// api.js

const EXCLUDE_URL_LIST = ['/api/auth/login']

const fetchWithAuth = async (url, options = {}) => {

    const token = sessionStorage.getItem('jwt');

    if (window.isLocalMode) {
        console.log('환경에서 실행됨')
        return;
    }
    if(!token && !url.includes(EXCLUDE_URL_LIST)){
        window.location.href = '/login';
        return Promise.reject(new Error('No JWT token found. Redirecting to login.'));
    }
    // 기본 헤더에 Authorization 추가
    const headers = {
        ...options.headers,
        'Content-Type': 'application/json',
        'Authorization': token ? `${token}` : ''
    }

    const response = await fetch(url, {
        ...options,
        headers: headers,
    });

    if (response.status === 401) {
        sessionStorage.clear();
        window.location.href = '/login';
        return Promise.reject(new Error('No Authorization. Redirecting to login.'));
    }

    return response;
}

const get = async (endpoint, params = {}) => {
    // const url = new URL(`${endpoint}`);
    // Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

    const response = await fetchWithAuth(`${endpoint}`, {
        method: 'GET'
    });

    if (!response.ok) {
        throw new Error(`GET 요청 실패: ${response.statusText}`);
    }

    return response;
}

const post = async (endpoint, data = {}) => {
    const response = await fetchWithAuth(`${endpoint}`, {
        method: 'POST',
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error(`POST 요청 실패: ${response.statusText}`);
    }

    return response;
}

const patch = async (endpoint, data = {}) => {
    const response = await fetchWithAuth(`${endpoint}`, {
        method: 'PATCH',
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error(`PATCH 요청 실패: ${response.statusText}`);
    }

    return response;
}

const remove = async (endpoint) => {
    const response = await fetchWithAuth(`${endpoint}`, {
        method: 'DELETE'
    });

    if (!response.ok) {
        throw new Error(`DELETE 요청 실패: ${response.statusText}`);
    }

    return response;
}

export { get, post, patch, remove };
