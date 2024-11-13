import { get } from './api.js';
import { renderPagination } from './common.js'

document.addEventListener('DOMContentLoaded', ()=>{
    renderDomesticCompanies();
})

const renderDomesticCompanies = async (page = 0, size = 20) => {
    const queryParams = new URLSearchParams({
        page:page,
        size:size
    })
    const response = await get(`/api/company/domestic?${queryParams.toString()}`);
    const companiesData = await response.json()

    const datatablesSimple = document.getElementById('datatablesSimple');

    if (!datatablesSimple) {
        console.error('No datatablesSimple element found on the page.');
        return;
    }

    const tbody = datatablesSimple.querySelector('tbody');
    tbody.innerHTML = '';
    companiesData.content.forEach((company) => {

        const tr = document.createElement('tr');
        tr.addEventListener('click',()=>{
            window.location.href = `/company/domestic/${company.companyId}`;
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
    renderPagination(dom, companiesData.page, renderDomesticCompanies);
}