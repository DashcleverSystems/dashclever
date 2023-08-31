package pl.dashclever.repairmanagment.plannig.readmodel.impl

import org.springframework.jdbc.core.JdbcTemplate
import java.util.UUID
import javax.sql.DataSource

internal class JobFinder(
    dataSource: DataSource
) {

    private companion object {
        val JOB_ROW_MAPPER = JobEntityRowMapper()
    }

    private val jdbcTemplate = JdbcTemplate(dataSource)

    fun findByPlanId(id: UUID): Set<JobEntity> {
        val query = """
            SELECT
                    ${JobEntity.ID},
                    ${JobEntity.CATALOGUE_JOB_ID},
                    ${JobEntity.MAN_MINUTES},
                    ${JobEntity.ASSIGNED_AT},
                    ${JobEntity.ASSIGNED_TO},
                    ${JobEntity.HOUR},
                    ${JobEntity.PLAN_ID}
            FROM
                    ${JobEntity.TABLE}
            WHERE
                    ${JobEntity.PLAN_ID} = ?
        """.trimIndent()
        return jdbcTemplate.query(
            query,
            JOB_ROW_MAPPER,
            id
        ).toSet()
    }
}
