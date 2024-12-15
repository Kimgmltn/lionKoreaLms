import {post} from "./api.js";
import {createConfirmModal, inputOnlyNumber} from './common.js'

document.getElementById('registerForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    const formData = new FormData(this);
    const memberData = {
        memberName: formData.get('memberName'),
        gender: formData.get('gender'),
        email: formData.get('email'),
        phoneNumber: formData.get('phoneNumber'),
        memo: formData.get('memo')
    };

    try {
        const response = await post('/api/members/save', memberData);
        const data = await response.json();

        await createConfirmModal({
            title: data.result
        }, function(){
            window.location.href=`/members/${data.memberId}`
        });

    } catch (error) {
        console.error('Error:', error);

        // 실패 처리 (예: 메시지 표시)
        alert('등록 실패: ' + error.message);
        return
    }
});

/**
 * Memo에서 textarea 높이 조절하는 js
 */
document.getElementById('inputMemo').addEventListener('input', function (){
    this.style.height = 'auto';
    this.style.height = this.scrollHeight + 'px';
})

/**
 * 핸드폰 입력 validation
 */
document.getElementById('inputCellPhone').addEventListener('input', function (e) {
    // 숫자만 남기고 모든 문자를 제거
    inputOnlyNumber(e)
})
