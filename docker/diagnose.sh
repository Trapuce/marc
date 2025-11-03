#!/bin/bash

echo "🔍 Diagnostic des services MARC"
echo "================================"
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Vérifier que nous sommes dans le bon répertoire
if [ ! -f "docker-compose.traefik.yml" ]; then
    echo -e "${RED}❌ Erreur: docker-compose.traefik.yml introuvable${NC}"
    echo "   Exécutez ce script depuis le répertoire docker/"
    exit 1
fi

echo "1️⃣ Vérification des services Docker..."
echo "--------------------------------------"
docker-compose -f docker-compose.traefik.yml ps
echo ""

echo "2️⃣ Vérification du réseau 'web'..."
echo "-----------------------------------"
if docker network ls | grep -q " web "; then
    echo -e "${GREEN}✅ Réseau 'web' existe${NC}"
    echo ""
    echo "Containers sur le réseau 'web':"
    docker network inspect web --format '{{range .Containers}}{{.Name}} {{end}}' 2>/dev/null || echo "Erreur lors de l'inspection"
else
    echo -e "${RED}❌ Réseau 'web' n'existe pas${NC}"
fi
echo ""

echo "3️⃣ Test de connectivité réseau..."
echo "----------------------------------"
echo "Recherche d'un container Traefik..."
TRAEFIK_CONTAINER=$(docker ps --filter "name=traefik" --format "{{.Names}}" | head -1)

if [ -z "$TRAEFIK_CONTAINER" ]; then
    echo -e "${YELLOW}⚠️  Aucun container Traefik trouvé${NC}"
    echo "   Les tests de connectivité depuis Traefik seront ignorés"
else
    echo -e "${GREEN}✅ Traefik trouvé: $TRAEFIK_CONTAINER${NC}"
    echo ""
    
    # Vérifier si Traefik est sur le réseau web
    if docker network inspect web 2>/dev/null | grep -q "$TRAEFIK_CONTAINER"; then
        echo -e "${GREEN}✅ Traefik est sur le réseau 'web'${NC}"
        
        echo ""
        echo "Test de connectivité depuis Traefik:"
        echo "- Backend (port 8082):"
        if docker exec $TRAEFIK_CONTAINER wget -q -O- --timeout=5 http://backend:8082/api/actuator/health 2>/dev/null; then
            echo -e "${GREEN}  ✅ Backend accessible${NC}"
        else
            echo -e "${RED}  ❌ Backend NON accessible${NC}"
        fi
        
        echo "- Front (port 80):"
        if docker exec $TRAEFIK_CONTAINER wget -q -O- --timeout=5 http://front:80 2>/dev/null | head -1 > /dev/null; then
            echo -e "${GREEN}  ✅ Front accessible${NC}"
        else
            echo -e "${RED}  ❌ Front NON accessible${NC}"
        fi
        
        echo "- Keycloak (port 8080):"
        if docker exec $TRAEFIK_CONTAINER wget -q -O- --timeout=5 http://keycloak:8080 2>/dev/null | head -1 > /dev/null; then
            echo -e "${GREEN}  ✅ Keycloak accessible${NC}"
        else
            echo -e "${RED}  ❌ Keycloak NON accessible${NC}"
        fi
        
        echo "- Grafana (port 3000):"
        if docker exec $TRAEFIK_CONTAINER wget -q -O- --timeout=5 http://grafana:3000/api/health 2>/dev/null; then
            echo -e "${GREEN}  ✅ Grafana accessible${NC}"
        else
            echo -e "${RED}  ❌ Grafana NON accessible${NC}"
        fi
    else
        echo -e "${YELLOW}⚠️  Traefik n'est PAS sur le réseau 'web'${NC}"
        echo "   Connectez Traefik au réseau: docker network connect web $TRAEFIK_CONTAINER"
    fi
fi

echo ""
echo "4️⃣ Vérification des healthchecks..."
echo "------------------------------------"
echo "- Backend healthcheck:"
docker exec backend curl -f -s http://localhost:8082/api/actuator/health > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo -e "${GREEN}  ✅ Backend healthcheck OK${NC}"
else
    echo -e "${RED}  ❌ Backend healthcheck ÉCHEC${NC}"
fi

echo ""
echo "5️⃣ Vérification des logs récents..."
echo "------------------------------------"
echo "Voulez-vous voir les logs récents des services ? (y/N)"
read -t 5 -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo ""
    echo "--- Logs Backend (dernières 10 lignes) ---"
    docker logs backend --tail 10 2>&1 | tail -10
    echo ""
    echo "--- Logs Front (dernières 10 lignes) ---"
    docker logs front --tail 10 2>&1 | tail -10
    echo ""
    echo "--- Logs Keycloak (dernières 10 lignes) ---"
    docker logs keycloak --tail 10 2>&1 | tail -10
fi

echo ""
echo "✅ Diagnostic terminé"
echo ""
echo "📝 Actions suggérées:"
echo "1. Si les services ne sont pas accessibles depuis Traefik, vérifiez le réseau"
echo "2. Si les healthchecks échouent, consultez les logs des services"
echo "3. Redémarrez les services après modification: docker-compose -f docker-compose.traefik.yml up -d"
echo ""

