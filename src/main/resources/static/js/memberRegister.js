import {post} from "./api.js";
import {createModal} from './common.js';

document.getElementById('saveForm').addEventListener('submit', async function(event) {
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

        await createModal({
            title: response.result
        }, function(){
            window.location.href=`/members/${response.memberId}`
        });

    } catch (error) {
        console.error('Error:', error);

        // 실패 처리 (예: 메시지 표시)
        alert('등록 실패: ' + error.message);
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
    let numbers = e.target.value.replace(/\D/g, '');

    // 최대 11자리까지만 남기기
    if (numbers.length > 11) {
        numbers = numbers.slice(0, 11);
    }

    // 입력한 숫자에 맞춰 포맷팅
    let formattedNumber = '';
    if (numbers.length <= 3) {
        formattedNumber = numbers;
    } else if (numbers.length <= 7) {
        formattedNumber = numbers.slice(0, 3) + '-' + numbers.slice(3);
    } else {
        formattedNumber = numbers.slice(0, 3) + '-' + numbers.slice(3, 7) + '-' + numbers.slice(7);
    }

    // 입력란에 포맷팅된 값을 넣어줌
    e.target.value = formattedNumber;
})
