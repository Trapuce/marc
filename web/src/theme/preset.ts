import { definePreset } from '@primevue/themes'
import Aura from '@primevue/themes/aura'

const palette = {
  50:  '#fff5f5',
  100: '#ffe3e3',
  200: '#ffbfbf',
  300: '#ff9999',
  400: '#ff7373',
  500: '#FF5858',
  600: '#e04e4e',
  700: '#c14444',
  800: '#a23939',
  900: '#832f2f',
  950: '#521b1b' 
};


const preset = definePreset(Aura, {
  semantic: {
    primary: palette,
    colorScheme: {
      light: {
        primary: {
          color: '{primary.500}',
          contrastColor: '#ffffff',
          hoverColor: '{primary.600}',
          activeColor: '{primary.700}',
        },
        highlight: {
          background: '{primary.50}',
          focusBackground: '{primary.100}',
          color: '{primary.700}',
          focusColor: '{primary.800}',
        },
      },
      dark: {
        primary: {
          color: '{primary.400}',
          contrastColor: '{surface.900}',
          hoverColor: '{primary.300}',
          activeColor: '{primary.200}',
        },
        highlight: {
          background:
            'color-mix(in srgb, {primary.400}, transparent 84%)',
          focusBackground:
            'color-mix(in srgb, {primary.400}, transparent 76%)',
          color: 'rgba(255,255,255,.87)',
          focusColor: 'rgba(255,255,255,.87)',
        },
      },
    },
  }
})

export default preset
