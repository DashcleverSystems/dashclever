package pl.dashclever.commons.security

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

object LocalDateTimeHelper {

    fun LocalDateTime.timezoned(zoneId: ZoneId): ZonedDateTime =
        this.atZone(ZoneId.of("GMT")).withZoneSameInstant(zoneId)
}
