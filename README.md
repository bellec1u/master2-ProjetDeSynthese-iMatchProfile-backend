# master2-ProjetDeSynthese-iMatchProfile-backend

## Equipe
 - Mohamed AMIROUNE
 - Léopold BELLEC
 - Juozas DAUZVARDIS
 - Yasar DEMIR
 - Karim DJENKAL
 - Alexis LANOIX
 
## Installation

Apparemment il y a un bug non corrigé dans Glassfish 4.1.1 qui empêche de travailler avec du JSON.  
La solution est :
 - soit d'installer Glassfish 4.1 ; \[non testé\]
 - soit d'installer la dernière version de [Payara Server Full](https://www.payara.fish/downloads). \[testé\] ([guide](http://blog.payara.fish/adding-payara-server-to-netbeans) d'ajout dans Netbeans)

__Note__ : Dans Payara, par défaut, il n'y a pas de Pool, ni de Ressource Sample, il faut les ajouter à la main.  
Configuration du Pool :
```
Resource Type: javax.sql.DataSource
Driver:        Derby 30
Properties :
    ServerName        : localhost
    DatabaseName      : sample
    PortNumber        : 1527
    User              : app
    Password          : app
    SecurityMechanism : 3 (ou rien)
``` 

Une fois que l'application est déployée, il doit être possible de visiter l'adresse `http://localhost:8080/imp/api/exemples/add` et voir s'afficher du JSON, une ligne est également insérée dans la table _Exemple_.

## Architecture

Toutes les routes correspondantes aux ressources seront définies dans le package `imp.core.rest`.  
Les Repository (classes qui permettent d'interroger la base de données) seront définis dans le package `imp.core.bean`.

Un exemple a été fourni :  
 - entity.Exemple
 - bean.ExempleRepository
 - rest.ExempleREST