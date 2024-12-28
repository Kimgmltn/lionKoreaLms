import { get } from './api.js';
import { renderPagination } from './common.js'

document.addEventListener('DOMContentLoaded', () => {
    /*const radioButtons = document.querySelectorAll('input[name="role"]');

    const selectedRadio = document.querySelector('input[name="role"]:checked');
    if (selectedRadio) {
        renderMembers(selectedRadio.value);
    }

    radioButtons.forEach(radio => {
        radio.addEventListener('change', function() {
            renderMembers(this.value);
        });
    });*/
    renderMembers({});
});

const renderMembers = async ({page = 0, size = 20}) => {
    const queryParams = new URLSearchParams({
        page:page,
        size:size
    })
    const datatablesSimple = document.getElementById('datatablesSimple');

    if (!datatablesSimple) {
        console.error('No datatablesSimple element found on the page.');
        return;
    }
    const response = await get(`/api/members?${queryParams.toString()}`);

    const membersData = await response.json()

    const tbody = datatablesSimple.querySelector('tbody');
    tbody.innerHTML = '';
    const pageInfo = membersData.page
    membersData.content.forEach((member, index) => {

        const tr = document.createElement('tr');
        tr.addEventListener('click',()=>{
            window.location.href = `/members/${member.memberId}`;
        })

        const indexTd = document.createElement('td');
        indexTd.innerText = pageInfo.totalElements - (pageInfo.number * pageInfo.size) - index
        tr.appendChild(indexTd)
        Object.values(member).forEach((value, index) => {
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
    renderPagination(dom, membersData.page, renderMembers);
}

document.getElementById('downloadExcelForm').addEventListener('click', async function () {
    const response = await get('/api/files/download/member');
    if(response.ok){
        const buffer = await response.arrayBuffer();
        downloadFile(buffer, 'input_member_form.xlsx')
    }
});

const downloadFile = (byteBuffer, fileName, mineType) => {
    const uint8Array = new Uint8Array(byteBuffer);
    const blob = new Blob([uint8Array], {type: mineType});

    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = fileName;
    document.body.appendChild(link);
    link.click();

    URL.revokeObjectURL(url);
    document.body.removeChild(link);
}
