// src/api/client.js
import axios from 'axios';

// En dev : VITE_API_URL = 'http://localhost:8888'
// En prod : VITE_API_URL = '' (même domaine que le backend)
const baseURL = import.meta.env.VITE_API_URL || '';

const api = axios.create({
  baseURL,
});

// Ajout automatique du token JWT s'il existe
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
