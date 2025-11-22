<template>
  <div class="auth-container">
    <div class="auth-card shadow">
      <h3 class="text-center mb-4 text-success">🔐 Connexion RH</h3>
      <form @submit.prevent="handleLogin">
        <div class="form-group mb-3">
          <label>Email</label>
          <input
            v-model="email"
            type="email"
            class="form-control"
            placeholder="exemple@mail.com"
            required
          />
        </div>
        <div class="form-group mb-3">
          <label>Mot de passe</label>
          <input
            v-model="password"
            type="password"
            class="form-control"
            placeholder="••••••••"
            required
          />
        </div>
        <button class="btn btn-success w-100" type="submit">Se connecter</button>
        <p class="text-danger mt-2 text-center" v-if="error">{{ error }}</p>
        <p class="text-center mt-3">
          Vous n'êtes pas encore inscrit ?
          <router-link to="/register" class="text-success">S'inscrire</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import { login } from '../api/auth'

export default {
  data() {
    return {
      email: '',
      password: '',
      error: '',
    }
  },
  methods: {
    async handleLogin() {
      try {
        const res = await login(this.email, this.password)
        const token = res.data.token
        const payload = JSON.parse(atob(token.split('.')[1]))
        const role = payload.role
        const email = payload.email

        localStorage.setItem('token', token)
        localStorage.setItem('role', role)
        localStorage.setItem('employeId', email)

        alert('Connexion réussie !')
        this.$router.push(role === 'admin' ? '/admin' : '/employe')
      } catch (err) {
        this.error = err.response?.data || 'Identifiants incorrects'
      }
    },
  },
}
</script>

<style scoped>
.auth-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f2fef6;
}
.auth-card {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  width: 100%;
  max-width: 400px;
}
</style>
