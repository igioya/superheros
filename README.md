# Superheros

## Run
```
$ docker-compose build   
$ docker-compose up -d   
$ sudo docker exec -w /usr/local/superheros superheros-container mvn spring-boot:run
```

## Posibles mejoras
* Centralizar el manejo de excepciones a nivel controller mediante un controller advice.
* Actualmente esta desarrollada la logica de manejo de cache, pero con una implementacion en memoria (hashmap), una mejora seria hacer que efectivamente se comunique contra un redis real y no con un hashmap.
* Podrian dividirse en microservicios en caso de escalar, con comunicacion mediante cola de mensajeria.
* Delegar la autenticacion y el manejo de usuarios a un servicio externo como auth0 o Oauth.
