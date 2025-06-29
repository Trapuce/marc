<script setup lang="ts">
import { useAuthStore } from '../stores/auth'
import Logo from './Logo.vue'
import HorizontalContainer from './HorizontalContainer.vue'
import ThemeMode from './ThemeMode.vue'
import { RouterLink } from 'vue-router'
import { Button, Menu, Divider, Badge } from 'primevue'
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import { projectApi } from '../services/api/projects'


const authStore = useAuthStore()
const { t } = useI18n()
const router = useRouter()

const logout = () => {
  authStore.logout()
}

const authDisplay = computed(() => {
  const authRole = authStore.role
  return `header.menu.${authRole}`
})

const menu = ref()
const toggle = (event) => {
  menu.value.toggle(event)
}
const items = ref([
  {
    label: t('header.projects'),
    icon: 'pi pi-folder',
    command: () => {
      router.push('/projects')
    }
  },
  {
    label: t('header.employees'),
    icon: 'pi pi-users',
    command: () => {
      router.push('/employees')
    }
  },
  {
    separator: true
  },
  {
    label: t('header.menu.settings'),
    icon: 'pi pi-cog',
    command: () => {
      console.log('Settings')
    }
  },
  {
    label: t('header.menu.messages'),
    icon: 'pi pi-inbox',
    badge: 2,
    command: () => {
      console.log('Messages')
    }
  },
  {
    label: t('header.menu.logout'),
    icon: 'pi pi-sign-out',
    command: () => {
      logout()
    }
  }
])

const profileMenu = ref()
const profileItems = ref([
  {
    label: t('header.menu.settings'),
    icon: 'pi pi-cog',
    command: () => {
      console.log('Settings')
    }
  },
  {
    label: t('header.menu.messages'),
    icon: 'pi pi-inbox',
    badge: 2,
    command: () => {
      console.log('Messages')
    }
  },
  {
    label: t('header.menu.logout'),
    icon: 'pi pi-sign-out',
    command: () => {
      logout()
    }
  }
])

const toggleProfileMenu = (event) => {
  profileMenu.value.toggle(event)
}
</script>

<template>
  <header class="border-b border-gray-200">
    <HorizontalContainer>
      <nav class="hidden sm:flex justify-between items-center py-4">
        <Logo :colored="true" :width="100" :height="20" />
        <div v-if="authStore.role == 'manager'" class="nav-left flex items-center gap-4">
          <RouterLink to="/projects" class="nav-link">{{ t('header.projects') }}</RouterLink>
          <RouterLink to="/employees" class="nav-link">{{ t('header.employees') }}</RouterLink>
        </div>

        <div class="nav-right flex items-center gap-4">
          <ThemeMode />
          <Button
            variant="text"
            @click="toggleProfileMenu"
            aria-haspopup="true"
            aria-controls="overlay_menu"
          >
            <span class="inline-flex flex-col items-start">
              <span class="font-bold">{{ authStore.username }}</span>
              <span class="text-xs">{{ t(authDisplay) }}</span>
            </span>
            <i class="pi pi-chevron-down"></i>
          </Button>

          <Menu ref="profileMenu" id="overlay_menu" :model="profileItems" :popup="true">
            <template #item="{ item, props }">
              <a v-ripple class="flex items-center gap-2 p-1.5 cursor-pointer">
                <span :class="item.icon" />
                <span>{{ item.label }}</span>
                <Badge v-if="item.badge" class="ml-auto" :value="item.badge" size="small" />
                <span
                  v-if="item.shortcut"
                  class="ml-auto border border-surface rounded bg-emphasis text-muted-color text-xs"
                  >{{ item.shortcut }}</span
                >
              </a>
            </template>
            <template #end>
              <Divider class="!my-0" />
              <div
                v-ripple
                class="w-full border-0 flex items-start p-2 pl-4 hover:bg-surface-100 dark:hover:bg-surface-800"
              >
                <span class="inline-flex flex-col items-start">
                  <span class="font-bold">{{ authStore.username }}</span>
                  <span class="text-sm">{{ t(authDisplay) }}</span>
                </span>
              </div>
            </template>
          </Menu>
        </div>
      </nav>
      <nav class="sm:hidden flex justify-between items-center py-3">
        <Logo :colored="true" :width="75" :height="20" />
        <Button
          type="button"
          icon="pi pi-bars"
          @click="toggle"
          aria-haspopup="true"
          aria-controls="overlay_menu"
          variant="text"
        />
        <Menu ref="menu" id="overlay_menu" :model="items" :popup="true">
          <template #item="{ item, props }">
            <a v-ripple class="flex items-center gap-2 p-1.5 cursor-pointer">
              <span :class="item.icon" />
              <span>{{ item.label }}</span>
              <Badge v-if="item.badge" class="ml-auto" :value="item.badge" size="small" />
              <span
                v-if="item.shortcut"
                class="ml-auto border border-surface rounded bg-emphasis text-muted-color text-xs"
                >{{ item.shortcut }}</span
              >
            </a>
          </template>
        </Menu>
      </nav>
    </HorizontalContainer>
  </header>
</template>
