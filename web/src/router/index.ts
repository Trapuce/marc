import { createWebHistory, createRouter } from 'vue-router'
import DefaultLayout from '../layouts/DefaultLayout.vue'
import NotFound from '../views/NotFound.vue'
import HomeView from '../views/HomeView.vue'
import EmptyLayout from '../layouts/EmptyLayout.vue'
import LoginView from '../views/LoginView.vue'
import LoaderView from '../views/LoaderView.vue'
import ProjectsView from '../views/ProjectsView.vue'
import EmployeesView from '../views/EmployeesView.vue'
import { useAuthStore } from '../stores/auth'
import RolesNotRegistered from '../views/RolesNotRegistered.vue'

const routes = [
  {
    path: '/',
    meta: { requiresAuth: false },
    component: DefaultLayout,
    children: [
      { path: '', name: 'home', component: HomeView },
      { path: 'projects', name: 'projects', component: ProjectsView },
      { path: 'employees', name: 'employees', component: EmployeesView },
      { path: 'norole', name: 'norole', component: RolesNotRegistered }
    ]
  },
  {
    path: '/auth',
    component: EmptyLayout,
    children: [
      { path: 'login', name: 'login', component: LoginView },
      { path: 'logout', redirect: { name: 'login' } }
    ]
  },
  {
    path: '/loading',
    component: LoaderView
  },
  {
    path: '/:pathMatch(.*)*',
    component: EmptyLayout,
    children: [{ path: '', name: 'not-found', component: NotFound }]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token')
  const authStore = useAuthStore()

  if (!to.meta.requiresAuth) {
    next()
  }

  if (!token) {
    next('/auth/login')
  }

  next()
})

export default router
