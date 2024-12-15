import {get, patch, post} from './api.js'
import {createConfirmModal, getLastPath} from './common.js'

// ID 발급 모달 창
const assignIdTag = document.getElementById('assignIdModal')
const assignIdModal = new bootstrap.Modal(assignIdTag,{
    backdrop: 'static',
    keyboard: true
})

// ID 수정 모달 창
const assignIdUpdateTag = document.getElementById('assignIdUpdateModal')
const assignIdUpdateModal = new bootstrap.Modal(assignIdUpdateTag,{
    backdrop: 'static',
    keyboard: true
})
const inputAccountIdTag = document.getElementById('inputAccountId')
const inputLoginIdUpdateTag = document.getElementById('inputUpdateLoginId')
const roleUpdateTags = document.querySelectorAll('#updateRoleTag input[name="role"]')
const inputUseYnUpdateTag = document.getElementById('inputUpdateUseYn')
const inputUpdatePasswordChangeYnTag = document.getElementById('inputUpdatePasswordChangeYn')
const changePasswordButtonTag = document.getElementById('changePasswordButton')

document.addEventListener('DOMContentLoaded', ()=>{
    if(window.location.host.includes('localhost:63342')){
        const rows = document.querySelectorAll('#datatablesSimple tbody tr:not(#noAssignLoginId)');

        rows.forEach(row => {
            row.addEventListener('click', () => {
                const data = {
                    loginId: row.cells[1].textContent,
                    roleEng: row.cells[2].textContent === '번역가' ? 'translator' : 'admin', // 권한에 대한 영문 변환 (번역가, 관리자 예시)
                    useYn: row.cells[3].textContent === 'Y',
                    passwordChangeYn: row.cells[4].textContent === 'Y'
                };
                assignDetailModal(data);
            });
        });
        return;
    }
    renderAssignedId();
})

const assignDetailModal = (data) => {
    closeUpdateAssignModal()
    assignIdUpdateModal.show()

    inputAccountIdTag.value = data.accountId
    inputLoginIdUpdateTag.value = data.loginId
    roleUpdateTags.forEach(radio => {
        if(radio.value === data.roleEng){
            radio.checked = true;
        }
    })
    inputUseYnUpdateTag.checked = data.useYn;
    inputUpdatePasswordChangeYnTag.value = data.passwordChangeYn ? 'Y' : 'N';
}


// ID 발급 모달 닫힐 시 초기화
const closeInputAssignModal = () => {
    document.getElementById('assignIdForm').reset();
}

const closeUpdateAssignModal = () => {
    document.getElementById('assignIdUpdateForm').reset();
}

assignIdTag.addEventListener('hide.bs.modal', closeInputAssignModal)
assignIdUpdateTag.addEventListener('hide.bs.modal', closeUpdateAssignModal)

const renderAssignedId = async ()=> {
    const memberId = getLastPath();
    const response = await get(`/api/members/${memberId}/accounts`);

    if(response.ok)
    {
        const assignData = await response.json();

        // 데이터가 있을 경우, tbody 초기화
        if(assignData && assignData.length > 0){
            const tbody = document.querySelector('#assignLoginIdTable tbody');
            tbody.innerHTML = '';
            assignData.forEach((data, index)=>{
                const tr = document.createElement('tr');
                tr.addEventListener('click',()=>{
                    assignDetailModal(data);
                })

                const indexTd = document.createElement('td');
                indexTd.textContent = assignData.length - index;
                const accountIdTd = document.createElement('td');
                accountIdTd.textContent = data.accountId;
                accountIdTd.hidden = true;
                const loginIdTd = document.createElement('td');
                loginIdTd.textContent = data.loginId;
                const roleKorTd = document.createElement('td');
                roleKorTd.textContent = data.roleKor;
                const roleEngTd = document.createElement('td');
                roleEngTd.textContent = data.roleEng;
                roleEngTd.hidden = true;
                const useYnTd = document.createElement('td');
                useYnTd.textContent = data.useYn ? 'Y' : 'N';
                const passwordChangeYnTd = document.createElement('td');
                passwordChangeYnTd.textContent = data.passwordChangeYn ? 'Y' : 'N';
                const joinDateTd = document.createElement('td');
                joinDateTd.textContent = data.joinDate;
                const expireDateTd = document.createElement('td');
                expireDateTd.textContent = data.expireDate;

                tr.appendChild(indexTd)
                tr.appendChild(accountIdTd)
                tr.appendChild(loginIdTd)
                tr.appendChild(roleKorTd)
                tr.appendChild(roleEngTd)
                tr.appendChild(useYnTd)
                tr.appendChild(passwordChangeYnTd)
                tr.appendChild(joinDateTd)
                tr.appendChild(expireDateTd)

                tbody.appendChild(tr);
            })
        }
    }
    else if(response.status === 404)
    {
        const errorData = await response.json();
        createConfirmModal({
            title: errorData.message
        }, function(){
            window.location.href = '/members';
        });
    }
    else{

    }
}

// ID 발급 모달창 보여주기
document.getElementById('assignBtn').addEventListener('click', (event)=>{
    event.preventDefault();
    assignIdModal.show();
})

// ID 신규 발급
document.getElementById('assignIdForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const memberId = getLastPath();

    const formData = new FormData(event.target);
    const request = {
        loginId: formData.get('loginId').trim(),
        role: formData.get('role'),
        useYn: !!formData.get('useYn'),
        to: document.getElementById('inputEmail').value
    };

    const response = await post(`/api/members/${memberId}/newAccount`, request)
    if (response.ok) {
        const resultData = await response.json();
        createConfirmModal({
                title: resultData.result
            }, assignIdModal.hide(),
            renderAssignedId()
        );
    }else{
        const errorData = await response.json();
        createConfirmModal({
            title: errorData.message
        }, null);
    }
})

// ID 수정
document.getElementById('assignIdUpdateForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const memberId = getLastPath();

    const formData = new FormData(event.target);
    const accountId = formData.get('accountId');
    const request = {
        useYn: !!formData.get('useYn'),
    };

    const response = await patch(`/api/members/${memberId}/accounts/${accountId}`, request)
    if (response.ok) {
        const resultData = await response.json();
        createConfirmModal({
                title: resultData.result
            }, assignIdUpdateModal.hide(),
            renderAssignedId()
        );
    }else{
        const errorData = await response.json();
        createConfirmModal({
            title: errorData.message
        }, null);
    }
})

// 비밀번호 변경 요청
changePasswordButtonTag.addEventListener('click', async (event)=>{
    event.preventDefault();

    const accountId = inputAccountIdTag.value

    const response = await patch(`/api/members/${accountId}/rePassword`)
    if (response.ok) {
        const resultData = await response.json();
        createConfirmModal({
                title: resultData.result
            }, assignIdModal.hide(),
            renderAssignedId()
        );
    }else{
        const errorData = await response.json();
        createConfirmModal({
            title: errorData.message
        }, null);
    }

});