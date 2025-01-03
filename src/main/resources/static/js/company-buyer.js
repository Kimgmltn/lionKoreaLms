import {get, post} from './api.js';
import {createConfirmModal, downloadFile, renderPagination} from './common.js'

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

document.getElementById('downloadExcelForm').addEventListener('click', async function () {
    const response = await get(`/api/files/download/buyer`);
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

    const response = await post('/api/files/upload/buyer', formData);
    if(response.ok || response.status === 500){
        const body = await response.json();
        createConfirmModal({
            title: body.result
        }, function () {
            renderBuyers({})
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