# ProjetCv

## Auteurs
- GUILIANI Maxime
- MUSARDO Léo-Paul

## Back End Project for CV
Ce projet est un projet de gestion de CV qui est une API REST. Il permet de créer, modifier, supprimer et afficher des CV.
Le back end est réalisé en Java avec le framework Spring.
Le front end est réalisé en Vue.js et Vite (voir le projet [ProjetCvFront](https://github.com/MaximeGuiliani/vue-cvproject)).


## Technologies
- SpringFramework 6 (latest stable)
- SpringBoot 3 (latest stable)

## Déploiement avec Maven
```sh
mvn clean
mvn install
mvn spring-boot:run
```

## Déploiement avec fichier war

```sh
mvn clean
mvn install
mvn package
java -jar target/ProjetCv-0.0.1-SNAPSHOT.war
cd WEB-INF
java -classpath "lib/*:classes/." com.example.projetcv.ProjetcvApplication
```