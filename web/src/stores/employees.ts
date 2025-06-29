import { defineStore } from 'pinia'
import { employeeApi } from '../services/api/employees'
import type { Employee, Skill } from '../models'

interface EmployeeState {
  employees: Employee[]
  currentEmployee: Employee | null
  loading: boolean
  error: string | null
  skills: Skill[]
}

export const useEmployeeStore = defineStore('employee', {
  state: (): EmployeeState => ({
    employees: [],
    currentEmployee: null,
    loading: false,
    error: null,
    skills: []
  }),
  
  getters: {
    getEmployeeById: (state) => {
      return (id: string) => state.employees.find(employee => employee.id === id)
    },
  },
  
  actions: {

    async fetchEmployees(firstName?: string, lastName?: string, skills?: string[], experience?: number, from?:string, to?: string) {
      this.loading = true
      console.log(firstName, lastName, skills, experience, from, to)
      try {
        const { data } = await employeeApi.getAll(firstName, lastName, skills, experience, from, to)
        this.employees = data.items
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      } finally {
        this.loading = false
      }
    },

    async fetchEmployeeById(id: string) {
      this.loading = true
      try {
        const { data } = await employeeApi.getById(id)
        this.currentEmployee = data
        return data
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      } finally {
        this.loading = false
      }
    },

    async createEmployee(employeeData: FormData) {
      try {
        const { data } = await employeeApi.create(employeeData)
        this.employees.push(data)
        return data
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      }
    },

    async updateEmployee(id: string, employeeData: Employee) {
      try {
        const { data } = await employeeApi.update(id, employeeData)
        const index = this.employees.findIndex(emp => emp.id === id)
        if (index !== -1) {
          this.employees[index] = data
        }
        return data
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      }
    },

    async fetchSkills() {
      try {
        const { data } = await employeeApi.getSkills()
        this.skills = data
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Une erreur est survenue'
        throw error
      }
    }
  }
})
