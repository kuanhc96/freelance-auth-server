import { createApp } from 'vue'
import App from './App.vue'
import router from "./router/router.js";
import BaseCard from "./components/ui/BaseCard.vue";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import './assets/scss/styles.scss';
import './assets/scss/bootstrap.scss';
import pinia from './store/index';

const app = createApp(App);
app.use(router);
app.use(pinia)

app.component('base-card', BaseCard);
app.mount('#app')
