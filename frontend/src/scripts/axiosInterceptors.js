import axios from 'axios';
import router from "@/scripts/router";

axios.interceptors.response.use(
    response => response, // 정상 응답 처리
    error => {
        if (error.response && error.response.status === 500) {
            // 500 에러 처리
            console.error('Server error (500):', error.response);
            router.push('/error');
            // 여기서 에러 처리를 마치고, 더 이상 전파하지 않음
            return;
        }
        // 500 에러가 아니라면, 에러를 다음 처리 단계로 전파
        return Promise.reject(error);
    }
);
