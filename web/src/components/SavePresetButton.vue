<script setup lang="ts">
import { ref } from 'vue'
import { Menu, InputText, Button } from 'primevue'
import { type SearchProfile } from '../models';
import {useSearchProfileStore} from '../stores/search'
const props = defineProps<{
    preset: SearchProfile | null
    disabled: boolean
}>()

const searchProfileStore = useSearchProfileStore();

const overlayPanel = ref()
const presetName = ref<string>('')

const toggleOverlay = (event: Event) => {
    overlayPanel.value.toggle(event)
}

async function savePreset() {
    if (props.preset && presetName.value.length > 0) {
        console.log('Preset to save', props.preset)
        const profile: SearchProfile = {
            id: 'a7e03e6f-a9b3-495f-acab-c2df86b386af',
            name: presetName.value,
            createdAt: new Date().toISOString(),
            startDate: props.preset.startDate,
            endDate: props.preset.endDate,
            skillsIds: props.preset.skillsIds,
            experience: props.preset.experience,
        }
        await searchProfileStore.createSearchProfile(profile);
        // close overlay
        toggleOverlay(new Event('click'))
    }else{
        console.log('Preset not saved', props.preset)
    }
}

</script>

<template>
    <Button @click="toggleOverlay" icon="pi pi-save" size="small" :disabled="disabled" />
    <Menu ref="overlayPanel" :popup="true">
        <template #start>
            <div class="p-3">
                <InputText class="mr-1" v-model="presetName" :placeholder="$t('employees.filters.name')" />
                <Button @click="savePreset" label="Enregistrer" />
            </div>
        </template>
    </Menu>
</template>