package pl.dashclever.commons.events

import java.util.*

interface DomainEvents {
    fun publish(event: DomainEvent)

    fun publish(events: List<DomainEvent>) = events.forEach(::publish)

}

interface DomainEvent {
    val id: UUID
}
