import {createApp} from 'vue'
import App from './App.vue'
import store from '@/scripts/store'
import './assets/js/jquery-3.2.1.slim.min'
import './assets/js/popper.min'
import './assets/js/holder.min.js'
import './assets/css/album.css'
import {createRouter, createWebHistory} from "vue-router"
import AppHome from "@/pages/AppHome.vue";
import AppLogin from "@/pages/AppLogin.vue";

const routes = [
    {path: '/', component: AppHome},
    {path: '/login', component: AppLogin}
]

const  router= createRouter({
    history: createWebHistory(),
    routes
})

createApp(App).use(store).use(router).mount('#app')
