package pl.dashclever.tests.integration

import org.springframework.test.context.ContextConfiguration
import pl.dashclever.tests.integration.spring.TestInMemorySyncDomainEventsPublisherBeansInitializer

@ContextConfiguration(
    initializers = [
        TestcontainersInitializer::class,
        TestInMemorySyncDomainEventsPublisherBeansInitializer::class
    ]
)
internal annotation class DefaultTestContextConfiguration
