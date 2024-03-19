package pl.dashclever.spring.security

import jakarta.ws.rs.POST
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector
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
    fun configure(http: HttpSecurity, introspector: HandlerMappingIntrospector): SecurityFilterChain {
        val mvcRequestMatcherBuilder = MvcRequestMatcher.Builder(introspector)
        val mvcKeycloakServletMatcher = mvcRequestMatcherBuilder.pattern("/*")

        return http
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers(mvcKeycloakServletMatcher).permitAll()
            .requestMatchers(mvcRequestMatcherBuilder.pattern(HttpMethod.POST, "/api/account")).permitAll()
            .requestMatchers(mvcRequestMatcherBuilder.pattern(HttpMethod.GET, "/login")).denyAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic().disable()
            .formLogin().loginProcessingUrl("/api/login")
            .successHandler { _, response, _ -> response.status = HttpStatus.OK.value() }
            .failureHandler { _, response, _ -> response.status = HttpStatus.BAD_REQUEST.value() }
            .permitAll()
            .and()
            .logout().logoutUrl("/api/logout")
            .logoutSuccessHandler { _, response, _ -> response.status = HttpStatus.OK.value() }
            .and()
            .exceptionHandling()
            .accessDeniedHandler { _, response, _ -> response.status = HttpStatus.FORBIDDEN.value() }
            .authenticationEntryPoint { _, response, _ -> response.status = HttpStatus.UNAUTHORIZED.value() }
            .and()
            .build()
    }
}
