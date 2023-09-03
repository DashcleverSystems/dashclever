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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pl.dashclever.accountresources.account.readmodel.AccountReader


@Configuration
internal class SecurityConfig(
    private val accountReader: AccountReader,
    private val corsFilter: CorsFilter
) {

    @Bean
    fun userDetailsService(): UserDetailsService =
        EntryUserDetailsService(accountReader)

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        return http
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter::class.java)
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET, "/*").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/account").permitAll()
            .requestMatchers(HttpMethod.GET, "/login").denyAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic().disable()
            .formLogin()
            .loginProcessingUrl("/api/login")
            .successHandler { _, response, _ -> response.status = HttpStatus.OK.value() }
            .failureHandler { _, response, _ -> response.status = HttpStatus.UNAUTHORIZED.value() }
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
