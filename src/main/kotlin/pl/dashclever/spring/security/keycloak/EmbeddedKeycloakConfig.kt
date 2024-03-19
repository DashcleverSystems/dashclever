package pl.dashclever.spring.security.keycloak

import jakarta.servlet.Filter
import jakarta.servlet.Servlet
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher
import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters
import org.keycloak.platform.Platform
import org.keycloak.platform.PlatformProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.naming.CompositeName
import javax.naming.InitialContext
import javax.naming.Name
import javax.naming.NameParser
import javax.naming.NamingException
import javax.naming.spi.InitialContextFactory
import javax.naming.spi.NamingManager
import javax.sql.DataSource


@Configuration
@EnableConfigurationProperties
class EmbeddedKeycloakConfig {
    @Bean
    fun keycloakJaxRsApplication(
        keycloakServerProperties: KeycloakServerProperties,
        dataSource: DataSource
    ): ServletRegistrationBean<*> {
        mockJndiEnvironment(dataSource)
        EmbeddedKeycloakApplication.keycloakServerProperties = keycloakServerProperties
        val servlet: ServletRegistrationBean<*> = ServletRegistrationBean<Servlet>(
            HttpServlet30Dispatcher()
        )
        servlet.addInitParameter(
            "jakarta.ws.rs.Application",
            EmbeddedKeycloakApplication::class.java.getName()
        )
        servlet.addInitParameter(
            ResteasyContextParameters.RESTEASY_SERVLET_MAPPING_PREFIX,
            keycloakServerProperties.contextPath
        )
        servlet.addInitParameter(
            ResteasyContextParameters.RESTEASY_USE_CONTAINER_FORM_PARAMS,
            "true"
        )
        servlet.addUrlMappings(keycloakServerProperties.contextPath + "/*")
        servlet.setLoadOnStartup(1)
        servlet.isAsyncSupported = true
        return servlet
    }

    @Bean
    fun keycloakSessionManagement(
        keycloakServerProperties: KeycloakServerProperties
    ): FilterRegistrationBean<*> {
        val filter: FilterRegistrationBean<in Filter> = FilterRegistrationBean<Filter>()
        filter.setName("Keycloak Session Management")
        filter.setFilter(EmbeddedKeycloakRequestFilter())
        filter.addUrlPatterns(keycloakServerProperties.contextPath + "/*")

        return filter
    }

    @Throws(NamingException::class)
    private fun mockJndiEnvironment(dataSource: DataSource) {
        NamingManager.setInitialContextFactoryBuilder {
            InitialContextFactory {
                object : InitialContext() {
                    override fun lookup(name: Name): Any? {
                        return lookup(name.toString())
                    }

                    override fun lookup(name: String): Any? {
                        if ("spring/datasource" == name) {
                            return dataSource
                        } else if (name.startsWith("java:jboss/ee/concurrency/executor/")) {
                            return fixedThreadPool()
                        }
                        return null
                    }

                    override fun getNameParser(name: String): NameParser {
                        return NameParser { n: String? -> CompositeName(n) }
                    }

                    override fun close() {
                    }
                }
            }
        }
    }

    @Bean("fixedThreadPool")
    fun fixedThreadPool(): ExecutorService {
        return Executors.newFixedThreadPool(5)
    }

    @Bean
    @ConditionalOnMissingBean(name = ["springBootPlatform"])
    protected fun springBootPlatform(): PlatformProvider {
        return SimplePlatformProvider()
    }
}

