package pl.dashclever.tests.integration.spring

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.event.SimpleApplicationEventMulticaster
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.task.SyncTaskExecutor
import pl.dashclever.spring.events.InMemoryDomainEventsPublisher
import kotlin.DeprecationLevel.ERROR

private val domainEventBeans = beans {
    bean<InMemoryDomainEventsPublisher>()
    bean(isPrimary = true) { SimpleApplicationEventMulticaster().apply { setTaskExecutor(SyncTaskExecutor()) } }
}

@Deprecated(message = "We do not send any events via spring events mechanism. We use spring cloud streams", level = ERROR)
internal class TestInMemorySyncDomainEventsPublisherBeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        domainEventBeans.initialize(applicationContext)
    }
}
