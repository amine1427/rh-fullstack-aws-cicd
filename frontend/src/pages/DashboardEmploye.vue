<template>
  <div class="d-flex min-vh-100">
    <!-- Sidebar -->
    <div class="bg-success text-white p-3" style="width: 250px">
      <h5 class="text-center mb-4">RH Employé</h5>
      <ul class="nav flex-column">
        <li class="nav-item mb-2">
          <a class="nav-link text-white" href="#">🏠 Tableau de bord</a>
        </li>
        <li class="nav-item mb-2"><a class="nav-link text-white" href="#">📄 Mes demandes</a></li>
        <li class="nav-item mb-2"><a class="nav-link text-white" href="#">🕑 Historique</a></li>
        <li class="nav-item mb-2"><a class="nav-link text-white" href="#">📥 Documents</a></li>
        <li class="nav-item mt-5">
          <button class="btn btn-outline-light w-100" @click="logout">🚪 Déconnexion</button>
        </li>
      </ul>
    </div>

    <!-- Main content -->
    <div class="flex-grow-1 p-4">
      <h3 class="mb-4">Bonjour, {{ userEmail }}</h3>

      <div class="row g-4">
        <div class="col-md-4" v-for="(item, index) in cards" :key="index">
          <div class="card h-100 shadow">
            <div class="card-body text-center">
              <div class="display-6 mb-3">{{ item.icon }}</div>
              <h5 class="card-title">{{ item.title }}</h5>
              <p class="card-text">{{ item.description }}</p>
              <button class="btn btn-success w-100" @click="handleCardClick(index)">Gérer</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userEmail: localStorage.getItem('userEmail') || 'Employé',
      cards: [
        {
          icon: '📅',
          title: 'Demande de congé',
          description: 'Faire une demande de congé payé ou maladie.',
        },
        {
          icon: '🕒',
          title: "Autorisation d'absence",
          description: "Demander une autorisation d'absence exceptionnelle.",
        },
        {
          icon: '💻',
          title: 'Demande de télétravail',
          description: 'Soumettre une demande de travail à distance.',
        },
        {
          icon: '📄',
          title: 'Attestation de travail',
          description: 'Demander une attestation de travail ou de salaire.',
        },
        {
          icon: '📜',
          title: 'Historique des demandes',
          description: 'Consulter vos demandes passées avec leurs statuts.',
        },
        {
          icon: '📁',
          title: 'Documents',
          description: 'Télécharger vos documents RH personnels.',
        },
      ],
    }
  },
  methods: {
    logout() {
      localStorage.clear()
      this.$router.push('/login')
    },
    handleCardClick(index) {
      if (index === 0) {
        // Redirection vers /demande-conge pour la 1ère carte uniquement
        this.$router.push('/demande-conge')
      } else {
        alert('Fonctionnalité à venir...')
      }
    },
  },
}
</script>

<style scoped>
body {
  background-color: #f8f9fa;
}
.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
</style>
