<template>
    <section>
        <BaseCard class="login-form" card-title="Login">
            <form class="login-form" action="/login" method="post">
                <div class="mb-3">
                    <label class="form-label" for="email">User Email </label>
                    <input class="form-control" name="email" type="email" id="email" v-model="email" required/>
                </div>
                <div class="mb-3">
                    <label class="form-label" for="password">Password </label>
                    <input class="form-control" name="password" type="password" id="password" v-model="password" required/>
                </div>
                <div class="mb-3">
                    <label class="form-label">Login as...</label>
                    <div class="form-check">
                        <input
                            class="form-check-input "
                            type="radio"
                            name="role"
                            id="student"
                            value="STUDENT"
                            v-model="selectedRole"
                            checked
                        />
                        <label for="student">Student</label>
                    </div>
                    <div class="form-check">
                        <input
                            class="form-check-input"
                            type="radio"
                            name="role"
                            id="instructor"
                            value="INSTRUCTOR"
                            v-model="selectedRole"
                        />
                        <label for="instructor">Instructor</label>
                    </div>
                </div>
                <div class="d-grid vstack mb-3">
                    <button class="btn btn-secondary" type="submit">Login</button>
                </div>
            </form>
            <div>
                <router-link class="btn btn-primary me-3" to="/createAccount">Create an account</router-link>
                <router-link class="btn btn-primary" to="/forgetPassword">Forgot password?</router-link>
            </div>
            <!--        Toast -->
            <div class="toast-container position-fixed bottom-0 end-0 p-3">
                <div class="toast" id="myToast">
                    <div class="toast-header text-bg-light">
                        <div class="me-auto">Auto Logged Out!</div>
                        <button class="btn btn-close" data-bs-dismiss="toast" type="button" id="toastBtn"></button>
                    </div>
                    <div class="toast-body text-bg-light">
                        Please Log In Again!
                    </div>
                </div>
            </div>
        </BaseCard>
    </section>
</template>

<script lang="ts">
import BaseCard from "../components/ui/BaseCard.vue";
import {onMounted, Ref, ref} from "vue";
import {useRouter, useRoute} from 'vue-router';
import {LoginRequest} from "../dto/request/loginRequest";
import Toast from 'bootstrap/js/dist/toast';
import {useLoginStore} from "../store/login";

export default {
    components: {
        BaseCard
    },
    setup() {
        const loginStore = useLoginStore();
        const router = useRouter();
        const email: Ref<string> = ref('');
        const password: Ref<string> = ref('');
        const selectedRole: Ref<string> = ref('STUDENT');

        function refresh() {
            email.value = '';
            password.value = '';
            selectedRole.value = 'STUDENT';
            router.replace("/login");
        }

        async function submitForm() {
            // const loginRequest: LoginRequest = {
            //     email: email.value,
            //     password: password.value,
            //     role: selectedRole.value
            // }
            // await loginStore.login(loginRequest);
        }

        onMounted(() => {
            const route = useRoute();
            const autoLoggedOut = route.query.autoLogOut;
            const toast: Element | null = document.querySelector('#myToast');
            if (autoLoggedOut !== undefined) {
                const toastBootstrap = Toast.getOrCreateInstance(toast!);
                toastBootstrap.show();
                const toastTrigger = document.querySelector('#toastBtn');
                toastTrigger?.addEventListener('click', () => {
                    toastBootstrap.hide();
                })
            }

        })

        return {
            email,
            password,
            selectedRole,
            refresh,
            submitForm
        }
    }
}
</script>
