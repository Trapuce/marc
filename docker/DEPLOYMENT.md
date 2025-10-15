# Guide de Déploiement MARC sur VPS

## 🚀 Déploiement sur marc.trapuce.tech

Ce guide vous explique comment déployer l'application MARC sur votre VPS avec le domaine `marc.trapuce.tech`.

## 📋 Prérequis

### Sur votre VPS :
1. **Docker** et **Docker Compose** installés
2. **Git** installé
3. **Accès root** ou utilisateur avec permissions sudo
4. **Ports ouverts** : 80 (HTTP), 5432 (PostgreSQL - optionnel)

### Configuration DNS :
Configurez les enregistrements DNS suivants pour pointer vers l'IP de votre VPS :
```
marc.trapuce.tech                    → IP_VPS
keycloak.marc.trapuce.tech          → IP_VPS
pgadmin.marc.trapuce.tech           → IP_VPS
grafana.marc.trapuce.tech           → IP_VPS
prometheus.marc.trapuce.tech        → IP_VPS
minio.marc.trapuce.tech             → IP_VPS
minio-console.marc.trapuce.tech     → IP_VPS
backend.marc.trapuce.tech           → IP_VPS
```

### Configuration Keycloak :
Le realm Keycloak est déjà configuré avec :
- **Client ID** : `marcapp`
- **Client Secret** : `marc-secret`
- **Redirect URIs** : `http://marc.trapuce.tech/*`
- **Web Origins** : `*`

Les utilisateurs de test sont déjà créés :
- **Admin** : `admini` / `admini`
- **RH** : `rh1`, `rh2`, `rh3` / `1234`
- **Managers** : `manager1`, `manager2`, `manager3` / `1234`

## 🛠️ Installation

### 1. Cloner le projet sur votre VPS
```bash
git clone <votre-repo-git> /opt/marc
cd /opt/marc/docker
```

### 2. Déployer avec le script automatique
```bash
# Rendre le script exécutable
chmod +x deploy.sh

# Démarrer les services
./deploy.sh start
```

### 3. Vérifier le déploiement
```bash
# Voir le statut des services
./deploy.sh status

# Voir les logs
./deploy.sh logs
```

## 🌐 URLs d'Accès

Une fois déployé, vous pouvez accéder aux services via :

| Service | URL | Identifiants |
|---------|-----|--------------|
| **Application principale** | http://marc.trapuce.tech | - |
| **Keycloak** | http://keycloak.marc.trapuce.tech | admin / admin |
| **PgAdmin** | http://pgadmin.marc.trapuce.tech | admin@example.com / admin |
| **Grafana** | http://grafana.marc.trapuce.tech | admin / admin |
| **Prometheus** | http://prometheus.marc.trapuce.tech | - |
| **MinIO Console** | http://minio-console.marc.trapuce.tech | minio / password |
| **Backend API** | http://backend.marc.trapuce.tech | - |

## 🔧 Commandes de Gestion

### Script de déploiement
```bash
./deploy.sh start      # Démarrer les services
./deploy.sh stop       # Arrêter les services
./deploy.sh restart    # Redémarrer les services
./deploy.sh logs       # Voir les logs
./deploy.sh status     # Voir le statut
```

### Commandes Docker Compose directes
```bash
# Démarrer en production
docker-compose -f docker-compose.prod.yml up -d

# Arrêter
docker-compose -f docker-compose.prod.yml down

# Voir les logs
docker-compose -f docker-compose.prod.yml logs -f

# Reconstruire les images
docker-compose -f docker-compose.prod.yml up -d --build
```

## 🔄 Mise à Jour

Pour mettre à jour l'application :

```bash
# 1. Arrêter les services
./deploy.sh stop

# 2. Récupérer les dernières modifications
git pull

# 3. Redémarrer avec les nouvelles images
./deploy.sh start
```

## 🗄️ Sauvegarde des Données

### Base de données PostgreSQL
```bash
# Sauvegarder
docker exec postgres-prod pg_dump -U backend backend > backup_$(date +%Y%m%d_%H%M%S).sql

# Restaurer
docker exec -i postgres-prod psql -U backend backend < backup_file.sql
```

### Volumes Docker
```bash
# Sauvegarder tous les volumes
docker run --rm -v pgadmin_data:/data -v $(pwd):/backup alpine tar czf /backup/pgadmin_data.tar.gz -C /data .
docker run --rm -v grafana_data:/data -v $(pwd):/backup alpine tar czf /backup/grafana_data.tar.gz -C /data .
docker run --rm -v minio:/data -v $(pwd):/backup alpine tar czf /backup/minio_data.tar.gz -C /data .
```

## 🚨 Dépannage

### Vérifier les logs
```bash
# Logs de tous les services
./deploy.sh logs

# Logs d'un service spécifique
docker-compose -f docker-compose.prod.yml logs [service_name]
```

### Redémarrer un service spécifique
```bash
docker-compose -f docker-compose.prod.yml restart [service_name]
```

### Vérifier les conteneurs
```bash
docker ps
docker-compose -f docker-compose.prod.yml ps
```

### Nettoyer les ressources
```bash
# Supprimer les conteneurs arrêtés
docker container prune

# Supprimer les images inutilisées
docker image prune

# Supprimer les volumes inutilisés
docker volume prune
```

## 📝 Notes Importantes

1. **Sécurité** : Changez les mots de passe par défaut en production
2. **Firewall** : Configurez votre firewall pour n'autoriser que les ports nécessaires
3. **SSL/HTTPS** : Ce déploiement utilise HTTP uniquement. Pour HTTPS, configurez un reverse proxy (nginx, traefik, etc.)
4. **Monitoring** : Utilisez Grafana et Prometheus pour surveiller l'application
5. **Sauvegardes** : Planifiez des sauvegardes régulières de la base de données

## 🆘 Support

En cas de problème :
1. Vérifiez les logs avec `./deploy.sh logs`
2. Vérifiez le statut avec `./deploy.sh status`
3. Consultez la documentation Docker Compose
4. Vérifiez la configuration DNS de votre domaine
