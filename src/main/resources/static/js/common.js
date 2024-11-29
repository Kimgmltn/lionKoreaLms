const createConfirmModal = (options, ...callbacks) => {
    // 모달을 감싸는 div 생성
    const modalDiv = document.createElement('div');
    modalDiv.className = 'modal fade show';
    modalDiv.tabIndex = -1;
    modalDiv.role = 'dialog';
    modalDiv.id = options.id || 'modalChoice';
    modalDiv.style.display = 'block';

    // 모달 다이얼로그 div 생성
    const modalDialogDiv = document.createElement('div');
    modalDialogDiv.className = 'modal-dialog modal-dialog-centered';
    modalDialogDiv.role = 'document';

    // 모달 내용 div 생성
    const modalContentDiv = document.createElement('div');
    modalContentDiv.className = 'modal-content rounded-3 shadow';

    // 모달 바디 div 생성
    const modalBodyDiv = document.createElement('div');
    modalBodyDiv.className = 'modal-body p-4 text-center';

    // 모달 바디의 제목 h5 생성
    const modalTitle = document.createElement('h5');
    modalTitle.className = 'mb-0';
    modalTitle.textContent = options.title || '뭔가 잘 못 되었는지요?';

    // 모달 바디의 설명 p 생성
    // const modalText = document.createElement('p');
    // modalText.className = 'mb-0';
    // modalText.textContent = options.text || 'You can always change your mind in your account settings.';

    // 모달 바디 div에 제목과 설명 추가
    modalBodyDiv.appendChild(modalTitle);
    // modalBodyDiv.appendChild(modalText);

    // 모달 푸터 div 생성
    const modalFooterDiv = document.createElement('div');
    modalFooterDiv.className = 'modal-footer flex-nowrap p-0 justify-content-center';

    // "Yes, enable" 버튼 생성
    const yesButton = document.createElement('button');
    yesButton.type = 'button';
    yesButton.className = 'btn btn-lg btn-link fs-6 text-decoration-none col-6 py-3 m-0 rounded-0';
    yesButton.innerHTML = '<strong>' + (options.yesText || '확인') + '</strong>';

    // "No thanks" 버튼 생성
    // const noButton = document.createElement('button');
    // noButton.type = 'button';
    // noButton.className = 'btn btn-lg btn-link fs-6 text-decoration-none col-6 py-3 m-0 rounded-0';
    // noButton.setAttribute('data-bs-dismiss', 'modal');
    // noButton.textContent = options.noText || 'No thanks';

    // 모달 푸터 div에 버튼 추가
    modalFooterDiv.appendChild(yesButton);
    // modalFooterDiv.appendChild(noButton);

    // 모달 내용 div에 바디와 푸터 추가
    modalContentDiv.appendChild(modalBodyDiv);
    modalContentDiv.appendChild(modalFooterDiv);

    // 모달 다이얼로그 div에 모달 내용 추가
    modalDialogDiv.appendChild(modalContentDiv);

    // 모달을 감싸는 div에 모달 다이얼로그 추가
    modalDiv.appendChild(modalDialogDiv);

    // 최종적으로 body에 모달을 추가
    document.body.appendChild(modalDiv);

    // 모달을 표시
    const myModal = new bootstrap.Modal(modalDiv, {
        backdrop: 'static', // Optional: prevents closing the modal by clicking outside of it
        keyboard: false // Optional: prevents closing the modal by pressing the escape key
    });

    // "Yes" 버튼 클릭 시 콜백 함수 호출
    yesButton.addEventListener('click', function() {
        callbacks.forEach(callback => {
            if (callback && typeof callback == 'function') {
                callback();
            }
        })
        myModal.hide(); // 모달 닫기
    });

    // 모달이 닫히면 DOM에서 모달 요소 제거
    modalDiv.addEventListener('hidden.bs.modal', function () {
        modalDiv.remove();
    });

    myModal.show();

    document.body.classList.add('modal-open');
    document.body.style.overflowY = 'scroll';
    document.body.style.paddingRight = '0';
}

const inputOnlyNumber = (e) => {
    let numbers = e.target.value.replace(/\D/g, '');

    // 최대 11자리까지만 남기기
    if (numbers.length > 11) {
        numbers = numbers.slice(0, 11);
    }

    // 입력한 숫자에 맞춰 포맷팅
    let formattedNumber = '';
    if (numbers.length <= 3) {
        formattedNumber = numbers;
    } else if (numbers.length <= 7) {
        formattedNumber = numbers.slice(0, 3) + '-' + numbers.slice(3);
    } else if (numbers.length <= 10) {
        formattedNumber = numbers.slice(0, 3) + '-' + numbers.slice(3, 6) + '-' + numbers.slice(6);
    } else {
        formattedNumber = numbers.slice(0, 3) + '-' + numbers.slice(3, 7) + '-' + numbers.slice(7);
    }

    // 입력란에 포맷팅된 값을 넣어줌
    e.target.value = formattedNumber;
}

const renderPagination = (dom, pageData, renderFunction, searchParam = {}) => {
    const paginationContainer = dom;

    if (!paginationContainer) {
        console.error('No paginationContainer element found on the page.');
        return;
    }

    paginationContainer.innerHTML = ''; // Clear existing pagination

    const totalPages = pageData.totalPages;
    const currentPage = pageData.number;
    const maxVisibleButtons = 10; // Maximum number of buttons to show

    const startPage = Math.floor(currentPage / maxVisibleButtons) * maxVisibleButtons;
    const endPage = Math.min(startPage + maxVisibleButtons, totalPages);

    // Previous button
    const prevButton = document.createElement('li');
    prevButton.classList.add('page-item')
    if(currentPage === 0){
        prevButton.classList.add('disabled')
    }
    const prevLink = document.createElement('a');
    prevLink.classList.add('page-link');
    prevLink.href = '#';
    prevLink.textContent = 'Previous';
    prevLink.addEventListener('click', () => {
        if (currentPage > 0) renderFunction({page:currentPage -1, ...searchParam});
    });
    prevButton.appendChild(prevLink);
    paginationContainer.appendChild(prevButton);

    // Page buttons
    for (let i = startPage; i < endPage; i++) {
        const pageButton = document.createElement('li');
        pageButton.classList.add('page-item');
        if(i === currentPage) {
            pageButton.classList.add('active')
        }
        const pageLink = document.createElement('a');
        pageLink.classList.add('page-link');
        pageLink.href = '#';
        pageLink.textContent = i + 1;
        pageLink.addEventListener('click', () => {
            renderFunction({page:i, ...searchParam});
        });
        pageButton.appendChild(pageLink);
        paginationContainer.appendChild(pageButton);
    }

    // Next button
    const nextButton = document.createElement('li');
    nextButton.classList.add('page-item');
    if(currentPage === totalPages - 1){
        nextButton.classList.add('disabled');
    }
    const nextLink = document.createElement('a');
    nextLink.classList.add('page-link');
    nextLink.href = '#';
    nextLink.textContent = 'Next';
    nextLink.addEventListener('click', () => {
        if (currentPage < totalPages - 1) renderFunction({page:currentPage +1, ...searchParam});
    });
    nextButton.appendChild(nextLink);
    paginationContainer.appendChild(nextButton);
}

const parseJWT = (token) => {
    try {
        // 토큰의 각 부분 분리 (헤더, 페이로드, 서명)
        const [header, payload, signature] = token.split('.');

        // Base64 디코딩
        const decodeBase64 = (str) => JSON.parse(decodeURIComponent(atob(str).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join('')));

        // 각 부분을 JSON으로 파싱
        const headerJSON = decodeBase64(header);
        const payloadJSON = decodeBase64(payload);

        return {
            header: headerJSON,
            payload: payloadJSON,
            signature: signature  // 서명은 그대로 유지
        };
    } catch (error) {
        console.error("Invalid JWT token:", error);
        return null;
    }
}

const getLastPath = () => {
    const pathname = window.location.pathname;
    return pathname.substring(pathname.lastIndexOf('/') + 1);
}

const PROCESS_STATUS = Object.freeze({
    WAITING:'대기',
    PROGRESS:'작성 중',
    COMPLETED:'작성 완료'
})

export {createConfirmModal, inputOnlyNumber, renderPagination, parseJWT, getLastPath, PROCESS_STATUS}