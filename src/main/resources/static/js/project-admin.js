import {get} from './api.js'
import {PROCESS_STATUS, renderPagination} from "./common.js";


document.addEventListener('DOMContentLoaded', () => {
    let today = new Date().toISOString().split('T')[0];
    console.log(today)
    $( "#datepicker" ).datepicker({
        dateFormat: 'yy-mm-dd',
        defaultDate: today,
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
        changeMonth: true,
        changeYear: true,
    }).datepicker('setDate', today);
    renderProjects({consultationDate:today})
} );

// $('#datepicker').on('change', function(){
//     const selectDate = $(this).val();
//     renderProjects({consultationDate:selectDate})
// })

document.getElementById('searchCondition').addEventListener('click', function (){
    const consultationDate = document.getElementById('datepicker').value;
    const buyerName = document.getElementById('buyerName').value;
    const domesticCompanyName = document.getElementById('domesticCompanyName').value;
    const translatorName = document.getElementById('translatorName').value;
    renderProjects({consultationDate:consultationDate, buyerName:buyerName, domesticCompanyName:domesticCompanyName, translatorName:translatorName})
})


const renderProjects = async ({page = 0, size = 20, consultationDate, buyerName, domesticCompanyName, translatorName}) => {
    const queryParams = new URLSearchParams({
        page:page,
        size:size,
        ...(consultationDate && {consultationDate}),
        ...(buyerName && {buyerName}),
        ...(domesticCompanyName && {domesticCompanyName}),
        ...(translatorName && {translatorName})
    })

    const datatablesSimple = document.getElementById('datatablesSimple');
    if (!datatablesSimple) {
        console.error('No datatablesSimple element found on the page.');
        return;
    }

    const response = await get(`/api/projects/admin?${queryParams.toString()}`);

    const projects = await response.json()

    const tbody = datatablesSimple.querySelector('tbody');
    tbody.innerHTML = '';
    projects.content.forEach((project) => {

        const tr = document.createElement('tr');
        tr.addEventListener('click',()=>{
            window.location.href = `/projects/admin/${project.projectId}`;
        })

        const projectIdTd = document.createElement('td')
        projectIdTd.hidden = true
        projectIdTd.innerHTML = project.projectId
        const projectNameTd = document.createElement('td')
        projectNameTd.innerHTML = project.projectName
        const buyerNameTd = document.createElement('td')
        buyerNameTd.innerHTML = project.buyerName
        const domesticCompanyNameTd = document.createElement('td')
        domesticCompanyNameTd.innerHTML = project.domesticCompanyName
        const translatorNameTd = document.createElement('td')
        translatorNameTd.innerHTML = project.translatorName
        const languageTd = document.createElement('td')
        languageTd.innerHTML = project.language
        const timeTd = document.createElement('td')
        timeTd.innerHTML = `${project.hour}:${project.minute} ${project.timePeriod}`
        const processStatusTd = document.createElement('td')
        const processStatusSpan = document.createElement('span')
        switch(project.processStatus){
            case "WAITING":
                processStatusSpan.classList= 'badge bg-secondary';
                processStatusSpan.textContent = PROCESS_STATUS.WAITING
                break;
            case "PROGRESS":
                processStatusSpan.classList= 'badge bg-warning';
                processStatusSpan.textContent = PROCESS_STATUS.PROGRESS
                break;
            case "COMPLETED":
                processStatusSpan.classList= 'badge bg-success';
                processStatusSpan.textContent = PROCESS_STATUS.COMPLETED
                break;
            case "REJECT":
                processStatusSpan.classList= 'badge bg-danger';
                processStatusSpan.textContent = PROCESS_STATUS.REJECT
                break;
        }
        processStatusTd.appendChild(processStatusSpan)

        tr.appendChild(projectIdTd)
        tr.appendChild(projectNameTd)
        tr.appendChild(buyerNameTd)
        tr.appendChild(domesticCompanyNameTd)
        tr.appendChild(translatorNameTd)
        tr.appendChild(languageTd)
        tr.appendChild(timeTd)
        tr.appendChild(processStatusTd)

        tbody.appendChild(tr);
    });

    const dom = document.getElementById('paginationContainer');
    renderPagination(dom, projects.page, renderProjects, {size, consultationDate, buyerName, domesticCompanyName, translatorName});
}