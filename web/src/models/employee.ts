import type { Page } from './page'
import type { Project } from './project'

export interface Employee {
  id: string
  firstname: string
  lastname: string
  email: string
  urlCV?: string
  careerStart: string
  createdAt: string
  skills: Skill[]
  projects?: Omit<Project, 'employees'>[]
}

export interface Skill {
  id: string
  label: string
}

export interface PaginatedEmployees extends Page {
  items: Employee[]
}
