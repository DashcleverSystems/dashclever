package pl.dashclever.commons.security

import java.time.LocalDateTime
import java.time.ZoneId

object LocalDateTimeHelper {

    fun LocalDateTime.timezoned(zoneId: ZoneId): LocalDateTime =
        this.atZone(ZoneId.of("GMT")).withZoneSameInstant(zoneId).toLocalDateTime()
}
