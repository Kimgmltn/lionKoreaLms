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

const buyerTag = document.getElementById('buyerModal');
const buyerModal = new bootstrap.Modal(buyerTag,{
    backdrop: 'static',
    keyboard: true
})

const domesticCompanyTag = document.getElementById('domesticCompanyModal')
const domesticCompanyModal = new bootstrap.Modal(domesticCompanyTag,{
    backdrop: 'static',
    keyboard: true
})

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

buyerModal.addEventListener('hide.bs.modal', function (){
    document.getElementById('searchBuyer').reset();
    this.querySelector('table tbody').innerHTML = '';
})
domesticCompanyModal.addEventListener('hide.bs.modal', function (){
    document.getElementById('searchDomesticCompany').reset();
    this.querySelector('table tbody').innerHTML = '';
})
managerModal.addEventListener('hide.bs.modal', function(){
    document.getElementById('searchTranslator').reset();
    this.querySelector('table tbody').innerHTML = '';
})

const renderCompanies = async ({
   page = 0,
   size = 5,
   companyType,
   companyName,
   tableDom
}) => {
    const queryParams = new URLSearchParams({
        page:page,
        size:size,
        ...(companyName && {companyName})
    })

    const response = await get(`/api/company/${companyType}?${queryParams.toString()}`)
    const companiesData = await response.json();

    const tbody = tableDom;
    tbody.innerHTML = '';

    companiesData.content.forEach((company) => {

        const companyId = company.companyId;
        const companyName = company.companyName;

        const tr = document.createElement('tr');
        tr.addEventListener('click',()=>{
            if(companyType === 'buyer'){
                document.getElementById('inputBuyerId').value = companyId;
                document.getElementById('inputBuyer').value = companyName;
                buyerModal.hide();
            } else {
                document.getElementById('inputDomesticCompanyId').value = companyId;
                document.getElementById('inputDomesticCompany').value = companyName;
                domesticCompanyModal.hide();
            }
        })

        const companyIdTd = document.createElement('td');
        companyIdTd.textContent = companyId;
        companyIdTd.hidden = true;
        const companyNameTd = document.createElement('td');
        companyNameTd.textContent = companyName;

        tr.appendChild(companyIdTd)
        tr.appendChild(companyNameTd)

        tbody.appendChild(tr);
    });

    const dom = document.getElementById(`${companyType}_paginationContainer`);
    renderPagination(dom, companiesData.page, renderCompanies, {size, companyType, companyName, tableDom});
}

document.getElementById('buyerSearchButton').addEventListener('click', async function(){
    const companyName = document.getElementById('inputBuyerName').value;
    if(!companyName.trim()){
        alert('바이어 명칭을 입력하세요.')
        return;
    }

    renderCompanies({
        companyType:"buyer",
        companyName:companyName,
        tableDom: document.querySelector('#buyerTable tbody')
    });
})

document.getElementById('domesticCompanySearchButton').addEventListener('click', async function(){
    const companyName = document.getElementById('inputDomesticCompanyName').value;
    if(!companyName.trim()){
        alert('국내회사 명칭을 입력하세요.')
        return;
    }

    renderCompanies({
        companyType:"domestic",
        companyName:companyName,
        tableDom: document.querySelector('#domesticCompanyTable tbody')
    });

})

document.getElementById('translatorSearchButton').addEventListener('click', async function(){
    const companyName = document.getElementById('inputTranslatorName').value;
    if(!companyName.trim()){
        alert('이름을 입력하세요.')
        return;
    }

    const response = await get('')
})