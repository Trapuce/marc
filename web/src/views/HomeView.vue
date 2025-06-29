<script setup lang="ts">

import { useAuthStore } from '../stores/auth';
import EmployeesView from './EmployeesView.vue';
import ProjectsView from './ProjectsView.vue';
import RolesNotRegistered from './RolesNotRegistered.vue';


const authStore = useAuthStore()

</script>

<template>
<EmployeesView v-if="authStore.role=='hr'"/>
<ProjectsView v-else-if="authStore.role=='manager'"/>
<RolesNotRegistered v-else/>
</template>
