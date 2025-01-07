import {get, post} from './api.js';
import {createConfirmModal, downloadFile, renderPagination} from './common.js'

const searchDomesticCompanyName = () => {
    const companyName = document.getElementById('searchName').value.trim()
    renderDomesticCompanies({companyName:companyName})
}

document.addEventListener('DOMContentLoaded', ()=>{
    renderDomesticCompanies({});
})

const renderDomesticCompanies = async ({page = 0, size = 20, companyName}) => {
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
    const response = await get(`/api/company/domestic?${queryParams.toString()}`);

    const companiesData = await response.json()

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
    renderPagination(dom, companiesData.page, renderDomesticCompanies, {size, companyName});
}

document.getElementById('downloadExcelForm').addEventListener('click', async function () {
    const response = await get(`/api/files/download/domesticCompany`);
    if(response.ok){
        const contentDisposition = response.headers.get('Content-Disposition')
        const blob = await response.blob();
        downloadFile(contentDisposition, blob);
    }
});

document.getElementById('uploadExcelForm').addEventListener('click', function(){
    document.getElementById('hiddenExcelInput').click();
})

document.getElementById('hiddenExcelInput').addEventListener('change', async function (event){
    const file = event.target.files[0];
    if (!file) {
        alert("파일을 선택해주세요.")
        return;
    }

    const fileName = file.name.toLowerCase();
    if (!fileName.endsWith('.xlsx') && !fileName.endsWith('.xls')) {
        alert('엑셀 파일만 업로드 가능합니다');
        return;
    }
    const formData = new FormData();
    formData.append('file', file);

    const response = await post('/api/files/upload/domesticCompany', formData);
    if(response.ok || response.status === 500){
        const body = await response.json();
        createConfirmModal({
            title: body.result
        }, function () {
            renderDomesticCompanies({})
        });
    }else if(response.status===400){
        const contentDisposition = response.headers.get('Content-Disposition')
        const blob = await response.blob();

        createConfirmModal({
            title:"양식에 맞지 않는 엑셀입니다."
            , function(){
                downloadFile(contentDisposition, blob)
            }
        })
        downloadFile(contentDisposition, blob);
    }else{
        const errorData = await response.json();
        createConfirmModal({
            title: errorData.message
        });
    }
})

document.getElementById('searchName').addEventListener('keydown', function (e) {
    if(e.key ==='Enter'){
        e.preventDefault();
        searchDomesticCompanyName()
    }
});

document.getElementById('searchNameButton').addEventListener('click', searchDomesticCompanyName)