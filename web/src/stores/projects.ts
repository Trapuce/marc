import { defineStore } from 'pinia'
import { projectApi } from '../services/api/projects'
import type { Project } from '../models'

interface ProjectState {
  projects: Project[],
  totalItems: number,
  currentProject: Project | null
  loading: boolean
  error: string | null
}

export const useProjectStore = defineStore('project', {
  state: (): ProjectState => ({
    projects: [],
    totalItems: 0,
    currentProject: null,
    loading: false,
    error: null
  }),

  getters: {
      getProjectById: (state) => {
        return (id: string) => state.projects.find(project => project.id === id)
      },
  },

  actions: {
    async fetchProjects(archived: boolean) {
      console.log(archived);
      this.loading = true
      try {
        const { data } = await projectApi.getAll(archived);
        this.projects = data.items
        this.totalItems = data.totalItems
      } finally {
        this.loading = false
      }
    },

    async fetchProjectById(id: string) {
      this.loading = true
      try {
        const { data } = await projectApi.getById(id)
        this.currentProject = data
        return data
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      } finally {
        this.loading = false
      }
    },

    async createProject(projectData: Project) {
      this.loading = true
      try {
        const { data } = await projectApi.create(projectData)
        this.projects.push(data)
        return data
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateProject(id: string, projectData: Project) {
      this.loading = true
      try {
        const { data } = await projectApi.update(id, projectData)
        const index = this.projects.findIndex(proj => proj.id === id)
        if (index !== -1) {
          this.projects[index] = data
        }
        return data
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      } finally {
        this.loading = false
      }
    },

    async addEmployees(id: string, employees: string[]) {
      this.loading = true
      try {
        const project = this.projects.find(proj => proj.id === id)
        const projectWithNewEmployees = {
          ...project,
          employeesIds: [...project.employeesIds, ...employees]
        }
        const { data } = await projectApi.update(id, projectWithNewEmployees)
        const index = this.projects.findIndex(proj => proj.id === id)
        if (index !== -1) {
          this.projects[index] = data
        }
        return data
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      } finally {
        this.loading = false
      }
    },

    async archiveProject(id: string) {
      this.loading = true
      try {
        const { data } = await projectApi.archive(id)
        const index = this.projects.findIndex(proj => proj.id === id)
        if (index !== -1) {
          this.projects.splice(index, 1)
        }
        return data
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      } finally {
        this.loading = false
      }
    }

  }
})