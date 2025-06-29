import { defineStore } from 'pinia'
import { searchProfileApi } from '../services/api/search'
import type { SearchProfile } from '../models'

interface SearchProfileState {
  searchProfiles: SearchProfile[]
  currentSearchProfile: SearchProfile | null
  loading: boolean
  error: string | null
}

export const useSearchProfileStore = defineStore('searchProfile', {
  state: (): SearchProfileState => ({
    searchProfiles: [],
    currentSearchProfile: null,
    loading: false,
    error: null
  }),

    getters: {
        getProjectById: (state) => {
        return (id: string) => state.searchProfiles.find(search => search.id === id)
        },
    },

  actions: {
    async getAllSearchProfiles() {
      this.loading = true
            try {
              const { data } = await searchProfileApi.getAll()
              this.searchProfiles = data
              return data
            } catch (error) {
              this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
              throw error
            } finally {
              this.loading = false
            }
    },
    async createSearchProfile(searchProfileData: SearchProfile) {
      try {
        const { data } = await searchProfileApi.create(searchProfileData)
        this.searchProfiles.push(data)
        return data
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      }
    }
  }
})