import {get} from './api.js'
import {getLastPath, PROCESS_STATUS} from './common.js'

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
            case PROCESS_STATUS.WAITING:
                caption.classList.add('bg-secondary');
                caption.textContent = PROCESS_STATUS.WAITING
                break;
            case PROCESS_STATUS.PROGRESS:
                caption.classList.add('bg-warning');
                caption.textContent = PROCESS_STATUS.PROGRESS
                break;
            case PROCESS_STATUS.COMPLETED:
                caption.classList.add('bg-success');
                caption.textContent = PROCESS_STATUS.COMPLETED
                break;
        }
    }
}

