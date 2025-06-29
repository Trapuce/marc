# Bilan du projet
* Départ assez fluide, répartition des tâches et formalisation des spécifications

## Difficultés rencontrées
* Grosses difficultés sur la mise en place de l'infra, il aurait fallut plus d'effectif sur ces tâches, ou alors simplifier l'infra, ce que nous avons fait à partir de jeudi
* Erreur d'autant séparer les équipes back et front qui auraient du mettre en place une passerelle fonctionnelle dès le débt, nous avons pu faire interagir les deux environnement uniquement à partir du vendredi
* Nous n'avons pas assez anticipé la mise en commun de nos activités
* D'un point de vue technique, nous avons eu quelques difficultés à organiser notre codebase et à synchroniser nos avancées avec des certaines tâches réalisées en doublon et des modications poussées sur git tardivement
* Nous avons fait le choix de certains outils qui nécessitent pas mal de configuration notamment Keycloak et SpringBoot. A l'échelle de notre application, SpringBoot ne semble pas être le choix optimal et nous aurions pu réduire le temps de développement du backend en utilisant une technologie plus simple à mettre en oeuvre comme par exemple NodeJS.


## Tests
### Tests de Montée en charge
Nous avons effectué des tests de montée en charge avec JMeter, les résultats montrent que le serveur tient la charge pour 10000 requêtes par endpoint à la minute, soit 30000 requetes. Cela est plus que satifaisant car nous n'avons pas mis en place de replicas au niveau de l'infra.

### Tests unitaires

## Améliorations
* Partitionnement de la table projects avec un statut pour optimiser la requête sur les projets non archivés et qui sont postérieurs à la date du jour.