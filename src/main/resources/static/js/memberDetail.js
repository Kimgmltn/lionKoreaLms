import {get, patch, post} from './api.js'
import {createConfirmModal, inputOnlyNumber} from './common.js'

const renderInfo = {
    name: '',
    email: '',
    gender: '',
    cellphone: '',
    memo: ''
}
// 회원 상세정보 창
const inputNameTag = document.getElementById('inputName');
const inputEmailTag = document.getElementById('inputEmail');
const inputCellPhoneTag = document.getElementById('inputCellPhone');
const inputMemoTag = document.getElementById('inputMemo');
const genderTags = document.querySelectorAll('input[name="gender"]');
const saveButtonSet = document.getElementById('saveButtonSet');
const changeButtonSet = document.getElementById('updateButtonSet');

// ID 발급 모달 창
const assignIdTag = document.getElementById('assignIdModal')
const assignIdModal = new bootstrap.Modal(assignIdTag,{
    backdrop: 'static',
    keyboard: true
})
const inputLoginIdTag = document.getElementById('inputLoginId')
const roleTags = document.querySelectorAll('input[name="role"]')
const inputUseYnTag = document.getElementById('inputUseYn')

document.addEventListener('DOMContentLoaded', ()=>{
    if(window.location.host.includes('localhost:63342')){
        return;
    }
    renderMember();
    renderAssignedId();
})

// ID 발급 모달 닫힐 시 초기화
assignIdTag.addEventListener('hide.bs.modal', () => {
    document.getElementById('assignIdForm').reset();
    inputLoginIdTag.disable = false;
    roleTags.forEach(role => {
        role.disable = false;
    })
    inputUseYnTag.disable = false;
})

// memberId 획득
const getMemberId = () => {
    const pathname = window.location.pathname;
    return pathname.substring(pathname.lastIndexOf('/') + 1);
}
// 상세정보 랜더링
const renderMember = async ()=> {
    const memberId = getMemberId();
    const response = await get(`/api/members/${memberId}`);

    if(response.ok)
    {
        const memberDetailData = await response.json();

        setInputValue(memberDetailData);
        setInfo(memberDetailData);
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

const renderAssignedId = async ()=> {
    const memberId = getMemberId();
    const response = await get(`/api/members/${memberId}`);

    if(response.ok)
    {
        document.getElementById('noAssignLoginId').hidden = true;
        const assignData = await response.json();

        // tbody 초기화
        const tbody = document.querySelector('#assignLoginIdTable tbody');
        tbody.innerHTML = '';

        if(assignData && assignData.length > 0){
            assignData.forEach((data)=>{
                const tr = document.createElement('tr');
                tr.addEventListener('click',()=>{
                    //TODO: 상세 모달 보여주기

                })

                Object.values(data).forEach((value, index) => {
                    const td = document.createElement('td');
                    td.textContent = value;

                    if(index === 0){
                        td.hidden = true;
                    }
                    tr.appendChild(td);
                })
                tbody.appendChild(tr);
            })
        }else{
            const tr = document.createElement('tr');
            const td = document.createElement('td');

            td.classList.add('text-center')
            td.setAttribute('colspan', '100');
            td.textContent = '발급된 LoginId가 없습니다.'
            tr.appendChild(td);
            tbody.appendChild(tr);
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

const setAssignData = (data) =>
{
    //TODO: 데이터 정보 입히기
    inputLoginIdTag.value = data.memberName || '';
    roleTags.forEach(radio => {
        if (radio.value === data.role) {
            radio.checked = true;
        }
    });
    inputUseYnTag.value = data.useYn || false
}

const setInputValue = (data) =>
{
    inputNameTag.value = data.memberName || '';
    inputEmailTag.value = data.email || '';
    inputCellPhoneTag.value = data.phoneNumber || '';
    inputMemoTag.value = data.memo || '';
    genderTags.forEach(radio => {
        if (radio.value === data.gender) {
            radio.checked = true;
        }
    });
}

// 수정하다가 취소 할 수 있으므로, 전역변수로 이전 값 저장
const setInfo = (data) =>
{
    renderInfo.name = data.memberName || '';
    renderInfo.email = data.email || '';
    renderInfo.gender = data.gender || '';
    renderInfo.cellphone = data.phoneNumber || '';
    renderInfo.memo = data.memo || '';
}

document.getElementById('inputCellPhone').addEventListener('input', function (e) {
    // 숫자만 남기고 모든 문자를 제거
    inputOnlyNumber(e)
})

document.getElementById('changeBtn').addEventListener('click',(e)=>{
    e.preventDefault();
    showSaveButtonSet();
})

const showSaveButtonSet = ()=>{
    changeButtonSet.hidden = true;
    saveButtonSet.hidden = false;

    inputNameTag.disabled = false;
    inputEmailTag.disabled = false;
    inputCellPhoneTag.disabled = false;
    inputMemoTag.disabled = false;
    genderTags.forEach(radio => {
        radio.disabled = false;
    });
}

const showUpdateButtonSet = () => {
    changeButtonSet.hidden = false;
    saveButtonSet.hidden = true;
}

document.getElementById('cancelBtn').addEventListener('click', (e)=>{
    e.preventDefault();
    showUpdateButtonSet();
    cancelSave();
})

// textarea 자동크기 조정
document.getElementById('inputMemo').addEventListener('input', function (){
    this.style.height = 'auto';
    this.style.height = this.scrollHeight + 'px';
})

// 회원정보 수정하다 취소시, 기존정보 덮어씌우기
const cancelSave = () => {
    setInputValue(renderInfo);
    inputNameTag.disabled = true;
    inputEmailTag.disabled = true;
    inputCellPhoneTag.disabled = true;
    inputMemoTag.disabled = true;
    genderTags.forEach(radio => {
        radio.disabled = true;
    });
}

document.getElementById('updateForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    const formData = new FormData(this);
    const memberData =
    {
        memberName: formData.get('memberName') ? formData.get('memberName').trim() : '',
        gender: formData.get('gender'),
        email: formData.get('email') ? formData.get('email').trim() : '',
        phoneNumber: formData.get('phoneNumber'),
        memo: formData.get('memo')
    };

    try {
        setInfo(memberData);
        const memberId = getMemberId();
        const response = await patch(`/api/members/save/${memberId}`, memberData);

        if(response.ok)
        {
            const resultData = await response.json();
            createConfirmModal({
                title: resultData.result
            }, function(){
                renderMember()
            });
        }
        else
        {
            const errorData = await response.json();
            createConfirmModal({
                title: errorData.message
            }, function(){
                window.location.href = '/members';
            });
        }
    } catch (error) {
        console.error('Error:', error);

        // 실패 처리 (예: 메시지 표시)
        alert('등록 실패: ' + error.message);
    }
});

// ID 발급 모달창 보여주기
document.getElementById('assignBtn').addEventListener('click', (event)=>{
    event.preventDefault();
    assignIdModal.show();
})

// ID 신규 발급
document.getElementById('assignIdForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const memberId = getMemberId();

    const formData = new FormData(event.target);
    const request = {
        loginId: formData.get('loginId').trim(),
        role: formData.get('role'),
        memberId: memberId,
        useYn: !!formData.get('useYn')
    };

    const response = await post('/api/members/newAccount', request)
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
