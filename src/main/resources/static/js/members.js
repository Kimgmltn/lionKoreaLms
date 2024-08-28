import { get } from './api.js';

document.addEventListener('DOMContentLoaded', () => {
    renderMembers();
});

const renderMembers = async (page = 0, size = 10) => {
    const pathname = window.location.pathname;
    const queryParams = new URLSearchParams({
        page:page,
        size:size
    })
    const response = await get(`/api${pathname}?${queryParams.toString()}`);
    const membersData = await response.json()

    const datatablesSimple = document.getElementById('datatablesSimple');

    if (!datatablesSimple) {
        console.error('No datatablesSimple element found on the page.');
        return;
    }

    const tbody = datatablesSimple.querySelector('tbody');
    membersData.content.forEach((member) => {

        const tr = document.createElement('tr');

        Object.values(member).forEach((value, index) => {
            const td = document.createElement('td');
            td.textContent = value;

            if(index === 0){
                td.hidden = true;
            }
            tr.appendChild(td);
        })

        tbody.appendChild(tr);
    });
}


