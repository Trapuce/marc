import axios from 'axios'
const baseURL = import.meta.env.VITE_API_URL || 'http://local.marc/api/'

const apiClient = axios.create({
  baseURL: baseURL,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true // A voir avec team back si on utilise des cookies
})

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')

    if (token) {
      // Attach the token in the Authorization header for each request
      config.headers['Authorization'] = `Bearer ${token}`
    }

    return config
  },
  
  (error) => {
    // Handle request error
    return Promise.reject(error)
  }
)

export default apiClient
