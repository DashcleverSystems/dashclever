package pl.dashclever.repairmanagment.plannig.readmodel.impl

import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.repairmanagment.plannig.readmodel.PlanDto
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader
import java.time.LocalDate
import java.util.UUID
import javax.sql.DataSource

@Component
class PlanReaderImpl(
    dataSource: DataSource
) : PlanReader {

    private companion object {
        val PLAN_ROW_MAPPER = PlanEntityRowMapper()
    }

    private val jdbcTemplate = JdbcTemplate(dataSource)
    private val jobFinder = JobFinder(dataSource)

    override fun findById(id: UUID): PlanDto {
        val query = """
            SELECT
                    ${PlanEntity.ID},
                    ${PlanEntity.ESTIMATE_ID}
            FROM
                    ${PlanEntity.TABLE}
            WHERE
                    ${PlanEntity.ID} = ?
        """.trimIndent()
        val plan: PlanEntity = jdbcTemplate.query(
            query,
            PLAN_ROW_MAPPER,
            id
        ).singleOrNull() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val jobs = jobFinder.findByPlanId(plan.id)
            .map { it.toDto() }
            .toSet()
        return plan.toDto(jobs)
    }

    override fun findByEstimateId(estimateId: String): Set<PlanDto> {
        val query = """
            SELECT
                    ${PlanEntity.ID},
                    ${PlanEntity.ESTIMATE_ID}
            FROM
                    ${PlanEntity.TABLE}
            WHERE
                    ${PlanEntity.ESTIMATE_ID} = ?
        """.trimIndent()
        val planJobs = jdbcTemplate.query(
            query,
            PLAN_ROW_MAPPER,
            estimateId
        ).associateWith { jobFinder.findByPlanId(it.id) }
        return planJobs.map { (plan, jobs) ->
            plan.toDto(
                jobs = jobs.map { it.toDto() }.toSet()
            )
        }.toSet()
    }

    override fun findByDateRange(from: LocalDate, to: LocalDate): Set<PlanDto> {
        val query = """
            SELECT
                    ${PlanEntity.ID},
                    ${PlanEntity.ESTIMATE_ID}
            FROM
                    ${PlanEntity.TABLE}
            WHERE
                    ${PlanEntity.ID} in(

                    SELECT
                            DISTINCT ${JobEntity.PLAN_ID}
                    FROM
                            ${JobEntity.TABLE}
                    WHERE
                            ${JobEntity.ASSIGNED_AT} >= ? AND ${JobEntity.ASSIGNED_AT} <= ?
                    )
        """.trimIndent()
        val planJobs = jdbcTemplate.query(
            query,
            PLAN_ROW_MAPPER,
            from, to
        ).associateWith { jobFinder.findByPlanId(it.id) }
        return planJobs.map { (plan, jobs) ->
            plan.toDto(
                jobs = jobs.map { it.toDto() }.toSet()
            )
        }.toSet()
    }
}
