package pl.dashclever.repairmanagment.plannig.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.plannig.model.Plan
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Component
interface PlanReader : Repository<Plan, UUID> {

    @Query(
        value =
        """
        SELECT p.id AS id, p.estimate_id AS estimateId, SUM(j.man_minutes) AS technicalRepairTimeInMinutes, p.created_on AS createdOn, e.estimate_id AS estimateName
        FROM RM_PLANNING_PLAN p INNER JOIN RM_PLANNING_JOB j ON j.plan_id = p.id
        INNER JOIN RM_SR_WORKSHOP_PLAN sr ON sr.workshop_id = :workshopId
        INNER JOIN RM_ESTIMATECATALOGUE_ESTIMATE e ON e.id = p.estimate_id
        WHERE p.id = :id
        GROUP BY p.id, e.estimate_id
        """,
        nativeQuery = true
    )
    fun findById(workshopId: UUID, id: UUID): Optional<PlanDto>

    @Query(
        value =
        """
        SELECT p.id AS id, p.estimate_id AS estimateId, SUM(j.man_minutes) AS technicalRepairTimeInMinutes, p.created_on AS createdOn, e.estimate_id AS estimateName
        FROM RM_PLANNING_PLAN p INNER JOIN RM_PLANNING_JOB j ON j.plan_id = p.id
        INNER JOIN RM_SR_WORKSHOP_PLAN sr ON sr.plan_id = p.id
        INNER JOIN RM_ESTIMATECATALOGUE_ESTIMATE e ON e.id = p.estimate_id
        WHERE p.estimate_id = :estimateId AND sr.workshop_id = :workshopId
        GROUP BY p.id, e.estimate_id
        """,
        nativeQuery = true
    )
    fun findByEstimateId(workshopId: UUID, estimateId: UUID): Set<PlanDto>

    @Query(
        value =
        """
        SELECT p.id AS id, p.estimate_id AS estimateId, SUM(j.man_minutes) AS technicalRepairTimeInMinutes, p.created_on AS createdOn, e.estimate_id AS estimateName
        FROM RM_PLANNING_PLAN p
        INNER JOIN RM_PLANNING_JOB j ON j.plan_id = p.id
        INNER JOIN RM_SR_WORKSHOP_PLAN sr ON sr.plan_id = p.id
        INNER JOIN RM_ESTIMATECATALOGUE_ESTIMATE e ON e.id = p.estimate_id
        WHERE (SELECT MIN(yj.assigned_at) FROM RM_PLANNING_JOB yj WHERE yj.plan_id = p.id) >= :from
        AND (SELECT MIN(yj.assigned_at) FROM RM_PLANNING_JOB yj WHERE yj.plan_id = p.id) <= :to
        AND sr.workshop_id = :workshopId
        GROUP BY p.id, e.estimate_id
        """,
        nativeQuery = true
    )
    fun findByDateRange(workshopId: UUID, from: LocalDate, to: LocalDate): Set<PlanDto>
}

interface PlanDto {

    val id: UUID
    val estimateName: String
    val estimateId: String
    val technicalRepairTimeInMinutes: Int
    val createdOn: LocalDateTime
}
