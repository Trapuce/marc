import type { PaginatedProjects, Project } from '../../models'
import apiClient from './client'
import type { AxiosResponse } from 'axios'

export const projectApi = {
  getAll: (archived: boolean): Promise<AxiosResponse<PaginatedProjects>> => {
    const reponse = apiClient.get('projects?archived=' + archived + '&page=0&limit=1000')
    return reponse},
    
  getById: (id: string): Promise<AxiosResponse<Project>> => 
    apiClient.get(`projects/${id}`),
    
  create: (project: Project): Promise<AxiosResponse<Project>> => 
    apiClient.post('projects', project),
    
  update: (id: string, project: Project): Promise<AxiosResponse<Project>> => 
    apiClient.put(`projects/${id}`, project),
    
  archive: (id: string): Promise<AxiosResponse<Project>> => 
    apiClient.post(`projects/${id}/archive`),
}