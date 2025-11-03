#!/bin/bash

set -e

echo "🚀 Déploiement de l'application MARC avec Traefik"
echo ""

# Vérifier si le réseau web existe
if ! docker network ls | grep -q " web "; then
    echo "📦 Création du réseau Docker 'web'..."
    docker network create web
    echo "✅ Réseau 'web' créé"
else
    echo "✅ Réseau 'web' existe déjà"
fi

# Créer le répertoire letsencrypt si nécessaire
if [ ! -d "./letsencrypt" ]; then
    echo "📁 Création du répertoire letsencrypt..."
    mkdir -p ./letsencrypt
    chmod 600 ./letsencrypt
    echo "✅ Répertoire letsencrypt créé"
else
    echo "✅ Répertoire letsencrypt existe déjà"
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
echo "   - Les certificats SSL seront générés automatiquement par Let's Encrypt"
echo "   - Cela peut prendre quelques minutes lors du premier démarrage"
echo ""

