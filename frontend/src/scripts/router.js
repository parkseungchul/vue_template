import AppHome from "@/pages/AppHome.vue";
import AppLogin from "@/pages/AppLogin.vue";
import {createRouter, createWebHistory} from "vue-router";

const routes = [
    {path: '/', component: AppHome},
    {path: '/login', component: AppLogin}
]

const  router= createRouter({
    history: createWebHistory(),
    routes
})

export default router;
