package pl.dashclever.commons.hibernate

import java.time.LocalDateTime

interface WithLastModificationTimestamp {

    val lastModifiedOn: LocalDateTime?
}
