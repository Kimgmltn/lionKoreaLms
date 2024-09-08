import {get, patch, post} from './api.js'
import {createConfirmModal, inputOnlyNumber} from './common.js'

const renderInfo = {
    name: '',
    email: '',
    gender: '',
    cellphone: '',
    memo: ''
}
const inputNameTag = document.getElementById('inputName');
const inputEmailTag = document.getElementById('inputEmail');
const inputCellPhoneTag = document.getElementById('inputCellPhone');
const inputMemoTag = document.getElementById('inputMemo');
const genderTags = document.querySelectorAll('input[name="gender"]');
const saveButtonSet = document.getElementById('saveButtonSet');
const changeButtonSet = document.getElementById('updateButtonSet');

document.addEventListener('DOMContentLoaded', ()=>{
    renderMember();
    addEventForChangeButton();
})

const renderMember = async ()=> {
    const pathname = window.location.pathname;
    const memberId = pathname.substring(pathname.lastIndexOf('/') + 1);
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

const setInputValue = (data) => {
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

const setInfo = (data) => {
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

const addEventForChangeButton = () => {
    document.getElementById('changeBtn').addEventListener('click',(e)=>{
        e.preventDefault();
        showSaveButtonSet();
    })
}

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

document.getElementById('inputMemo').addEventListener('input', function (){
    this.style.height = 'auto';
    this.style.height = this.scrollHeight + 'px';
})

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
    const memberData = {
        memberName: formData.get('memberName').trim(),
        gender: formData.get('gender'),
        email: formData.get('email').trim(),
        phoneNumber: formData.get('phoneNumber'),
        memo: formData.get('memo')
    };

    try {
        const pathname = window.location.pathname;
        const memberId = pathname.substring(pathname.lastIndexOf('/') + 1);
        const response = await patch(`/api/members/save/${memberId}`, memberData);

        if(response.ok){
            const resultData = await response.json();
            createConfirmModal({
                title: resultData.result
            }, function(){
                window.location.reload();
            });
        }else{
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

document.getElementById('assignBtn').addEventListener('click', (event)=>{
    event.preventDefault();

    const myModal = new bootstrap.Modal(document.getElementById('assignIdModal'),{
        backdrop: 'static',
        keyboard: true
    })

    myModal.show();
})