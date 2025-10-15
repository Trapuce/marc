#!/bin/bash

# Script de déploiement MARC sur VPS
# Usage: ./deploy.sh [start|stop|restart|logs|status]

set -e

# Couleurs pour les messages
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Fonction pour afficher les messages
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Vérifier que Docker et Docker Compose sont installés
check_requirements() {
    log_info "Vérification des prérequis..."
    
    if ! command -v docker &> /dev/null; then
        log_error "Docker n'est pas installé. Veuillez l'installer d'abord."
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose n'est pas installé. Veuillez l'installer d'abord."
        exit 1
    fi
    
    log_success "Docker et Docker Compose sont installés"
}

# Fonction pour démarrer les services
start_services() {
    log_info "Démarrage des services MARC en production..."
    
    # Arrêter les services existants s'ils tournent
    docker-compose -f docker-compose.prod.yml down 2>/dev/null || true
    
    # Construire et démarrer les services
    docker-compose -f docker-compose.prod.yml up -d --build
    
    log_success "Services démarrés avec succès!"
    show_status
}

# Fonction pour arrêter les services
stop_services() {
    log_info "Arrêt des services MARC..."
    docker-compose -f docker-compose.prod.yml down
    log_success "Services arrêtés"
}

# Fonction pour redémarrer les services
restart_services() {
    log_info "Redémarrage des services MARC..."
    stop_services
    sleep 2
    start_services
}

# Fonction pour afficher les logs
show_logs() {
    log_info "Affichage des logs des services..."
    docker-compose -f docker-compose.prod.yml logs -f
}

# Fonction pour afficher le statut
show_status() {
    log_info "Statut des services:"
    docker-compose -f docker-compose.prod.yml ps
    
    echo ""
    log_info "URLs d'accès:"
    echo "  🌐 Application principale: http://marc.trapuce.tech"
    echo "  🔐 Keycloak: http://keycloak.marc.trapuce.tech"
    echo "  📊 PgAdmin: http://pgadmin.marc.trapuce.tech"
    echo "  📈 Grafana: http://grafana.marc.trapuce.tech"
    echo "  📊 Prometheus: http://prometheus.marc.trapuce.tech"
    echo "  💾 MinIO Console: http://minio-console.marc.trapuce.tech"
    echo "  🔧 Backend API: http://backend.marc.trapuce.tech"
}

# Fonction pour afficher l'aide
show_help() {
    echo "Usage: $0 [COMMAND]"
    echo ""
    echo "Commandes disponibles:"
    echo "  start     - Démarrer tous les services"
    echo "  stop      - Arrêter tous les services"
    echo "  restart   - Redémarrer tous les services"
    echo "  logs      - Afficher les logs en temps réel"
    echo "  status    - Afficher le statut des services"
    echo "  help      - Afficher cette aide"
    echo ""
    echo "Exemples:"
    echo "  $0 start"
    echo "  $0 logs"
    echo "  $0 status"
}

# Fonction principale
main() {
    check_requirements
    
    case "${1:-help}" in
        start)
            start_services
            ;;
        stop)
            stop_services
            ;;
        restart)
            restart_services
            ;;
        logs)
            show_logs
            ;;
        status)
            show_status
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            log_error "Commande inconnue: $1"
            show_help
            exit 1
            ;;
    esac
}

# Exécuter la fonction principale avec tous les arguments
main "$@"
