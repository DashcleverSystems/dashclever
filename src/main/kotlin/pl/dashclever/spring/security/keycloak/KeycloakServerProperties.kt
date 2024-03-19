package pl.dashclever.spring.security.keycloak

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "keycloak.server")
data class KeycloakServerProperties(
    val contextPath: String = "/oidc",
    val realmImportFile: String = "realm.json",
    val adminUser: AdminUser = AdminUser(),
) {


    // getters and setters
    data class AdminUser(
        val username: String = "admin",
        val password: String = "admin"
    )
}

