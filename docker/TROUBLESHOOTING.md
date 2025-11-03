# Dépannage - Gateway Timeout

Si vous rencontrez des erreurs "Gateway Timeout" (504), voici les étapes de diagnostic :

## 1. Vérifier que les services sont accessibles

Testez depuis le container Traefik ou un container sur le même réseau :

```bash
# Depuis le container Traefik
docker exec <nom-container-traefik> wget -O- http://backend:8082/api/actuator/health
docker exec <nom-container-traefik> wget -O- http://front:80
docker exec <nom-container-traefik> wget -O- http://keycloak:8080
docker exec <nom-container-traefik> wget -O- http://grafana:3000
```

Si ces commandes échouent, le problème est réseau.

## 2. Vérifier le réseau Docker

```bash
# Vérifier que tous les services sont sur le réseau "web"
docker network inspect web | grep -A 5 "Containers"

# Vérifier que Traefik est sur le réseau "web"
docker network inspect web | grep traefik
```

## 3. Vérifier que les services répondent

```bash
# Tester directement depuis l'hôte (si les ports sont exposés)
curl http://localhost:8082/api/actuator/health  # Backend
curl http://localhost:80                         # Front
curl http://localhost:8080                       # Keycloak
curl http://localhost:3000                       # Grafana
```

## 4. Vérifier les logs Traefik

```bash
docker logs <nom-container-traefik> | tail -50
```

Cherchez des erreurs comme :
- "no healthy upstream"
- "connection refused"
- "timeout"

## 5. Vérifier les logs des services

```bash
docker logs backend | tail -50
docker logs front | tail -50
docker logs keycloak | tail -50
```

## 6. Vérifier la configuration Traefik

Dans le dashboard Traefik (si accessible), vérifiez :
- Que les services sont bien détectés
- Que les healthchecks passent
- Que les routes sont correctement configurées

## Solutions courantes

### Problème : Services non sur le même réseau

**Solution :**
```bash
docker network connect web <nom-container>
```

### Problème : Healthcheck qui échoue

**Solution :** Vérifiez que le path du healthcheck est correct :
- Backend : `/api/actuator/health` (notez le `/api` au début car le backend a un context-path)

### Problème : Timeout trop court

**Solution :** Augmentez les timeouts dans votre configuration Traefik (au niveau global ou par service)

### Problème : Container backend ne démarre pas correctement

**Solution :**
```bash
# Vérifier les logs
docker logs backend

# Vérifier que le backend peut se connecter à PostgreSQL
docker exec backend curl http://localhost:8082/api/actuator/health

# Redémarrer le service
docker-compose -f docker-compose.traefik.yml restart backend
```

### Problème : Backend a un context-path `/api`

Le backend Spring Boot a un `context-path: /api`, donc :
- Les URLs internes doivent inclure `/api` : `http://backend:8082/api/...`
- Traefik route vers `http://backend:8082` mais les requêtes doivent avoir le path `/api/...`

**Vérification :**
```bash
# Depuis le container Traefik
docker exec <nom-container-traefik> wget -O- http://backend:8082/api/actuator/health
# Devrait retourner {"status":"UP"} ou similaire
```

## Commandes utiles de diagnostic

```bash
# Voir tous les services sur le réseau web
docker network inspect web --format '{{range .Containers}}{{.Name}} {{end}}'

# Tester la connectivité réseau
docker run --rm --network web curlimages/curl:latest curl -v http://backend:8082/api/actuator/health

# Voir la configuration Traefik actuelle (si API activée)
curl http://localhost:8080/api/http/routers  # Remplacez localhost:8080 par votre config Traefik
```

