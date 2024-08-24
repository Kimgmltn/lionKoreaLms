import { get } from './api.js';

document.addEventListener('DOMContentLoaded', () => {
    renderMembers();
});

const renderMembers = async () => {
    const response = await get("/api/member");
    const membersData = await response.json()

    const datatablesSimple = document.getElementById('datatablesSimple');

    if (!datatablesSimple) {
        console.error('No datatablesSimple element found on the page.');
        return;
    }

    const tbody = datatablesSimple.querySelector('tbody');
    membersData.forEach((member) => {

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

    new DataTable(datatablesSimple);
}


