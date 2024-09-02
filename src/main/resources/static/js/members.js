import { get } from './api.js';

document.addEventListener('DOMContentLoaded', () => {
    const radioButtons = document.querySelectorAll('input[name="role"]');

    const selectedRadio = document.querySelector('input[name="role"]:checked');
    if (selectedRadio) {
        renderMembers(selectedRadio.value);
    }

    radioButtons.forEach(radio => {
        radio.addEventListener('change', function() {
            renderMembers(this.value);
        });
    });
});

const renderMembers = async (role, page = 0, size = 20) => {
    const queryParams = new URLSearchParams({
        role: role === 'all' ? '' : role,
        page:page,
        size:size
    })
    const response = await get(`/api/members?${queryParams.toString()}`);
    const membersData = await response.json()

    const datatablesSimple = document.getElementById('datatablesSimple');

    if (!datatablesSimple) {
        console.error('No datatablesSimple element found on the page.');
        return;
    }

    const tbody = datatablesSimple.querySelector('tbody');
    tbody.innerHTML = '';
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

    renderPagination(role,membersData.page);
}

const renderPagination = (role, pageData) => {
    const paginationContainer = document.getElementById('paginationContainer');

    if (!paginationContainer) {
        console.error('No paginationContainer element found on the page.');
        return;
    }

    paginationContainer.innerHTML = ''; // Clear existing pagination

    const totalPages = pageData.totalPages;
    const currentPage = pageData.number;
    const maxVisibleButtons = 10; // Maximum number of buttons to show

    const startPage = Math.floor(currentPage / maxVisibleButtons) * maxVisibleButtons;
    const endPage = Math.min(startPage + maxVisibleButtons, totalPages);

    // Previous button
    const prevButton = document.createElement('li');
    prevButton.classList.add('page-item')
    if(currentPage === 0){
        prevButton.classList.add('disabled')
    }
    const prevLink = document.createElement('a');
    prevLink.classList.add('page-link');
    prevLink.href = '#';
    prevLink.textContent = 'Previous';
    prevLink.addEventListener('click', () => {
        if (currentPage > 0) renderMembers(role,currentPage - 1);
    });
    prevButton.appendChild(prevLink);
    paginationContainer.appendChild(prevButton);

    // Page buttons
    for (let i = startPage; i < endPage; i++) {
        const pageButton = document.createElement('li');
        pageButton.classList.add('page-item');
        if(i === currentPage) {
            pageButton.classList.add('active')
        }
        const pageLink = document.createElement('a');
        pageLink.classList.add('page-link');
        pageLink.href = '#';
        pageLink.textContent = i + 1;
        pageLink.addEventListener('click', () => {
            renderMembers(role,i);
        });
        pageButton.appendChild(pageLink);
        paginationContainer.appendChild(pageButton);
    }

    // Next button
    const nextButton = document.createElement('li');
    nextButton.classList.add('page-item');
    if(currentPage === totalPages - 1){
        nextButton.classList.add('disabled');
    }
    const nextLink = document.createElement('a');
    nextLink.classList.add('page-link');
    nextLink.href = '#';
    nextLink.textContent = 'Next';
    nextLink.addEventListener('click', () => {
        if (currentPage < totalPages - 1) renderMembers(role,currentPage + 1);
    });
    nextButton.appendChild(nextLink);
    paginationContainer.appendChild(nextButton);
}


