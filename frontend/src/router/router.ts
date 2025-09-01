// router.js
import { createRouter, createWebHistory, RouteRecordRaw, Router } from 'vue-router';
import LoginForm from '../pages/LoginForm.vue';
import HelloWorld from "../components/HelloWorld.vue";
// import CreateAccountForm from '../pages/login/CreateAccountForm.vue';
// import ForgetPasswordForm from '../pages/login/ForgetPasswordForm.vue';
// import ResetPasswordForm from '../pages/login/ResetPasswordForm.vue';


const routes: RouteRecordRaw[] = [
    // paths that can only be accessed if logged out
  { path: '/login', component: LoginForm, meta: { requiresLogout: true }},
  { path: '/login?logout=true', component: LoginForm, meta: { requiresLogout: true }},
  { path: '/hello', component: HelloWorld },


    // paths that don't need to be protected
  // { path: '/createAccount', component: CreateAccountForm },
  // { path: '/forgetPassword', component: ForgetPasswordForm },
  // { path: '/resetPassword', component: ResetPasswordForm },
  // { path: '/:notFound(.*)', redirect: '/notFound' },
  // { path: '/notFound', component: NotFound },

    // paths that can only be accessed if logged in
];

const router: Router = createRouter({
  history: createWebHistory(),
  routes,
});


export default router;
