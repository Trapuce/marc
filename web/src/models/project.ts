import type { Employee } from './employee'
import type { Page } from './page'

export interface Project {
  id: string
  name: string
  description: string
  color?: string
  managerId: string
  startDate: string
  endDate: string
  client: string
  archivedAt?: string
  createdAt: string
  employees?: Omit<Employee, 'projects'>[]
}

export interface PaginatedProjects extends Page {
  items: Project[]
}
