import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '../pages/LoginPage.vue'
import RegisterPage from '../pages/RegisterPage.vue'
import DashboardAdmin from '../pages/DashboardAdmin.vue'
import DashboardEmploye from '../pages/DashboardEmploye.vue'
import DemandeCongePage from '../pages/DemandeCongePage.vue' // ✅ ajouté

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: LoginPage },
  { path: '/register', name: 'Register', component: RegisterPage },
  { path: '/admin', name: 'AdminDashboard', component: DashboardAdmin },
  { path: '/employe', name: 'EmployeDashboard', component: DashboardEmploye },
  { path: '/demande-conge', name: 'DemandeConge', component: DemandeCongePage }, // ✅ ajouté
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
