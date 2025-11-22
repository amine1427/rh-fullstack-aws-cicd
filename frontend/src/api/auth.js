// src/api/auth.js
import api from './client';

export function login(email, password) {
  return api.post('/api/login', { email, password });
}

export function register(email, password, role) {
  return api.post('/api/register', { email, password, role });
}
