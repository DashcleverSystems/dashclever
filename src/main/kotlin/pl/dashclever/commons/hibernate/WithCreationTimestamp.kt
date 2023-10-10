package pl.dashclever.commons.hibernate

import java.time.LocalDateTime

interface WithCreationTimestamp {

    val createdOn: LocalDateTime?
}
