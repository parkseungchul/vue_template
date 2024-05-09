import {createApp} from 'vue'
import store from '@/scripts/store'
import router from "@/scripts/router";
import App from './App.vue'
import '@/scripts/axiosInterceptors'; // 인터셉터 설정 추가

const isAuthenticated = sessionStorage.getItem('isAuthenticated') === 'true';
const userId = sessionStorage.getItem('userId') || '0';

store.commit('setUser', {isAuthenticated, userId});

createApp(App).use(store).use(router).mount('#app')
