package pl.dashclever.spring.events.cloudstreams

import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.dashclever.commons.security.CurrentAccessProvider

@Configuration
class CloudStreamDomainEventsConfiguration(
    currentAccessProvider: CurrentAccessProvider,
    streamBridge: StreamBridge,
) {

    private val cloudStreamDomainEvents = CloudStreamDomainEvents(streamBridge)
    private val cloudStreamMultitenantDomainEvents =
        CloudStreamDomainEventsMultitenantProxy(cloudStreamDomainEvents, currentAccessProvider)

    @Bean
    fun cloudStreamDomainEvents() = cloudStreamMultitenantDomainEvents
}
