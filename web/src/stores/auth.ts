import { defineStore } from 'pinia'
import KeyCloakService from '../services/keycloak'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    username: '',
    token: '',
    role: '',
    isReady: false
  }),
  actions: {
    async initializeAuth() {
      await KeyCloakService.Init()
      this.username = KeyCloakService.GetUserName() || ''
      this.token = KeyCloakService.GetAccessToken() || ''
      const roles = KeyCloakService.GetRoles() || []
      console.log('set role ', roles)
      this.role = roles.includes('HR') ? 'hr' : this.role
      this.role = roles.includes('MANAGER') ? 'manager' : this.role
    },
    login() {
      KeyCloakService.Login()
    },
    logout() {
      KeyCloakService.CallLogOut()
    },
    refreshToken() {
      KeyCloakService.UpdateToken(() => {
        this.token = KeyCloakService.GetAccessToken() || ''
      })
    }
  }
})
