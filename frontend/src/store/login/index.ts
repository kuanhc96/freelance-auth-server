import { defineStore } from 'pinia';
import {LoginRequest} from "../../dto/request/loginRequest";
import router from "../../router/router";
import Cookies from "js-cookie";

export interface LoginState {
    userGUID: string
    email: string
    role: string
    expirationTimestamp: string
    didAutoLogout: boolean
}
let timer: number;

export const useLoginStore = defineStore('login', {
    state: (): LoginState => ({
        userGUID: localStorage.getItem('userGUID') || '',
        expirationTimestamp: localStorage.getItem('expirationTimestamp') || '0',
        email: '',
        role: '',
        didAutoLogout: false
    }),
    getters: {
        isLoggedIn: (state) => {
            return !!state.userGUID?.trim() && state.expirationTimestamp !== '0'
        },
        getUserGUID: state => state.userGUID,
        getRole: state => state.role,
        isStudent: state => state.role === 'STUDENT',
        getEmail: state => state.email,
        getDidAutoLogout: state => state.didAutoLogout
    },
    actions: {
        async login(loginRequest: LoginRequest): Promise<void> {
            const csrfToken = Cookies.get('XSRF-TOKEN');
            const response: Response = await fetch("/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    'X-XSRF-TOKEN': csrfToken
                },
                body: new URLSearchParams( {
                    username: loginRequest.email,
                    password: loginRequest.password,
                    role: loginRequest.role
                })
            })

            if (response.ok) {
                console.log('Login successful');
                console.log(response);
                // const data: LoginResponse = await response.json();
                // console.log(data);
                // if (data) {
                    // this.userGUID = data.userGUID;
                    // localStorage.setItem('userGUID', data.userGUID);
                    // this.email = data.email;
                    // this.role = data.role;
                    // localStorage.setItem('expirationTimestamp', data.expirationTimestamp.toString());
                    // this.expirationTimestamp = data.expirationTimestamp.toString();
                    // timer = setTimeout(async () => {
                    //     await this.autoLogout();
                    // }, data.expirationTimestamp - new Date().getTime());
                    // await router.replace('/hello');
                // }
            } else {
                console.log(response);
            }
        },
        async autoLogout() {
            this.didAutoLogout = true;
            console.log('auto logout');
        }
    }
})
