import {get, patch, post} from './api.js'
import {createConfirmModal, getLastPath, PROCESS_STATUS} from './common.js'

const buttonBox = document.getElementById('buttonBox')
const projectId = getLastPath();
const startButton = document.getElementById('start')
const completeButton = document.getElementById('complete')
const consultationNotes = document.getElementById('inputConsultationNotes')
const caption = document.getElementById('caption')
document.addEventListener('DOMContentLoaded', () => {
    // renderProjectInfo()
} );

const renderProjectInfo = async () => {
    const projectId = getLastPath();
    const response = await get(`/api/projects/translator/${projectId}`)
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
        switch(projectDetail.processStatus){
            case "WAITING":
                caption.classList.add('bg-secondary');
                caption.textContent = PROCESS_STATUS.WAITING
                break;
            case "PROGRESS":
                caption.classList.add('bg-warning');
                caption.textContent = PROCESS_STATUS.PROGRESS
                startButton.hidden = true
                completeButton.hidden = false
                consultationNotes.disabled = false
                break;
            case "COMPLETED":
                caption.classList.add('bg-success');
                caption.textContent = PROCESS_STATUS.COMPLETED
                startButton.hidden = true
                break;
            case "REJECT":
                caption.classList.add('bg-danger');
                caption.textContent = PROCESS_STATUS.REJECT
                startButton.hidden = true
                break;
        }
    }
}

startButton.addEventListener('click', async function(event){
    event.preventDefault();

    const response = await patch(`/api/projects/translator/${projectId}/start`)
    if(response.ok){
        startButton.hidden = true
        completeButton.hidden = false
        consultationNotes.disabled = false
        caption.classList = 'badge fs-6 position-absolute top-0 start-0 bg-warning';
        caption.textContent = PROCESS_STATUS.PROGRESS
    }
})

completeButton.addEventListener('click', async function(event){
    event.preventDefault();

    const request = {
        consultationNotes: consultationNotes.value
    }

    const response = await patch(`/api/projects/translator/${projectId}/complete`, request)
    if (response.ok) {
        const data = await response.json();

        await createConfirmModal({
            title: data.result
        }, function(){
            window.location.href=`/projects/translator/${projectId}`
        })
    } else{
        await createConfirmModal({
            title: data.result
        }, function (){
            window.location.reload()
        });
    }
})
// document.getElementById('rejectForm').addEventListener('submit',async function (event){
//     event.preventDefault();
//
//     const projectId = getLastPath();
//     const formData = new FormData(this);
//     const request = {
//         buyerId: projectId,
//         rejectReason: formData.get('rejectReason'),
//     };
//
//     try {
//         const response = await patch(`/api/projects/admin/${projectId}/reject`, request);
//         if(response.ok){
//             const data = await response.json();
//
//             await createConfirmModal({
//                 title: data.result
//             }, function(){
//                 window.location.href=`/projects/admin/${projectId}`
//             });
//         }else{
//             await createConfirmModal({
//                     title: data.result
//                 }, function (){
//                     window.location.reload()
//                 }
//             );
//         }
//     } catch (error) {
//         console.error('Error:', error);
//
//         // 실패 처리 (예: 메시지 표시)
//         alert('등록 실패: ' + error.message);
//     }
// })

