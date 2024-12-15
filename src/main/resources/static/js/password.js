import {get, patch} from './api.js'
import {createConfirmModal, getLastPath} from "./common.js";

let accountId;
document.addEventListener('DOMContentLoaded', () => {
    checkValidUrl()
});

const checkValidUrl = async () => {
    const key = getLastPath();
    const response = await get(`/api/members/${key}/valid`);

    if(response.ok)
    {
        const data = await response.json();
        accountId = data.accountId;
    }
    else
    {
        const errorData = await response.json;
        createConfirmModal({
            title: errorData.message
        }, function(){
            window.location.href = "/404"
        })
    }
}

document.getElementById('changeButton').addEventListener('click', async (event) => {
    event.preventDefault();

    if (isEqualPassword()) {
        const request = {
            password: document.getElementById('inputPassword').value
        };

        const response = await patch(`/api/members/accounts/${accountId}/password`, request)
        if (response.ok) {
            const resultData = await response.json();
            createConfirmModal({
                    title: resultData.result
                }, function(){
                    window.location.href = "/login"
                }
            );
        }else{
            const errorData = await response.json();
            createConfirmModal({
                title: errorData.message
            }, null);
        }
    }
})

document.getElementById('inputConfirmPassword').addEventListener('input', function() {
    if (isEqualPassword()) {
        // 비밀번호가 일치할 경우
        this.setCustomValidity('');
        this.style.borderColor = 'green';
    } else {
        // 비밀번호가 일치하지 않을 경우
        this.setCustomValidity('비밀번호가 일치하지 않습니다.');
        this.style.borderColor = 'red';
    }
});

const isEqualPassword = () => {
    return document.getElementById('inputPassword').value === document.getElementById('inputConfirmPassword').value
}

