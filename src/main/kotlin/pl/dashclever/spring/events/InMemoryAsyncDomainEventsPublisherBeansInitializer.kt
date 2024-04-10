package pl.dashclever.spring.events

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.event.SimpleApplicationEventMulticaster
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.task.SimpleAsyncTaskExecutor

private val inMemoryAsyncDomainEventsPublisherBeans = beans {
    bean<InMemoryDomainEventsPublisher>()
    bean { SimpleApplicationEventMulticaster().apply { setTaskExecutor(SimpleAsyncTaskExecutor()) } }
}

class InMemoryAsyncDomainEventsPublisherBeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        inMemoryAsyncDomainEventsPublisherBeans.initialize(applicationContext)
    }
}
