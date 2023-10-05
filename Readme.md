# Getting Started

To run application you have to have java, docker and docker compose set up on a device.
___
Application can be run in dev mode with command:`./gradlew bootDev`. It will try to set up database and application. It assumes that java, docker, docker compose is pre installed.
___
To clean your system from app env run `./gradlew cleanDev`
___

### Open API

You can generate Open API specification with `./gradlew generateOpenApiDocs`. Keep in mind that this will spin up the whole application for one second so you need to have dev env set up.
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

