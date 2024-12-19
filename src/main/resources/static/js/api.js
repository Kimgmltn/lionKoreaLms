// api.js

const EXCLUDE_URL_LIST = ['/api/auth/login', '/api/members/*/valid', '/api/members/accounts/*/password']
let isRefreshing = false; // Refresh 진행 여부 플래그
let refreshSubscribers = []; // 대기 중인 요청을 저장하는 배열
const htmlTag = document.getElementsByTagName('html')[0]

const onAccessTokenRefreshed = (newAccessToken) => {
    refreshSubscribers.forEach(callback => callback(newAccessToken));
    refreshSubscribers = [];
}

const addRefreshSubscriber = (callback) => {
    refreshSubscribers.push(callback);
}

const wildcardToRegex = (pattern) => {
    return new RegExp('^' + pattern.replace(/\*/g, '[a-zA-Z0-9]+') + '$');
}

const isExcludedUrl = (url) => {
    return EXCLUDE_URL_LIST.some(pattern => wildcardToRegex(pattern).test(url));
}

const fetchWithAuth = async (url, options = {}) => {

    const accessToken = sessionStorage.getItem('access');

    if (window.isLocalMode) {
        console.log('환경에서 실행됨')
        return;
    }
    if(!accessToken && !isExcludedUrl(url)){
        window.location.href = '/login';
        return Promise.reject(new Error('No JWT token found. Redirecting to login.'));
    }
    // 기본 헤더에 access 추가
    const headers = {
        ...options.headers,
        'Content-Type': 'application/json',
        'access': accessToken ? `${accessToken}` : ''
    }

    return new Promise(async (resolve, reject) => {
        if(isRefreshing){
            // 이미 refresh 진행 중이라면 요청을 list에 추가하고 대기
            await addRefreshSubscriber((newToken) => {
                const retryHeader = {
                    ...options.headers,
                    'Content-Type': 'application/json',
                    'access': `${newToken}`
                }
                resolve(fetch(url, {...options, headers: retryHeader}));
            });
            return;
        }

        try{
            let response = await fetch(url, {...options, headers});

            if (response.status === 401) {
                console.log('Access token expired, attempting reissue');
                isRefreshing = true;

                const reissueResponse = await fetch("/api/auth/reissue", {
                    method: 'POST'
                });

                if(reissueResponse.status === 429){
                    await addRefreshSubscriber((newToken) => {
                        const retryHeader = {
                            ...options.headers,
                            'Content-Type': 'application/json',
                            'access': `${newToken}`
                        }
                        resolve(fetch(url, {...options, headers: retryHeader}));
                    });
                    return;
                }

                if(!reissueResponse.ok){
                    console.error('Failed to reissue access token, redirecting to login');
                    sessionStorage.clear();
                    window.location.href = '/login';
                    return reject(new Error('Failed to reissue access token.'));
                }

                const newAccessToken = reissueResponse.headers.get('access');
                if (newAccessToken) {
                    sessionStorage.setItem('access', newAccessToken);
                    onAccessTokenRefreshed(newAccessToken); // 대기 중인 요청 처리
                } else {
                    throw new Error('No access token found in reissue response');
                }

                // 첫 번째 요청을 새 토큰으로 재시도
                const retryHeaders = {
                    ...options.headers,
                    'Content-Type': 'application/json',
                    'access': `${newAccessToken}`
                }
                // const retryResponse = await fetch(url, {...options, retryHeader});
                const retryResponse = await fetch(url, {...options, headers : retryHeaders});
                resolve(retryResponse);
            } else {
                resolve(response)
            }
        } catch(error){
            reject(error); // 오류 발생 시 거부
        } finally {
            isRefreshing = false;
            htmlTag.hidden = false;
        }
    })
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
