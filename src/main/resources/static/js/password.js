import {get} from './api.js'
import {createConfirmModal} from "./common.js";

//
let memberId;
document.addEventListener('DOMContentLoaded', () => {
    checkValidUrl()
});

const checkValidUrl = async () => {
    const key = getKey();
    const response = await get(`/api/members/${key}/valid`);

    if(response.ok)
    {
        const data = await response.json();
        memberId = data.memberId;
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

const getKey = () => {
    const pathname = window.location.pathname;
    return pathname.substring(pathname.lastIndexOf('/') + 1);
}