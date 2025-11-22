<template>
  <form @submit.prevent="handleRegister">
    <div class="input-group">
      <label>Email</label>
      <input v-model="email" type="email" required />
    </div>
    <div class="input-group">
      <label>Mot de passe</label>
      <input v-model="password" type="password" required />
    </div>
    <div class="input-group">
      <label>Rôle</label>
      <select v-model="role" required>
        <option value="employe">Employé</option>
        <option value="admin">Admin</option>
      </select>
    </div>
    <button type="submit">S'inscrire</button>
    <p v-if="error" class="error">{{ error }}</p>
  </form>
</template>

<script>
import { register } from '../api/auth'
export default {
  data() {
    return { email: '', password: '', role: 'employe', error: '' }
  },
  methods: {
    async handleRegister() {
      try {
        await register(this.email, this.password, this.role)
        this.$emit('switch-tab', 'login')
      } catch (err) {
        this.error = err.response?.data || "Erreur d'inscription"
      }
    },
  },
}
</script>
