package pl.dashclever.spring.events

import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import pl.dashclever.commons.events.DomainEvent
import pl.dashclever.commons.events.DomainEvents

class InMemoryDomainEventsPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : DomainEvents {

    override fun publish(event: DomainEvent) {
        TransactionSynchronizationManager.registerSynchronization(
            object : TransactionSynchronization {
                override fun afterCommit() {
                    applicationEventPublisher.publishEvent(event)
                }
            }
        )
    }
}
