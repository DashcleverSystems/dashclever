package pl.dashclever.accountresources.account.readmodel.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import pl.dashclever.accountresources.account.readmodel.WorkshopDto
import pl.dashclever.accountresources.account.readmodel.WorkshopReader
import java.util.Optional
import java.util.UUID

@Component
class WorkshopReaderImpl(
    private val jdbcTemplate: JdbcTemplate
) : WorkshopReader {
    override fun findById(id: UUID): Optional<WorkshopDto> {
        val query = """
            SELECT
                    id,
                    display_name
            FROM
                    WORKSHOP
            WHERE
                    id = ?
        """.trimIndent()
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                query,
                { rs, _ ->
                    WorkshopDto(
                        id = UUID.fromString(rs.getString("id")),
                        displayName = rs.getString("display_name")
                    )
                },
                id
            )
        )
    }
}
