import { get } from './api.js'

document.addEventListener('DOMContentLoaded', ()=>{

})

const renderMember = async ()=> {
    const pathname = window.location.pathname;
    const memberId = pathname.substring(pathname.lastIndexOf('/') + 1);
    const response = await get(`/api/members/${memberId}`);

    const inputNameTag = document.getElementById('inputName');
    const inputEmailTag = document.getElementById('inputEmail');
    const inputCellPhoneTag = document.getElementById('inputCellPhone');
    const inputMemoTag = document.getElementById('inputMemo');
    const genderTags = document.querySelectorAll('input[name="gender"]');

    inputNameTag.value = response.memberName;
    inputEmailTag.value = response.email;
    inputNameTag.value = response.memberName;
    inputNameTag.value = response.memberName;
    inputNameTag.value = response.memberName;
    inputNameTag.value = response.memberName;
}

