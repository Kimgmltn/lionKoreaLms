document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    const formData = new FormData(this);
    const loginData = {
        loginId: formData.get('loginId'),
        password: formData.get('password'),
        // 'remember-me': formData.get('remember-me') ? true : false
    };

    fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
        .then(response => {
            console.log(response)
            if (response.ok) {
                window.location.href = '/'; // 로그인 성공 시 리다이렉트
            } else {
                return response.json().then(data => {
                    // 실패 처리 (예: 메시지 표시)
                    alert('Login failed: ' + data.message);
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Login failed: An unexpected error occurred.');
        });
});