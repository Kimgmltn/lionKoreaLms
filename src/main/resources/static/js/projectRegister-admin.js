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
        buyerId: formData.get('buyerId'),
        domesticCompanyId: formData.get('domesticCompanyId'),
        projectName: formData.get('projectName'),
        translatorId: formData.get('translatorId'),
        language: formData.get('language'),
        consultationDate: formData.get('consultationDate'),
        hour: formData.get('hour'),
        minute: formData.get('minute'),
        timePeriod: formData.get('timePeriod'),
        consultationNotes: formData.get('consultationNotes'),
    };

    try {
        const response = await post('/api/projects/admin/save', projectData);
        if(response.ok){
            const data = await response.json();

            await createConfirmModal({
                title: data.result
            }, function(){
                window.location.href=`/projects/admin/${data.projectId}`
            });
        }else{
            await createConfirmModal({
                title: data.result
            },
            );
        }
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

buyerTag.addEventListener('hide.bs.modal', function (){
    document.getElementById('searchBuyer').reset();
    this.querySelector('table tbody').innerHTML = '';
    this.querySelector('#buyer_paginationContainer').innerHTML = '';
})
domesticCompanyTag.addEventListener('hide.bs.modal', function (){
    document.getElementById('searchDomesticCompany').reset();
    this.querySelector('table tbody').innerHTML = '';
    this.querySelector('#domestic_paginationContainer').innerHTML = '';
})
translatorTag.addEventListener('hide.bs.modal', function(){
    document.getElementById('searchTranslator').reset();
    this.querySelector('table tbody').innerHTML = '';
    this.querySelector('#translator_paginationContainer').innerHTML = '';
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
    const tbody = tableDom;
    tbody.innerHTML = '';
    const companiesData = await response.json();

    if (companiesData.content && companiesData.content.length !== 0) {

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
    }else{
        const tr = document.createElement('tr');
        const td = document.createElement('td');
        td.innerHTML = '검색 결과가 존재하지 않습니다.'
        tr.appendChild(td)
        tbody.appendChild(tr)
    }
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
    const translatorName = document.getElementById('inputTranslatorName').value;
    if(!translatorName.trim()){
        alert('이름을 입력하세요.')
        return;
    }

    await renderTranslator({translatorName});
})

document.getElementById('inputBuyerName').addEventListener('keydown', async function(event){
    if(event.key === 'Enter'){
        event.preventDefault();

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
    }
})

document.getElementById('inputDomesticCompanyName').addEventListener('keydown', async function(event){
    if(event.key === 'Enter'){
        event.preventDefault();

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
    }
})

document.getElementById('inputTranslatorName').addEventListener('keydown', async function(event){
    if(event.key === 'Enter'){
        event.preventDefault();

        const translatorName = document.getElementById('inputTranslatorName').value;
        if(!translatorName.trim()){
            alert('이름을 입력하세요.')
            return;
        }

        await renderTranslator({translatorName:translatorName});
    }
})

const renderTranslator = async ({page = 0 , size = 5, translatorName}) => {
    const queryParams = new URLSearchParams({
        page:page,
        size:size,
        ...(translatorName && {translatorName})
    })
    const tbody = document.querySelector('#translatorTable tbody');
    tbody.innerHTML = '';

    const response = await get(`/api/members/translator?${queryParams.toString()}`)
    const membersData = await response.json();

    if (membersData.content && membersData.content.length !== 0) {
        membersData.content.forEach((member) => {

            const accountId = member.accountId;
            const memberName = member.memberName;

            const tr = document.createElement('tr');
            tr.addEventListener('click',()=>{
                document.getElementById('translatorId').value = accountId;
                document.getElementById('inputManager').value = memberName;
                managerModal.hide();
            })

            const memberIdTd = document.createElement('td');
            memberIdTd.textContent = accountId;
            memberIdTd.hidden = true;
            const memberNameTd = document.createElement('td');
            memberNameTd.textContent = memberName;

            tr.appendChild(memberIdTd)
            tr.appendChild(memberNameTd)

            tbody.appendChild(tr);
        });

        const dom = document.getElementById(`translator_paginationContainer`);
        renderPagination(dom, membersData.page, renderTranslator, {size, translatorName});
    }else{
        const tr = document.createElement('tr');
        const td = document.createElement('td');
        td.innerHTML = '검색 결과가 존재하지 않습니다.'
        tr.appendChild(td)
        tbody.appendChild(tr)
    }



}