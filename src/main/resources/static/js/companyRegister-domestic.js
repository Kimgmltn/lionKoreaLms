import { post } from './api.js'
import {createConfirmModal, inputOnlyNumber} from './common.js'

document.getElementById('registerForm').addEventListener('submit', async function(event) {
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
        const response = await post('/api/company/domestic/save', companyData);
        const data = await response.json();

        await createConfirmModal({
            title: data.result
        }, function(){
            window.location.href=`/company/domestic/${data.companyId}`
        });

    } catch (error) {
        console.error('Error:', error);

        // 실패 처리 (예: 메시지 표시)
        alert('등록 실패: ' + error.message);
    }
});

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