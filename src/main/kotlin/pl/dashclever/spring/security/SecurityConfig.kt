package pl.dashclever.spring.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import pl.dashclever.accountresources.account.readmodel.CredentialsReader

@Configuration
internal class SecurityConfig(
    private val credentialsReader: CredentialsReader
) {

    @Bean
    fun userDetailsService(): UserDetailsService =
        EntryUserDetailsService(credentialsReader)

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET, "/*").permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/account").permitAll()
                it.requestMatchers(HttpMethod.GET, "/login").denyAll()
                it.anyRequest().authenticated()
            }
            .httpBasic { it.disable() }
            .formLogin {
                it.loginProcessingUrl("/api/login")
                it.successHandler { _, response, _ -> response.status = HttpStatus.OK.value() }
                it.failureHandler { _, response, _ -> response.status = HttpStatus.BAD_REQUEST.value() }
                it.permitAll()
            }
            .logout {
                it.logoutUrl("/api/logout")
                it.logoutSuccessHandler { _, response, _ -> response.status = HttpStatus.OK.value() }
            }
            .exceptionHandling {
                it.accessDeniedHandler { _, response, _ -> response.status = HttpStatus.FORBIDDEN.value() }
                it.authenticationEntryPoint { _, response, _ -> response.status = HttpStatus.UNAUTHORIZED.value() }
            }
            .build()
    }
}
