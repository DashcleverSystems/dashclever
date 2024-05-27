package pl.dashclever.spring.events.cloudstreams

import pl.dashclever.commons.events.DomainEvent
import pl.dashclever.commons.events.DomainEvents
import pl.dashclever.commons.security.CurrentAccessProvider
import java.util.UUID

class CloudStreamDomainEventsMultitenantProxy(
    private val cloudStreamDomainEvents: CloudStreamDomainEvents,
    private val currentAccessProvider: CurrentAccessProvider
) : DomainEvents {

    override fun publish(event: DomainEvent) {
        if (event.isWorkshopSpecificDomainEvent().not()) {
            cloudStreamDomainEvents.publish(event)
        } else {
            val currentWorkshop = currentAccessProvider.currentWorkshopId()
            cloudStreamDomainEvents.publish(
                MultitenantDomainEvent(currentWorkshop.workshopId, event)
            )
        }
    }

    data class MultitenantDomainEvent<T>(
        val workshopId: UUID,
        val event: T
    ) : DomainEvent

    private fun DomainEvent.isWorkshopSpecificDomainEvent(): Boolean {
        when (this) {
            else -> error("Could not deduce if $this event of type ${this::class.simpleName} is workshop specific or not.")
        }
    }
}
