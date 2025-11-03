# Déploiement avec Traefik existant

Ce guide explique comment déployer l'application MARC en utilisant un Traefik existant comme reverse proxy.

## Prérequis

1. **Traefik existant** : Vous devez avoir un container Traefik déjà en cours d'exécution
   - Le Traefik doit être configuré avec le provider Docker
   - Il doit être connecté au réseau Docker "web"

2. **Réseau Docker "web"** : Le réseau `web` doit exister et être connecté à votre Traefik
   ```bash
   # Vérifier si le réseau existe
   docker network ls | grep web
   
   # Si nécessaire, créer le réseau
   docker network create web
   
   # Connecter votre Traefik au réseau (si pas déjà fait)
   docker network connect web <nom-du-container-traefik>
   ```

3. **Configuration Traefik** : Votre Traefik doit avoir :
   - Entrypoint `web` sur le port 80 (HTTP)
   - Entrypoint `websecure` sur le port 443 (HTTPS)
   - Certificate resolver `myresolver` configuré (ou modifier les labels dans docker-compose.traefik.yml)

4. **DNS configuré** : Assurez-vous que les sous-domaines suivants pointent vers votre serveur :
   - `marc.trapuce.tech` → IP du serveur
   - `backend.marc.trapuce.tech` → IP du serveur
   - `grafana.marc.trapuce.tech` → IP du serveur
   - `keycloak.marc.trapuce.tech` → IP du serveur

## Structure des services

- **Frontend** : Application Vue.js accessible sur `marc.trapuce.tech`
- **Backend** : API Spring Boot accessible sur `backend.marc.trapuce.tech`
- **Keycloak** : Service d'authentification accessible sur `keycloak.marc.trapuce.tech`
- **Grafana** : Tableaux de bord de monitoring accessible sur `grafana.marc.trapuce.tech`
- **PostgreSQL** : Base de données
- **Prometheus** : Collecteur de métriques
- **MinIO** : Stockage d'objets

## Configuration

### 1. Vérifier la configuration Traefik

Vérifiez que votre Traefik existant a les entrypoints et certificate resolver nécessaires. Si votre Traefik utilise des noms différents, vous devrez adapter les labels dans `docker-compose.traefik.yml`.

**Noms utilisés dans ce fichier :**
- Entrypoints : `web` (HTTP) et `websecure` (HTTPS)
- Certificate resolver : `myresolver`

**Pour modifier :** Recherchez et remplacez dans `docker-compose.traefik.yml` :
- `entrypoints=web` → votre entrypoint HTTP
- `entrypoints=websecure` → votre entrypoint HTTPS
- `tls.certresolver=myresolver` → votre certificate resolver

### 2. Vérifier le réseau Docker

```bash
# Vérifier que le réseau "web" existe
docker network ls | grep web

# Si le réseau n'existe pas, le créer
docker network create web

# Vérifier que votre Traefik est sur le réseau web
docker network inspect web
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
## Certificats SSL

Les certificats SSL sont gérés par votre Traefik existant. Assurez-vous que votre configuration Traefik inclut un certificate resolver pour Let's Encrypt.

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

1. Créez le réseau : `docker network create web`
2. Connectez votre Traefik au réseau : `docker network connect web <nom-du-container-traefik>`

### Les services ne sont pas détectés par Traefik

1. Vérifiez que les services sont sur le réseau "web" : `docker network inspect web`
2. Vérifiez que votre Traefik a le provider Docker activé
3. Vérifiez les noms d'entrypoints et certificate resolver dans votre Traefik
4. Consultez les logs de Traefik : `docker logs <nom-du-container-traefik>`

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

