import {get, patch} from './api.js'
import {createConfirmModal, inputOnlyNumber} from "./common.js";

const renderInfo = {
    companyName: '',
    englishName: '',
    companyRegistrationNumber: '',
    roadNameAddress: '',
    products: '',
    homepageUrl: '',
    manager: '',
    email: '',
    phoneNumber: '',
    memo: ''
}

// 회원 상세정보 창
const inputCompanyName = document.getElementById('inputCompanyName');
const inputCompanyEnglishName = document.getElementById('inputCompanyEnglishName');
const inputRegistrationNumber = document.getElementById('inputRegistrationNumber');
const inputProduct = document.getElementById('inputProduct');
const inputManager = document.getElementById('inputManager');
const inputManagerEmail = document.getElementById('inputManagerEmail');
const inputManagerPhoneNumber = document.getElementById('inputManagerPhoneNumber');
const inputHomepageUrl = document.getElementById('inputHomepageUrl');
const inputRoadNameAddr = document.getElementById('inputRoadNameAddr');
const inputMemo = document.getElementById('inputMemo');

const saveButtonSet = document.getElementById('saveButtonSet');
const changeButtonSet = document.getElementById('updateButtonSet');

document.addEventListener('DOMContentLoaded', ()=>{
    if(window.location.host.includes('localhost:63342')){
        return;
    }
    renderCompany();
})

const renderCompany = async () => {
    const companyId = getCompanyId();
    const response = await get(`/api/company/domestic/${companyId}`)

    if(response.ok)
    {
        const companyDetailData = await response.json();

        setInputValue(companyDetailData);
        setInfo(companyDetailData);
    }
    else if(response.status === 404)
    {
        const errorData = await response.json();
        createConfirmModal({
            title: errorData.message
        }, function(){
            window.location.href = '/company/domestic';
        });
    }
    else{

    }
}

const setInputValue = (data) => {
    inputCompanyName.value = data.companyName || '';
    inputCompanyEnglishName.value = data.englishName || '';
    inputRegistrationNumber.value = data.companyRegistrationNumber || '';
    inputProduct.value = data.products || '';
    inputManager.value = data.manager || '';
    inputManagerEmail.value = data.email || '';
    inputManagerPhoneNumber.value = data.phoneNumber || '';
    inputHomepageUrl.value = data.homepageUrl || '';
    inputRoadNameAddr.value = data.roadNameAddress || '';
    inputMemo.value = data.memo || '';
}

const setInfo = (data) => {
    renderInfo.companyName = data.companyName
    renderInfo.englishName = data.englishName
    renderInfo.companyRegistrationNumber = data.companyRegistrationNumber
    renderInfo.roadNameAddress = data.roadNameAddress
    renderInfo.products = data.products
    renderInfo.homepageUrl = data.homepageUrl
    renderInfo.manager = data.manager
    renderInfo.email = data.email
    renderInfo.phoneNumber = data.phoneNumber
    renderInfo.memo = data.memo
}

const getCompanyId = () => {
    const pathname = window.location.pathname;
    return pathname.substring(pathname.lastIndexOf('/') + 1);
}

document.getElementById('inputManagerPhoneNumber').addEventListener('input', function (e) {
    // 숫자만 남기고 모든 문자를 제거
    inputOnlyNumber(e)
})

/**
 * Memo에서 textarea 높이 조절하는 js
 */
document.getElementById('inputMemo').addEventListener('input', function (){
    this.style.height = 'auto';
    this.style.height = this.scrollHeight + 'px';
})

document.getElementById('changeBtn').addEventListener('click',(e)=>{
    e.preventDefault();
    showSaveButtonSet();
})

const showSaveButtonSet = ()=>{
    changeButtonSet.hidden = true;
    saveButtonSet.hidden = false;

    changeDisableInputTag(false);
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

const cancelSave = () => {
    setInputValue(renderInfo);
    changeDisableInputTag(true)
}

const changeDisableInputTag = (tureOrFalse) => {
    inputCompanyName.disabled = tureOrFalse;
    inputCompanyEnglishName.disabled = tureOrFalse;
    inputRegistrationNumber.disabled = tureOrFalse;
    inputProduct.disabled = tureOrFalse;
    inputManager.disabled = tureOrFalse;
    inputManagerEmail.disabled = tureOrFalse;
    inputManagerPhoneNumber.disabled = tureOrFalse;
    inputHomepageUrl.disabled = tureOrFalse;
    inputRoadNameAddr.disabled = tureOrFalse;
    inputMemo.disabled = tureOrFalse;
}

document.getElementById('updateForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    const formData = new FormData(this);
    const companyData = {
        companyName: formData.get('companyName'),
        englishName: formData.get('englishName') || null,
        companyRegistrationNumber: formData.get('companyRegistrationNumber') || null,
        roadNameAddress: formData.get('roadNameAddress') || null,
        products: formData.get('products') || null,
        homepageUrl: formData.get('homepageUrl') || null,
        manager: formData.get('manager'),
        email: formData.get('email'),
        phoneNumber: formData.get('phoneNumber'),
        memo: formData.get('memo') || null
    };

    try {
        const companyId = getCompanyId();
        const response = await patch(`/api/company/domestic/save/${companyId}`, companyData);

        if(response.ok)
        {
            setInfo(companyData);
            const resultData = await response.json();
            createConfirmModal({
                title: resultData.result
            }, showUpdateButtonSet(), changeDisableInputTag(true),
                function(){
                renderCompany();
            });
        }
        else
        {
            const errorData = await response.json();
            createConfirmModal({
                title: errorData.message
            }, function(){
                window.location.href = '/company/domestic';
            });
        }
    } catch (error) {
        console.error('Error:', error);

        // 실패 처리 (예: 메시지 표시)
        alert('등록 실패: ' + error.message);
    }
});