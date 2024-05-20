package pl.dashclever.commons.events

interface DomainEvents {

    fun publish(event: DomainEvent)

    fun publish(events: List<DomainEvent>) = events.forEach(::publish)
}

interface DomainEvent
