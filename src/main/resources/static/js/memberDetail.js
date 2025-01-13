import {get, patch, post} from './api.js'
import {createConfirmModal, inputOnlyNumber, getLastPath} from './common.js'

const renderInfo = {
    memberName: '',
    email: '',
    gender: '',
    phoneNumber: '',
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

document.addEventListener('DOMContentLoaded', ()=>{
    if(window.location.host.includes('localhost:63342')){
        return;
    }
    renderMember();
})

// 상세정보 랜더링
const renderMember = async ()=> {
    const memberId = getLastPath();
    const response = await get(`/api/members/${memberId}/detail`);

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
    renderInfo.memberName = data.memberName || '';
    renderInfo.email = data.email || '';
    renderInfo.gender = data.gender || '';
    renderInfo.phoneNumber = data.phoneNumber || '';
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
        const memberId = getLastPath();
        const response = await patch(`/api/members/save/${memberId}`, memberData);

        if(response.ok)
        {
            const resultData = await response.json();
            createConfirmModal({
                    title: resultData.result
                },
                showUpdateButtonSet(),
                cancelSave()
            );
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