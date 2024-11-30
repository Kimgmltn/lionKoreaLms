import {get} from './api.js'
import {renderPagination} from "./common";


document.addEventListener('DOMContentLoaded', () => {
    let today = new Date().toISOString().split('T')[0];
    $( "#datepicker" ).datepicker({
        dateFormat: 'yy-mm-dd',
        defaultDate: today,
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
        changeMonth: true,
        changeYear: true,
        // onSelect: function(dateText){
        //     sendDateToApi(dateText);
        // }
    }).datepicker('setDate', today);
    renderProjects({dateString:today})
} );

$('#datepicker').on('change', function(){
    const selectDate = $(this).val();
    renderProjects({dateString:selectDate})
})

const renderProjects = async ({page = 0, size = 20, dateString}) => {
    const queryParams = new URLSearchParams({
        page:page,
        size:size,
        dateString
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

        Object.values(project).forEach((value, index) => {
            const td = document.createElement('td');
            td.textContent = value;

            if(index === 0){
                td.hidden = true;
            }
            tr.appendChild(td);
        })

        tbody.appendChild(tr);
    });

    const dom = document.getElementById('paginationContainer');
    renderPagination(dom, projects.page, renderProjects, {size, dateString});
}