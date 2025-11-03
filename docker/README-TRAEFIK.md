# Déploiement avec Traefik

Ce guide explique comment déployer l'application MARC avec Traefik comme reverse proxy.

## Prérequis

1. **Réseau Docker externe "web"** : Le réseau `web` doit être créé avant le déploiement
   ```bash
   docker network create web
   ```

2. **DNS configuré** : Assurez-vous que les sous-domaines suivants pointent vers votre serveur :
   - `marc.trapuce.tech` → IP du serveur
   - `backend.marc.trapuce.tech` → IP du serveur
   - `grafana.marc.trapuce.tech` → IP du serveur
   - `keycloak.marc.trapuce.tech` → IP du serveur

3. **Ports ouverts** : Les ports suivants doivent être ouverts :
   - `80` (HTTP)
   - `443` (HTTPS)
   - `8080` (Dashboard Traefik - optionnel)

## Structure des services

- **Traefik** : Reverse proxy avec gestion automatique des certificats SSL (Let's Encrypt)
- **Frontend** : Application Vue.js accessible sur `marc.trapuce.tech`
- **Backend** : API Spring Boot accessible sur `backend.marc.trapuce.tech`
- **Keycloak** : Service d'authentification accessible sur `keycloak.marc.trapuce.tech`
- **Grafana** : Tableaux de bord de monitoring accessible sur `grafana.marc.trapuce.tech`
- **PostgreSQL** : Base de données
- **Prometheus** : Collecteur de métriques
- **MinIO** : Stockage d'objets

## Configuration

### 1. Créer le réseau Docker

```bash
docker network create web
```

### 2. Créer le répertoire pour Let's Encrypt

```bash
mkdir -p ./letsencrypt
chmod 600 ./letsencrypt
```

### 3. Déployer les services

Depuis le répertoire `docker/` :

```bash
docker-compose -f docker-compose.traefik.yml up -d
```

### 4. Vérifier les logs

```bash
# Voir les logs de Traefik
docker logs -f traefik

# Voir les logs du backend
docker logs -f backend

# Voir les logs de Keycloak
docker logs -f keycloak
```

## Accès aux services

Une fois déployé, les services sont accessibles via :

- **Frontend** : https://marc.trapuce.tech
- **Backend API** : https://backend.marc.trapuce.tech/api
- **Keycloak** : https://keycloak.marc.trapuce.tech
  - Admin Console : https://keycloak.marc.trapuce.tech/admin
  - Username : `admin`
  - Password : `admin`
- **Grafana** : https://grafana.marc.trapuce.tech
  - Username : `admin`
  - Password : `admin`
- **Traefik Dashboard** : http://VOTRE_IP:8080 (non sécurisé, seulement pour le développement)

## Certificats SSL

Traefik gère automatiquement les certificats SSL via Let's Encrypt. Les certificats sont stockés dans `./letsencrypt/acme.json`.

**Important** : Le fichier `acme.json` contient des clés privées. Ne le commitez jamais dans Git !

## Redirections HTTP → HTTPS

Tous les services redirigent automatiquement le trafic HTTP vers HTTPS.

## Configuration Frontend

Le frontend est configuré pour utiliser l'API backend via la variable d'environnement `VITE_API_URL` qui est définie lors du build Docker à `https://backend.marc.trapuce.tech/api`.

## Configuration Backend

Le backend est configuré pour utiliser Keycloak via :
- `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=https://keycloak.marc.trapuce.tech/realms/marc`

## Configuration Keycloak

Keycloak est configuré en mode proxy avec :
- `--proxy=edge` : Indique que Keycloak est derrière un proxy
- `--hostname=keycloak.marc.trapuce.tech` : Hostname public
- `KC_PROXY_ADDRESS_FORWARDING=true` : Active le forwarding des adresses

## Configuration Grafana

Grafana est configuré avec :
- `GF_SERVER_ROOT_URL=https://grafana.marc.trapuce.tech` : URL publique
- `GF_SERVER_DOMAIN=grafana.marc.trapuce.tech` : Domaine

## Dépannage

### Les certificats SSL ne sont pas générés

1. Vérifiez que les DNS pointent correctement vers le serveur
2. Vérifiez que le port 80 est accessible depuis Internet (nécessaire pour le challenge HTTP)
3. Consultez les logs de Traefik : `docker logs traefik`

### Erreur "network web not found"

Créez le réseau : `docker network create web`

### Services non accessibles

1. Vérifiez que les services sont en cours d'exécution : `docker ps`
2. Vérifiez les logs : `docker logs <nom-du-service>`
3. Vérifiez la configuration Traefik dans le dashboard : http://VOTRE_IP:8080

### Keycloak ne fonctionne pas correctement

Assurez-vous que :
- Le proxy est bien configuré (`--proxy=edge`)
- `KC_PROXY_ADDRESS_FORWARDING=true` est défini
- Les headers X-Forwarded-* sont correctement passés (configurés dans les labels Traefik)

## Arrêter les services

```bash
docker-compose -f docker-compose.traefik.yml down
```

Pour supprimer aussi les volumes (⚠️ supprime les données) :

```bash
docker-compose -f docker-compose.traefik.yml down -v
```

## Mise à jour

Pour mettre à jour les services :

```bash
# Arrêter les services
docker-compose -f docker-compose.traefik.yml down

# Reconstruire les images
docker-compose -f docker-compose.traefik.yml build

# Redémarrer
docker-compose -f docker-compose.traefik.yml up -d
```

## Notes importantes

1. **Sécurité** : Changez les mots de passe par défaut en production (Keycloak, Grafana, PostgreSQL)
2. **Dashboard Traefik** : Le dashboard est accessible en HTTP non sécurisé sur le port 8080. En production, vous devriez le sécuriser ou le désactiver.
3. **Backup** : Pensez à sauvegarder régulièrement les volumes Docker (postgres_data, grafana_data, minio_data)

