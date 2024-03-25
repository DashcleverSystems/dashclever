package pl.dashclever.commons.events

import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

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
