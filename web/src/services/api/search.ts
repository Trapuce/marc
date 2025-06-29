import type { SearchProfile } from '../../models'
import apiClient from './client'
import type { AxiosResponse } from 'axios'

export const searchProfileApi = {
    getAll: (): Promise<AxiosResponse<SearchProfile[]>> => {
        const reponse = apiClient.get('searchProfiles')  
        console.log(reponse)
        return reponse},
      create: (preset: SearchProfile): Promise<AxiosResponse<SearchProfile>> => 
        apiClient.post('searchProfiles', preset),
}