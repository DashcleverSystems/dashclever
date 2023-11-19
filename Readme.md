# Getting Started

To run application you have to have java, docker and docker compose set up on a device.
___
- To make `./gradlew` work on Ubuntu WSL2 give user all permissions to the gradle file `chmod 777 gradlew` or `chmod +x gradlew` and then to make it work type `sed -i -e 's/\r$//' gradlew`.
- To set application environment you can simply run: `./gradlew setDev`.
- Application can be run in dev mode with command:`./gradlew bootDev`. It will try to set up database and application. It assumes that java, docker, docker compose is pre-installed.
- To skip installing a UI into spring jar you can `./gradlew bootDev -x installWeb`
- Frontend uses http clients generated with openapi generator. To generate them you can run `./gradlew generateHttpClients -x installWeb` or from within `web` module `npm run generate:api`. Keep in mind
that in second case you will have to ensure that current openapi specification is present in `./build/openapi.json`, nevertheless you can generate it with `./gradlew generateOpenApiDocs -x installWeb`
- To run frontend without having a node installed on your machine you can run `./gradlew web:start`
- If you want to run it as jar you have to specify program arguments:
  - `--jdbc.db.url="jdbc:postgresql://localhost:5432/dashclever"`
  - `--jdbc.db.username="postgres"`
  - `--jdbc.db.password="postgres"`
  - `--spring.security.logging="TRACE"`
___
To clean your system from app env run `./gradlew cleanDev`
___

### Open API

You can generate Open API specification with `./gradlew generateOpenApiDocs`. Keep in mind that this will spin up the whole application for a moment so you need to have dev env set up.
### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.6/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.6/gradle-plugin/reference/html/#build-image)
* [Testcontainers Postgres Module Reference Guide](https://www.testcontainers.org/modules/databases/postgres/)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/6.0.8/spring-framework-reference/languages.html#coroutines)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#using.devtools)
* [Testcontainers](https://www.testcontainers.org/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#web.reactive)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)