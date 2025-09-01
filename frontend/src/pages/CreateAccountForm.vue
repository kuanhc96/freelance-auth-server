<script lang="ts">
import { Ref, ref } from 'vue';
import { useRouter} from "vue-router";
import BaseCard from "../components/ui/BaseCard.vue";

export default {
  components: {
    BaseCard
  },
  setup() {
    const router = useRouter();
    const email: Ref<string> = ref('');
    const name: Ref<string> = ref('');
    const password: Ref<string> = ref('');
    const dateInput: Ref<string> = ref('');
    const retypePassword: Ref<string> = ref('');
    const selectedRole: Ref<string> = ref('');
    const selectedGender: Ref<string> = ref('');
    const description: Ref<string> = ref('');

    async function submitForm() {
      if (password.value === retypePassword.value) {

        const response: Response = await fetch('/api/createUser', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            name: name.value,
            role: selectedRole.value,
            email: email.value,
            password: password.value,
            birthday: dateInput.value,
            gender: selectedGender.value,
            description: description.value
          }),
        });
        if (response.ok) {
          await router.push('/login');
        } else {
          alert('an error occurred when creating an account')
        }
      } else {
        alert('password and retype password must match');
      }

    }

    return {
      email,
      name,
      password,
      dateInput,
      retypePassword,
      selectedRole,
      selectedGender,
      description,
      submitForm
    }
  },
};

</script>

<template>
<section>
  <BaseCard card-title="Create Account">
    <form>
      <div class="mb-3">
        <label class="form-label" for="email">Email</label>
        <input class="form-control" type="email" id="email" v-model="email" required/>
      </div>
      <div class="mb-3">
        <label class="form-label" for="name">Name</label>
        <input class="form-control" type="text" id="name" v-model="name" required/>
      </div>
      <div class="mb-3">
        <label class="form-label" for="password">Password</label>
        <input class="form-control" type="password" id="password" v-model="password" required/>
      </div>
      <div class="mb-3">
        <label class="form-label" for="retype-password">Retype Password</label>
        <input class="form-control" type="password" id="retype-password" v-model="retypePassword" required/>
      </div>
      <div class="mb-3">
        <label for="dateInput" class="form-label">Birthday</label>
        <input type="date" value="1996-01-01" class="form-control" id="dateInput" v-model="dateInput">
      </div>
      <div class="mb-3">
        <label class="form-label">Gender</label>
        <div class="form-check">
          <input
              class="form-check-input"
              type="radio"
              name="gender"
              id="male"
              value="MALE"
              v-model="selectedGender"
          />
          <label class="form-check-label" for="male">Male</label>
        </div>
        <div class="form-check">
          <input
              class="form-check-input"
              type="radio"
              name="gender"
              id="female"
              value="FEMALE"
              v-model="selectedGender"
          />
          <label class="form-check-label" for="female">Female</label>
        </div>
      </div>
      <div class="mb-3">
        <label class="form-label">Register as...</label>
        <div class="form-check">
          <input
              class="form-check-input"
              type="radio"
              id="student"
              value="STUDENT"
              v-model="selectedRole"
          />
          <label for="student">Student</label>
        </div>
        <div class="form-check">
          <input
              class="form-check-input"
              type="radio"
              id="instructor"
              value="INSTRUCTOR"
              v-model="selectedRole"
          />
          <label for="instructor">Instructor</label>
        </div>
      </div>
      <div class="mb-3">
        <label class="form-label" for="description">Description</label>
        <textarea class="form-control" placeholder="Short Bio" id="description" rows="6"
                  v-model="description"></textarea>
      </div>
      <div class="mb-3">
        <button class="btn btn-secondary" type="submit">Create Account</button>
      </div>
    </form>
    <div>
      <router-link class="btn btn-primary" to="/login">Login</router-link>
    </div>

  </BaseCard>
</section>
</template>
