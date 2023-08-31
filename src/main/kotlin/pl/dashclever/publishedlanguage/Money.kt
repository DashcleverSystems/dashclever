package pl.dashclever.publishedlanguage

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import java.math.BigDecimal

@Embeddable
data class Money(
    val denomination: BigDecimal,
    @Enumerated(STRING) val currency: Currency
)

enum class Currency {
    PLN,
    EUR
}
