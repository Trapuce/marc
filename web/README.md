# Web App

Ce projet est une application Vue 3 utilisant TypeScript, Vite, Tailwind CSS et PrimeVue. Il inclut la gestion d'état avec Pinia, la gestion des routes avec Vue Router, et la prise en charge de l'internationalisation avec Vue I18n.

---

## 📌 **Prérequis**

- **Node.js** ≥ 18.0.0
- **npm** ou **pnpm** (npm recommandé)

---

## 🚀 **Installation & Lancement**

```sh
git clone <repository_url> # Cloner le projet
cd web # Se rendre dans le dossier de l'app
npm install  # Installer les dépendances
npm run dev  # Lancer le projet
```

L'application sera accessible sur **http://localhost:5173/**.

---

## 📂 **Structure du projet**

```
web/
│── src/
│   ├── assets/           # Fichiers statiques (images, styles globaux...)
│   ├── components/       # Composants réutilisables
│   ├── composables/      # Fonctions utilitaires basées sur Composition API
│   ├── layouts/          # Layouts globaux de l'application
│   ├── pages/            # Pages principales liées aux routes
│   ├── router/           # Configuration Vue Router
│   ├── stores/           # Gestion d'état avec Pinia
│   ├── i18n/             # Fichiers de traduction pour Vue I18n
│   ├── App.vue           # Composant racine
│   ├── main.ts           # Point d'entrée principal
│── public/               # Fichiers statiques accessibles directement
│── vite.config.ts        # Configuration Vite
│── tsconfig.json         # Configuration TypeScript
│── tailwind.config.ts    # Configuration Tailwind CSS
│── package.json          # Dépendances et scripts npm
│── README.md             # Documentation du projet
```

---

## 📦 **Dépendances principales**

| Package         | Version   | Description          |
| --------------- | --------- | -------------------- |
| **Vue**         | `^3.5.13` | Framework JavaScript |
| **Vue Router**  | `^4.5.0`  | Gestion des routes   |
| **Pinia**       | `^3.0.1`  | Gestion d'état       |
| **Vue I18n**    | `^10.0.5` | Internationalisation |
| **Axios**       | `^1.7.9`  | Requêtes HTTP        |
| **PrimeVue**    | `^4.2.5`  | Composants UI        |
| **TailwindCSS** | `^4.0.6`  | Framework CSS        |

---

## 🛠 **Commandes utiles**

| Commande          | Description                              |
| ----------------- | ---------------------------------------- |
| `npm run dev`     | Démarre le serveur de développement      |
| `npm run build`   | Compile l'application pour la production |
| `npm run preview` | Prévisualise la version buildée          |
| `npm run lint`    | Vérifie le code avec ESLint              |
| `npm run format`  | Formate le code avec Prettier            |
| `npm run test`    | Exécute les tests avec Vitest            |

---

## 🔗 **Liens utiles**

- [Vue.js](https://vuejs.org/)
- [Vue Router](https://router.vuejs.org/)
- [Pinia](https://pinia.vuejs.org/)
- [Vue I18n](https://vue-i18n.intlify.dev/)
- [PrimeVue](https://primevue.org/)
- [Tailwind CSS](https://tailwindcss.com/)
- [Vite](https://vitejs.dev/)

---

## 🤝 **Travailler sur le projet**

1. **Clone** le projet
2. **Crée une branche** (`git checkout -b front/feat/ma-feature`)
3. **Fais tes modifications** et **commit** (`git commit -m "[front] Ajout de ma feature"`)
4. **Push** (`git push origin front/feat/ma-feature`)
5. **Ouvre une Pull Request**

---
