import {post} from "./api.js";
import {createConfirmModal} from './common.js'

document.addEventListener('DOMContentLoaded', () => {
    let today = new Date().toISOString().split('T')[0];
    $( "#datepicker" ).datepicker({
        dateFormat: 'yy-mm-dd',
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
        changeMonth: true,
        changeYear: true,
        // onSelect:
    });
} );

document.getElementById('registerForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    const formData = new FormData(this);
    const projectData = {
        buyerId: formData.get(''),
        domesticCompanyId: formData.get(''),
        projectName: formData.get(''),
        translatorId: formData.get(''),
        language: formData.get(''),
        consultationDate: formData.get(''),
        hour: formData.get(''),
        minute: formData.get(''),
        timePeriod: formData.get(''),
        consultationNotes: formData.get(''),
    };

    try {
        const response = await post('/api/projects/admin/save', projectData);
        const data = await response.json();

        await createConfirmModal({
            title: data.result
        }, function(){
            window.location.href=`/projects/${data.projectId}`
        });

    } catch (error) {
        console.error('Error:', error);

        // 실패 처리 (예: 메시지 표시)
        alert('등록 실패: ' + error.message);
    }
});


document.getElementById('inputConsultationNotes').addEventListener('input', function (){
    this.style.height = 'auto';
    this.style.height = this.scrollHeight + 'px';
})