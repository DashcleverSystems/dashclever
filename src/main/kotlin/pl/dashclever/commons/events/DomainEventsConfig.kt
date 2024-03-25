package pl.dashclever.commons.events

import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DomainEventsConfig {

    @Bean
    fun domainEvents(applicationEventPublisher: ApplicationEventPublisher): DomainEvents =
        InMemoryDomainEventsPublisher(applicationEventPublisher)
}
