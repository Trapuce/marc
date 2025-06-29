import type { Employee, PaginatedEmployees, Skill } from "../../models"
import apiClient from './client'
import type { AxiosResponse } from 'axios'
import axios from 'axios'


export const employeeApi = {
  getAll: (firstName: string = '', lastName: string = '', skills: string[] = [], experience: number = 0, from: string = '', to: string = ''): Promise<AxiosResponse<PaginatedEmployees>> => {
    const queryParams: Record<string, string | number | string[]> = {};
    if (firstName) queryParams.firstName = firstName;
    if (lastName) queryParams.lastName = lastName;
    if (skills.length > 0) queryParams.skills = skills;
    if (experience) queryParams.experience = experience;
    if (from) queryParams.from = from;
    if (to) queryParams.to = to;
    queryParams.page = 0;
    queryParams.size = 1000;

    const queryString = Object.entries(queryParams)
      .map(([key, value]) => `${key}=${encodeURIComponent(value as string)}`)
      .join('&');

    return apiClient.get(`employees?${queryString}`);
  },

  getById: (id: string): Promise<AxiosResponse<Employee>> => 
    apiClient.get(`employees/${id}`),
    
  create: (employeeData: FormData): Promise<AxiosResponse<Employee>> => 
    axios.post(`${apiClient.defaults.baseURL}/employees`, employeeData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }),
    
  update: (id: string, employee: Employee): Promise<AxiosResponse<Employee>> => 
    apiClient.put(`employees/${id}`, employee),
    
  delete: (id: string): Promise<AxiosResponse<void>> => 
    apiClient.delete(`employees/${id}`),

  getSkills: (): Promise<AxiosResponse<Skill[]>> =>
    apiClient.get(`skills`)
}