import {get, post} from "./api.js";
import {createConfirmModal, renderPagination} from './common.js'

document.addEventListener('DOMContentLoaded', () => {
    let today = new Date().toISOString().split('T')[0];
    $( "#datepicker" ).datepicker({
        dateFormat: 'yy-mm-dd',
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
        changeMonth: true,
        changeYear: true,
        // onSelect:
    });
} );

const inputBuyerTag = document.getElementById('inputBuyer');
const buyerTag = document.getElementById('buyerModal');
const buyerModal = new bootstrap.Modal(buyerTag,{
    backdrop: 'static',
    keyboard: true
})

const inputDomesticCompanyTag = document.getElementById('inputDomesticCompany');
const domesticCompanyTag = document.getElementById('domesticCompanyModal')
const domesticCompanyModal = new bootstrap.Modal(domesticCompanyTag,{
    backdrop: 'static',
    keyboard: true
})

const inputManageTag = document.getElementById('inputManager');
const translatorTag = document.getElementById('manageModal')
const managerModal = new bootstrap.Modal(translatorTag,{
    backdrop: 'static',
    keyboard: true
})

document.getElementById('registerForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    const formData = new FormData(this);
    const projectData = {
        buyerId: formData.get(''),
        domesticCompanyId: formData.get(''),
        projectName: formData.get(''),
        translatorId: formData.get(''),
        language: formData.get(''),
        consultationDate: formData.get(''),
        hour: formData.get(''),
        minute: formData.get(''),
        timePeriod: formData.get(''),
        consultationNotes: formData.get(''),
    };

    try {
        const response = await post('/api/projects/admin/save', projectData);
        const data = await response.json();

        await createConfirmModal({
            title: data.result
        }, function(){
            window.location.href=`/projects/${data.projectId}`
        });

    } catch (error) {
        console.error('Error:', error);

        // 실패 처리 (예: 메시지 표시)
        alert('등록 실패: ' + error.message);
    }
});

document.getElementById('inputConsultationNotes').addEventListener('input', function (){
    this.style.height = 'auto';
    this.style.height = this.scrollHeight + 'px';
})

inputBuyerTag.addEventListener('click', function(event){
    event.preventDefault();
    buyerModal.show();
})

inputDomesticCompanyTag.addEventListener('click', function(event){
    event.preventDefault();
    domesticCompanyModal.show();
})

inputManageTag.addEventListener('click', function(event){
    event.preventDefault();
    managerModal.show();
})

buyerModal.addEventListener('hide.bs.modal', function (){
    document.getElementById('searchBuyer').reset();
    this.querySelector('table').innerHTML = '';
})
domesticCompanyModal.addEventListener('hide.bs.modal', function (){
    document.getElementById('searchDomesticCompany').reset();
    this.querySelector('table').innerHTML = '';
})
managerModal.addEventListener('hide.bs.modal', function(){
    document.getElementById('searchTranslator').reset();
    this.querySelector('table').innerHTML = '';
})

document.getElementById('buyerSearchButton').addEventListener('click', async function(){
    const companyName = document.getElementById('inputBuyerName').value;
    if(!companyName.trim()){
        alert('바이어 명칭을 입력하세요.')
        return;
    }

    const queryParams = new URLSearchParams({
        page:0,
        size:5,
        companyName: companyName
    })

    const response = await get(`/api/company/buyer?${queryParams}`)

    const companiesData = await response.json();
    const table = document.querySelector('#buyerTable tbody');
    table.innerHTML = '';

    const tbody = document.createElement('tbody');
    const tr = document.createElement('tbody');

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
})

document.getElementById('domesticCompanySearchButton').addEventListener('click', async function(){
    const companyName = document.getElementById('inputDomesticCompanyName').value;
    if(!companyName.trim()){
        alert('국내회사 명칭을 입력하세요.')
        return;
    }

    const queryParams = new URLSearchParams({
        page:0,
        size:5,
        companyName: companyName
    })

    const response = await get(`/api/company/domestic?${queryParams}`)


})

document.getElementById('translatorSearchButton').addEventListener('click', async function(){
    const companyName = document.getElementById('inputTranslatorName').value;
    if(!companyName.trim()){
        alert('이름을 입력하세요.')
        return;
    }

    const response = await get('')
})