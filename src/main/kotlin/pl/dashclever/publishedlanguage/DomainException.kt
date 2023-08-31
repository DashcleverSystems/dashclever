package pl.dashclever.publishedlanguage

data class DomainException(override val message: String) : RuntimeException(message)
