package pl.dashclever.commons.exception

data class DomainException(override val message: String) : RuntimeException(message)
