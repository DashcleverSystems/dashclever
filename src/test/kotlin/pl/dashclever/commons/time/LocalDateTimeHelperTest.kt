package pl.dashclever.commons.time

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.dashclever.commons.time.LocalDateTimeHelper.asGmt
import pl.dashclever.commons.time.LocalDateTimeHelper.timezoned
import java.time.LocalDateTime
import java.time.ZoneId

internal class LocalDateTimeHelperTest {

    @Test
    fun `should change to other timezone`() {
        // given
        val localDateTime = LocalDateTime.of(2023, 1, 1, 12, 0)

        // when
        val result = localDateTime.timezoned(ZoneId.of("UTC-1"))

        // then
        assertThat(result.zone).isEqualTo(ZoneId.of("UTC-1"))
        assertThat(result.toLocalDateTime()).isEqualTo(LocalDateTime.of(2023, 1, 1, 11, 0))
    }

    @Test
    fun `should return same local date time timezoned as GMT`() {
        // given
        val localDateTime = LocalDateTime.of(2023, 1, 1, 12, 0)

        // when
        val result = localDateTime.asGmt()

        // then
        assertThat(result.zone).isEqualTo(ZoneId.of("GMT"))
        assertThat(result.toLocalDateTime()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0))
    }
}
