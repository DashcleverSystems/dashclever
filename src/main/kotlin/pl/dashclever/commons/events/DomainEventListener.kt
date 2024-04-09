package pl.dashclever.commons.events

interface DomainEventListener<T> {

    fun handle(event: T)
}
