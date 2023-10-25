package pl.dashclever.publishedlanguage

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated

@Embeddable
data class Money(
    val denomination: Long,
    @Enumerated(STRING) val currency: Currency
)

enum class Currency {
    PLN,
    EUR
}
