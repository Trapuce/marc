#!/bin/bash

set -e

echo "🚀 Déploiement de l'application MARC avec Traefik existant"
echo ""
echo "⚠️  IMPORTANT: Ce script suppose que vous avez déjà un Traefik en cours d'exécution"
echo ""

# Vérifier si le réseau web existe
if ! docker network ls | grep -q " web "; then
    echo "📦 Création du réseau Docker 'web'..."
    docker network create web
    echo "✅ Réseau 'web' créé"
    echo "⚠️  N'oubliez pas de connecter votre Traefik au réseau web:"
    echo "   docker network connect web <nom-du-container-traefik>"
else
    echo "✅ Réseau 'web' existe déjà"
fi

# Vérifier si Traefik est sur le réseau web
echo ""
echo "🔍 Vérification du Traefik existant..."
TRAEFIK_CONTAINERS=$(docker network inspect web 2>/dev/null | grep -o '"Name":"[^"]*traefik[^"]*"' | cut -d'"' -f4 || echo "")
if [ -z "$TRAEFIK_CONTAINERS" ]; then
    echo "⚠️  Aucun container Traefik trouvé sur le réseau 'web'"
    echo "   Assurez-vous que votre Traefik est connecté au réseau web:"
    echo "   docker network connect web <nom-du-container-traefik>"
else
    echo "✅ Traefik trouvé sur le réseau: $TRAEFIK_CONTAINERS"
fi

# Vérifier que les fichiers nécessaires existent
if [ ! -f "docker-compose.traefik.yml" ]; then
    echo "❌ Erreur: docker-compose.traefik.yml introuvable"
    echo "   Assurez-vous d'être dans le répertoire docker/"
    exit 1
fi

echo ""
echo "🔍 Vérification de la configuration..."

# Afficher un résumé
echo ""
echo "📋 Configuration des services:"
echo "   - Frontend: https://marc.trapuce.tech"
echo "   - Backend: https://backend.marc.trapuce.tech"
echo "   - Keycloak: https://keycloak.marc.trapuce.tech"
echo "   - Grafana: https://grafana.marc.trapuce.tech"
echo ""

read -p "Continuer le déploiement? (y/N) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "❌ Déploiement annulé"
    exit 1
fi

echo ""
echo "🔨 Construction et démarrage des services..."
docker-compose -f docker-compose.traefik.yml up -d --build

echo ""
echo "⏳ Attente du démarrage des services (30 secondes)..."
sleep 30

echo ""
echo "📊 État des services:"
docker-compose -f docker-compose.traefik.yml ps

echo ""
echo "✅ Déploiement terminé!"
echo ""
echo "📝 Prochaines étapes:"
echo "   1. Vérifiez les logs: docker-compose -f docker-compose.traefik.yml logs -f"
echo "   2. Vérifiez le dashboard Traefik: http://VOTRE_IP:8080"
echo "   3. Accédez à l'application: https://marc.trapuce.tech"
echo ""
echo "⚠️  Important:"
echo "   - Assurez-vous que les DNS pointent vers ce serveur"
echo "   - Les certificats SSL sont gérés par votre Traefik existant"
echo "   - Vérifiez que votre Traefik a les entrypoints 'web' et 'websecure'"
echo "   - Vérifiez que votre Traefik a un certificate resolver 'myresolver'"
echo "   - Ou modifiez les labels dans docker-compose.traefik.yml pour correspondre à votre config"
echo ""

