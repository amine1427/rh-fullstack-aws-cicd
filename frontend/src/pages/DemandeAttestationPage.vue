<template>
  <div class="container mt-5">
    <div class="card p-4 shadow mx-auto" style="max-width: 600px">
      <h3 class="mb-4">📄 Demande d'attestation</h3>

      <div class="d-grid gap-3">
        <button class="btn btn-primary" @click="demanderAttestation('travail')">
          Attestation de travail
        </button>
        <button class="btn btn-secondary" @click="demanderAttestation('salaire')">
          Attestation de salaire
        </button>
      </div>

      <p class="text-success mt-4" v-if="message">{{ message }}</p>
      <p class="text-danger mt-4" v-if="erreur">{{ erreur }}</p>
    </div>
  </div>
</template>

<script>
import api from '../api/client'

export default {
  name: 'DemandeAttestationPage',
  data() {
    return {
      message: '',
      erreur: '',
    }
  },
  methods: {
    async demanderAttestation(type) {
      this.message = ''
      this.erreur = ''

      const endpoint =
        type === 'travail'
          ? '/api/demande/attestation-travail'
          : '/api/demande/attestation-salaire'

      try {
        const res = await api.post(endpoint, {})

        this.message = res.data.message || '✅ Demande envoyée'
      } catch (err) {
        this.erreur = err.response?.data || '❌ Erreur lors de la demande'
      }
    },
  },
}
</script>
