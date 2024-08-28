import {post} from './api.js'

document.getElementById('logout-image').addEventListener('click', async (event) => {
    event.preventDefault();

    const response = await post('/api/auth/logout');
    sessionStorage.clear();
    window.location.href = '/login';
})