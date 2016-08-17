# TRP Ticket Manager Service #

Descripción:
============

Es una aplicación Java, armada con Maven, que expone servicios REST con JAX-RS (Apache CXF) y utiliza Spring.

Para compilar y empaquetar:
================================================

Parado sobre la carpeta root del proyecto, ejecutar:

> mvn clean package && cp target/trp-ticket-manager-service.war $CATALINA_HOME/webapps

Si toda sale bien, la API estará disponible en localhost:8080/trp-ticket-manager-service/services

Para formatear el código fuente:
================================

El proyecto incluye un Code Style en formato de Eclipse XML para darle un formato al código fuente. Este Code Style puede utilizarse de dos maneras: 1) Incluyéndolo en el Eclipse, o 2) Compilando con el perfil format activo, de la siguiente forma:

> mvn clean package -Pformat
