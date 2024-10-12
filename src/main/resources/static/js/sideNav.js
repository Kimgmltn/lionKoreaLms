// sideNav.js
import { get } from './api.js';

const MENU_KEY = 'menu'; // 캐시 키
// const CACHE_EXPIRATION_TIME = 5 * 60 * 1000; // 5분 (밀리초 단위)
const CACHE_EXPIRATION_TIME = 60 * 1000; // 1분 (밀리초 단위)
const OPEN_MENU_KEY = 'openMenus';

// 초기화 시 기존 캐시를 localStorage에서 불러오기
let menuCache = {
    data: null,
    expiration: 0
};

const loadMenuCache = () => {
    const cachedData = sessionStorage.getItem(MENU_KEY);
    if (cachedData) {
        menuCache = JSON.parse(cachedData);
    }
};


const loadOpenMenuState = () => {
    const openMenus = sessionStorage.getItem(OPEN_MENU_KEY);
    return openMenus ? JSON.parse(openMenus) : [];
}

const saveCache = (key, data) => {
    sessionStorage.setItem(key, JSON.stringify(data));
};

document.addEventListener('DOMContentLoaded', () => {
    if(window.location.host.includes('localhost:63342')){
        return;
    }
    loadMenuCache();
    loadSideNavMenu();
});

const loadSideNavMenu = async () => {
    const currentTime = new Date().getTime();
    let menuData = '';
    // 캐시가 유효한지 확인
    if (menuCache.data && currentTime < menuCache.expiration) {
        menuData = menuCache.data;
    }else{
        try {
            const response = await get('/api/menu/my');
            menuData = await response.json();

            // 캐시 갱신 및 만료 시간 설정
            menuCache.data = menuData;
            menuCache.expiration = currentTime + CACHE_EXPIRATION_TIME;
            saveCache(MENU_KEY, menuCache);
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

    const openMenus = loadOpenMenuState();

    menuData.forEach(({ menuId, menuName, menuLink, menuIkon, parentMenuId, childMenu }, index) => {
        // Menu item with childMenu
        if (childMenu && childMenu.length > 0) {
            const anchorElement = document.createElement('a');
            anchorElement.classList.add('nav-link', 'collapsed');
            anchorElement.href = '#';
            anchorElement.setAttribute('data-bs-toggle', 'collapse');
            anchorElement.setAttribute('data-bs-target', `#collapseLayouts-${index}`);
            anchorElement.setAttribute('aria-expanded', openMenus.includes(`#collapseLayouts-${index}`) ? 'true' : 'false');
            anchorElement.setAttribute('aria-controls', `collapseLayouts-${index}`);
            anchorElement.addEventListener('click', ()=>{
                const openMenus = loadOpenMenuState();
                if(openMenus.includes(`#collapseLayouts-${index}`)){
                    openMenus.splice(openMenus.indexOf(`#collapseLayouts-${index}`), 1);
                }else{
                    openMenus.push(`#collapseLayouts-${index}`);
                }
                saveCache(OPEN_MENU_KEY, openMenus);
            })

            const divIconElement = document.createElement('div');
            divIconElement.classList.add('sb-nav-link-icon');

            const iconElement = document.createElement('i');
            iconElement.classList.add('fas', menuIkon);

            const collapseArrowElement = document.createElement('div');
            collapseArrowElement.classList.add('sb-sidenav-collapse-arrow');

            const arrowIconElement = document.createElement('i');
            arrowIconElement.classList.add('fas', 'fa-angle-down');

            // Assemble elements
            collapseArrowElement.appendChild(arrowIconElement);
            divIconElement.appendChild(iconElement);
            anchorElement.appendChild(divIconElement);
            anchorElement.appendChild(document.createTextNode(menuName));
            anchorElement.appendChild(collapseArrowElement);
            sideNavContainer.appendChild(anchorElement);

            const collapseDivElement = document.createElement('div');
            collapseDivElement.classList.add('collapse');
            collapseDivElement.id = `collapseLayouts-${index}`;
            collapseDivElement.setAttribute('aria-labelledby', 'headingOne');
            collapseDivElement.setAttribute('data-bs-parent', '#sidenavAccordion');

            if (openMenus.includes(`#collapseLayouts-${index}`)) {
                collapseDivElement.classList.add('show');
            }else{
                collapseDivElement.classList.remove('show');
            }

            const nestedNavElement = document.createElement('nav');
            nestedNavElement.classList.add('sb-sidenav-menu-nested', 'nav');

            // Recursively render child menus
            childMenu.forEach(child => {
                const childAnchorElement = document.createElement('a');
                childAnchorElement.classList.add('nav-link');
                childAnchorElement.href = child.menuLink || '#';

                const divIconElement = document.createElement('div');
                divIconElement.classList.add('sb-nav-link-icon');

                const iconElement = document.createElement('i');
                iconElement.classList.add('fas', child.menuIkon);

                divIconElement.appendChild(iconElement);
                childAnchorElement.appendChild(divIconElement);
                childAnchorElement.appendChild(document.createTextNode(child.menuName));
                nestedNavElement.appendChild(childAnchorElement);
            });

            collapseDivElement.appendChild(nestedNavElement);
            sideNavContainer.appendChild(collapseDivElement);

        } else {
            // Menu item without childMenu
            const anchorElement = document.createElement('a');
            anchorElement.classList.add('nav-link');
            anchorElement.href = menuLink || '#';

            const divIconElement = document.createElement('div');
            divIconElement.classList.add('sb-nav-link-icon');

            const iconElement = document.createElement('i');
            iconElement.classList.add('fas', menuIkon);

            // Assemble elements
            divIconElement.appendChild(iconElement);
            anchorElement.appendChild(divIconElement);
            anchorElement.appendChild(document.createTextNode(menuName));
            sideNavContainer.appendChild(anchorElement);
        }
    });
};
