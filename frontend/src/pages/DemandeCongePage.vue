<template>
  <div class="container mt-5">
    <div class="card p-4 shadow mx-auto" style="max-width: 600px">
      <h3 class="mb-4"><span class="me-2">📅</span>Demande de congé</h3>

      <div class="mb-3">
        <label class="form-label">Type de congé</label>
        <select class="form-select" v-model="typeConge">
          <option>Congé payé</option>
          <option>Congé maladie</option>
          <option>Congé exceptionnel</option>
        </select>
      </div>

      <div class="mb-3">
        <label class="form-label">Motif</label>
        <textarea
          v-model="motif"
          class="form-control"
          placeholder="congé pour..."
          rows="3"
        ></textarea>
      </div>

      <div class="mb-3">
        <label class="form-label">Date souhaitée</label>
        <input v-model="date" type="date" class="form-control" />
      </div>

      <button class="btn btn-success w-100" :disabled="loading" @click="envoyerDemande">
        {{ loading ? 'Envoi en cours...' : 'Envoyer la demande' }}
      </button>

      <p class="text-danger mt-3" v-if="erreur">{{ erreur }}</p>
      <p class="text-success mt-3" v-if="message">{{ message }}</p>
    </div>
  </div>
</template>

<script>
import api from '../api/client'

export default {
  name: 'DemandeCongePage',
  data() {
    return {
      typeConge: 'Congé payé',
      motif: '',
      date: '',
      message: '',
      erreur: '',
      loading: false,
    }
  },
  methods: {
    async envoyerDemande() {
      if (!this.typeConge || !this.motif || !this.date) {
        this.erreur = 'Champs requis : type, motif, date'
        this.message = ''
        return
      }

      this.loading = true
      this.erreur = ''
      this.message = ''

      const payload = {
        type: this.typeConge,
        motif: this.motif,
        date: this.date,
      }

      try {
        const res = await api.post('/api/demande', payload)

        this.message = res.data.message || '✅ Demande envoyée avec succès'
        this.motif = ''
        this.date = ''
      } catch (err) {
        this.erreur = err.response?.data || 'Erreur lors de l’envoi'
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
.container {
  padding-top: 2rem;
}
</style>
