// api.js

const EXCLUDE_URL_LIST = ['/api/auth/login']

const fetchWithAuth = async (url, options = {}) => {

    const accessToken = sessionStorage.getItem('access');

    if (window.isLocalMode) {
        console.log('환경에서 실행됨')
        return;
    }
    if(!accessToken && !url.includes(EXCLUDE_URL_LIST)){
        window.location.href = '/login';
        return Promise.reject(new Error('No JWT token found. Redirecting to login.'));
    }
    // 기본 헤더에 Authorization 추가
    const headers = {
        ...options.headers,
        'Content-Type': 'application/json',
        'access': accessToken ? `${accessToken}` : ''
    }

    let response = await fetch(url, {
        ...options,
        headers: headers,
    });

    if (response.status === 401) {

        console.log('Access token expired, attempting reissue');

        const reissueResponse = await fetch("/api/auth/reissue",{
            method: 'POST',
            headers: headers
        })

        // refresh token이 만료 or 발급 에러시 로그인 창으로 이동
        if(!reissueResponse.ok){
            console.error('Failed to reissue access token or refresh Token is expired, redirecting to login');
            sessionStorage.clear();
            window.location.href = '/login';
            return Promise.reject(new Error('Failed to reissue access token or refresh Token is expired, redirecting to login'));
        }

        const newAccessToken = reissueResponse.headers.get('access');
        if (newAccessToken) {
            sessionStorage.setItem('access', newAccessToken);
        } else {
            console.error('No access token found in reissue response');
            sessionStorage.clear();
            window.location.href = '/login';
            return Promise.reject(new Error('Failed to retrieve new access token.'));
        }

        // 원래 요청을 새 Access Token으로 다시 시도
        const retryHeaders = {
            ...options.headers,
            'Content-Type': 'application/json',
            'access': newAccessToken
        };

        response = await fetch(url, {
            ...options,
            headers: retryHeaders,
        });
    }

    return response;
}

const get = async (endpoint, params = {}) => {
    // const url = new URL(`${endpoint}`);
    // Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

    return await fetchWithAuth(`${endpoint}`, {
        method: 'GET'
    });
}

const post = async (endpoint, data = {}) => {
    return await fetchWithAuth(`${endpoint}`, {
        method: 'POST',
        body: JSON.stringify(data)
    });
}

const patch = async (endpoint, data = {}) => {
    return await fetchWithAuth(`${endpoint}`, {
        method: 'PATCH',
        body: JSON.stringify(data)
    });
}

const remove = async (endpoint) => {
    return await fetchWithAuth(`${endpoint}`, {
        method: 'DELETE'
    });
}

export { get, post, patch, remove };
