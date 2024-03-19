package pl.dashclever.spring.security.keycloak

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.keycloak.common.ClientConnection
import org.keycloak.models.KeycloakSession
import org.keycloak.services.filters.AbstractRequestFilter
import java.io.UnsupportedEncodingException


class EmbeddedKeycloakRequestFilter : AbstractRequestFilter(), Filter {
    @Throws(UnsupportedEncodingException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse?, filterChain: FilterChain) {
        servletRequest.characterEncoding = "UTF-8"
        val clientConnection = createConnection(servletRequest as HttpServletRequest)

        filter(clientConnection) { _: KeycloakSession? ->
            try {
                filterChain.doFilter(servletRequest, servletResponse)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    private fun createConnection(request: HttpServletRequest): ClientConnection {
        return object : ClientConnection {
            override fun getRemoteAddr(): String {
                return request.remoteAddr
            }

            override fun getRemoteHost(): String {
                return request.remoteHost
            }

            override fun getRemotePort(): Int {
                return request.remotePort
            }

            override fun getLocalAddr(): String {
                return request.localAddr
            }

            override fun getLocalPort(): Int {
                return request.localPort
            }
        }
    }
}
