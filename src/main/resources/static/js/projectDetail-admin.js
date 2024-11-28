import {get} from './api.js'
import {getLastPath} from './common.js'

document.addEventListener('DOMContentLoaded', () => {
    renderProjectInfo()
} );

const renderProjectInfo = async () => {
    const projectId = getLastPath();
    const response = await get(`/api/projects/admin/${projectId}`)
    if(response.ok){
        const projectDetail = await response.json()
        document.getElementsById('inputBuyer').value = projectDetail.
    }
}