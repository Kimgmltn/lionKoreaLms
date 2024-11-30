import {get, patch, post} from './api.js'
import {createConfirmModal, getLastPath, PROCESS_STATUS} from './common.js'

const rejectModalTag = document.getElementById('rejectModal')
const rejectModal = new bootstrap.Modal(rejectModalTag,{
    backdrop: 'static',
    keyboard: true
})

rejectModalTag.addEventListener('hide.bs.modal', function(){
    document.getElementById('rejectForm').reset();
})

document.addEventListener('DOMContentLoaded', () => {
    renderProjectInfo()
} );

const renderProjectInfo = async () => {
    const projectId = getLastPath();
    const response = await get(`/api/projects/admin/${projectId}`)
    if(response.ok){
        const projectDetail = await response.json()
        document.getElementById('inputBuyer').value = projectDetail.buyerName;
        document.getElementById('inputDomesticCompany').value = projectDetail.domesticCompanyName;
        document.getElementById('inputProjectName').value = projectDetail.projectName;
        document.getElementById('inputManager').value = projectDetail.translatorName;
        document.getElementById('inputLanguage').value = projectDetail.language;
        document.getElementById('datepicker').value = projectDetail.consultationDate;
        document.getElementById('inputHour').value = projectDetail.hour;
        document.getElementById('inputMinute').value = projectDetail.minute;
        document.getElementById('inputTimePeriod').value = projectDetail.timePeriod;
        document.getElementById('inputConsultationNotes').value = projectDetail.consultationNotes;
        const caption = document.getElementById('caption');
        switch(projectDetail.processStatus){
            case "WAITING":
                caption.classList.add('bg-secondary');
                caption.textContent = PROCESS_STATUS.WAITING
                break;
            case "PROGRESS":
                caption.classList.add('bg-warning');
                caption.textContent = PROCESS_STATUS.PROGRESS
                break;
            case "COMPLETED":
                caption.classList.add('bg-success');
                caption.textContent = PROCESS_STATUS.COMPLETED
                break;
            case "REJECT":
                caption.classList.add('bg-danger');
                caption.textContent = PROCESS_STATUS.REJECT
                break;
        }
    }
}

document.getElementById('rejectForm').addEventListener('submit',async function (event){
    event.preventDefault();

    const projectId = getLastPath();
    const formData = new FormData(this);
    const request = {
        buyerId: projectId,
        rejectReason: formData.get('inputRejectReason'),
    };

    try {
        const response = await patch(`/api/projects/admin/${projectId}/reject`, request);
        if(response.ok){
            const data = await response.json();

            await createConfirmModal({
                title: data.result
            }, function(){
                window.location.href=`/projects/admin/${data.projectId}`
            });
        }else{
            await createConfirmModal({
                    title: data.result
                }, function (){
                    window
                }
            );
        }
    } catch (error) {
        console.error('Error:', error);

        // 실패 처리 (예: 메시지 표시)
        alert('등록 실패: ' + error.message);
    }
})

