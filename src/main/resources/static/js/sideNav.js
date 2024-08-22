// sideNav.js
import { get } from './api.js';

const CACHE_KEY = 'menuCache'; // 캐시 키
const CACHE_EXPIRATION_TIME = 5 * 60 * 1000; // 5분 (밀리초 단위)

// 초기화 시 기존 캐시를 localStorage에서 불러오기
let menuCache = {
    data: null,
    expiration: 0
};

const loadCache = () => {
    const cachedData = localStorage.getItem(CACHE_KEY);
    if (cachedData) {
        menuCache = JSON.parse(cachedData);
    }
};

const saveCache = () => {
    localStorage.setItem(CACHE_KEY, JSON.stringify(menuCache));
};;

document.addEventListener('DOMContentLoaded', () => {
    loadCache();
    loadSideNavMenu();
});

const loadSideNavMenu = async () => {
    const currentTime = new Date().getTime();
    let menuData = '';
    // 캐시가 유효한지 확인
    if (menuCache.data && currentTime < menuCache.expiration) {
        menuData = menuCache.data;
        console.log("캐시에서 메뉴 가져옴")
    }else{
        try {
            const response = await get('/api/menu/my');
            menuData = await response.json();

            // 캐시 갱신 및 만료 시간 설정
            menuCache.data = menuData;
            menuCache.expiration = currentTime + CACHE_EXPIRATION_TIME;
            saveCache();
        } catch (error) {
            console.error('Error loading side navigation menu:', error);
        }
    }

    renderSideNavMenu(menuData);
};

const renderSideNavMenu = (menuData) => {
    const sideNavContainer = document.getElementById('side-nav');

    if (!sideNavContainer) {
        console.error('No sideNav element found on the page.');
        return;
    }

    // Clear any existing menu items
    sideNavContainer.innerHTML = '';

    menuData.forEach(({ menuId, menuName, menuLink}) => {

        const anchorElement = document.createElement('a');
        anchorElement.classList.add('nav-link');
        anchorElement.href = menuLink; // 메뉴 링크를 설정합니다.

        const divElement = document.createElement('div');
        divElement.classList.add('sb-nav-link-icon');

        const iconElement = document.createElement('i');
        iconElement.classList.add('fas'); // 아이콘 클래스를 추가합니다.

        // 요소들 조립
        divElement.appendChild(iconElement);
        anchorElement.appendChild(divElement);
        anchorElement.appendChild(document.createTextNode(menuName));
        sideNavContainer.appendChild(anchorElement);
    });
};
