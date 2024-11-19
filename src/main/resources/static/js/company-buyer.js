import { get } from './api.js';
import { renderPagination } from './common.js'

document.addEventListener('DOMContentLoaded', ()=>{
    renderBuyers({});
})

const renderBuyers = async ({page = 0, size = 20, companyName}) => {
    const queryParams = new URLSearchParams({
        page:page,
        size:size,
        ...(companyName && {companyName})
    })
    const datatablesSimple = document.getElementById('datatablesSimple');

    if (!datatablesSimple) {
        console.error('No datatablesSimple element found on the page.');
        return;
    }
    const response = await get(`/api/company/buyer?${queryParams.toString()}`);

    const companiesData = await response.json()

    const tbody = datatablesSimple.querySelector('tbody');
    tbody.innerHTML = '';
    companiesData.content.forEach((company) => {

        const tr = document.createElement('tr');
        tr.addEventListener('click',()=>{
            window.location.href = `/company/buyer/${company.companyId}`;
        })

        Object.values(company).forEach((value, index) => {
            const td = document.createElement('td');
            td.textContent = value;

            if(index === 0){
                td.hidden = true;
            }
            tr.appendChild(td);
        })

        tbody.appendChild(tr);
    });

    const dom = document.getElementById('paginationContainer');
    renderPagination(dom, companiesData.page, renderBuyers, {size, companyName});
}