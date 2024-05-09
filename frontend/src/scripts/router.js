import AppHome from "@/pages/AppHome.vue";
import AppLogin from "@/pages/AppLogin.vue";
import AppCart from "@/pages/AppCart.vue"
import {createRouter, createWebHistory} from "vue-router";
import AppError from "@/pages/AppError.vue";

const routes = [
    {path: '/', component: AppHome},
    {path: '/login', component: AppLogin},
    {path: '/cart', component: AppCart},
    {path: '/error', component: AppError}
]

const  router= createRouter({
    history: createWebHistory(),
    routes
})

export default router;
