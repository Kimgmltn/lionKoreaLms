
const parseJwt = (token) => {
    const base64Url = token.split('.')[1]; // JWT의 두 번째 파트인 페이로드
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

export {parseJwt};