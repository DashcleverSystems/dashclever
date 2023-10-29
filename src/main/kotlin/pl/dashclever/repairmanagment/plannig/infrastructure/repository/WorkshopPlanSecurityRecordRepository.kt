package pl.dashclever.repairmanagment.plannig.infrastructure.repository

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.commons.security.WorkshopSecurityRecord
import pl.dashclever.repairmanagment.plannig.model.Plan
import java.io.Serializable
import java.util.UUID

@Component
interface WorkshopPlanSecurityRecordRepository :
    EntitySecurityRecordRepository<Plan, UUID, WorkshopPlan>,
    Repository<WorkshopPlan, WorkshopPlan.ComposePk> {

    override fun create(securityRecord: WorkshopPlan): WorkshopPlan = save(securityRecord)

    fun save(securityRecord: WorkshopPlan): WorkshopPlan

    override fun doesSecurityRecordExistFor(entity: Plan): Boolean = existsByPlanId(entity.id)

    @Query(
        value = """
            SELECT
                CASE WHEN EXISTS (
                    SELECT 1
                    FROM RM_SR_WORKSHOP_PLAN sr
                    WHERE sr.plan_id = :planId
                )
            THEN 'true'
            ELSE 'false'
            END
    """,
        nativeQuery = true
    )
    fun existsByPlanId(planId: UUID): Boolean

    override fun deleteByEntityId(entityId: UUID) = deleteByEstimateId(entityId)

    @Modifying
    @Query("DELETE FROM WorkshopPlan WHERE id.planId = :planId")
    fun deleteByEstimateId(planId: UUID)
}

@Entity
@Table(name = "RM_SR_WORKSHOP_PLAN")
class WorkshopPlan(
    workshopId: UUID,
    planId: UUID
) : WorkshopSecurityRecord {

    @EmbeddedId
    private val id: ComposePk = ComposePk(workshopId, planId)

    override val workshopId get() = id.workshopId

    val planId get() = id.planId

    @Embeddable
    data class ComposePk(
        @Column(name = "workshop_id")
        val workshopId: UUID,
        @Column(name = "plan_id")
        val planId: UUID
    ) : Serializable {

        companion object {

            const val serialVersionUID = 46L
        }
    }
}
