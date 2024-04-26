package pl.dashclever.spring.events

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.event.SimpleApplicationEventMulticaster
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.task.SimpleAsyncTaskExecutor
import kotlin.DeprecationLevel.ERROR

private val inMemoryAsyncDomainEventsPublisherBeans = beans {
    bean<InMemoryDomainEventsPublisher>()
    bean { SimpleApplicationEventMulticaster().apply { setTaskExecutor(SimpleAsyncTaskExecutor()) } }
}

@Deprecated(message = "We do not send any events via spring events mechanism. It was too complicated to set transactional async events handling. We use spring cloud streams", level = ERROR)
class InMemoryAsyncDomainEventsPublisherBeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {

    override fun initialize(applicationContext: GenericApplicationContext) {
        inMemoryAsyncDomainEventsPublisherBeans.initialize(applicationContext)
    }
}
