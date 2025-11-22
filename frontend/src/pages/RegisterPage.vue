<template>
  <div class="auth-container">
    <div class="auth-card shadow">
      <h3 class="text-center mb-4 text-success">📝 Inscription RH</h3>
      <form @submit.prevent="handleRegister">
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
        <div class="form-group mb-3">
          <label>Rôle</label>
          <select v-model="role" class="form-select" required>
            <option value="employe">Employé</option>
            <option value="admin">Admin</option>
          </select>
        </div>
        <button class="btn btn-success w-100" type="submit">S'inscrire</button>
        <p class="text-danger mt-2 text-center" v-if="error">{{ error }}</p>
        <p class="text-center mt-3">
          Déjà inscrit ?
          <router-link to="/login" class="text-success">Se connecter</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import { register } from '../api/auth'

export default {
  data() {
    return {
      email: '',
      password: '',
      role: 'employe',
      error: '',
    }
  },
  methods: {
    async handleRegister() {
      try {
        await register(this.email, this.password, this.role)
        alert('Inscription réussie !')
        this.$router.push('/login')
      } catch (err) {
        this.error = err.response?.data || "Erreur d'inscription"
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
