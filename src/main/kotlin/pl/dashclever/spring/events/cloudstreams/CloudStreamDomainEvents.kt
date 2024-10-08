package pl.dashclever.spring.events.cloudstreams

import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import pl.dashclever.commons.events.DomainEvent
import pl.dashclever.commons.events.DomainEvents
import pl.dashclever.spring.events.cloudstreams.CloudStreamDomainEventsMultitenantProxy.MultitenantDomainEvent

class CloudStreamDomainEvents(
    private val streamBridge: StreamBridge
) : DomainEvents {

    override fun publish(event: DomainEvent) {
        val bindingName = if (event is MultitenantDomainEvent<*>) {
            "${event.event!!.javaClass.simpleName}-out-0"
        } else {
            "${event.javaClass.simpleName}-out-0"
        }
        if (TransactionSynchronizationManager.isActualTransactionActive().not()) {
            streamBridge.send(bindingName, event)
            return
        }
        TransactionSynchronizationManager.registerSynchronization(
            object : TransactionSynchronization {
                override fun afterCommit() {
                    streamBridge.send(bindingName, event)
                }
            }
        )
    }
}
