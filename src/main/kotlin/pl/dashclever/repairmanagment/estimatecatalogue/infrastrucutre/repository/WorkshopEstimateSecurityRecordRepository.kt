package pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre.repository

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
import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import java.io.Serializable
import java.util.*

@Component
interface EstimateWorkshopSecurityRecordRepository :
    EntitySecurityRecordRepository<Estimate, UUID, WorkshopEstimate>,
    Repository<WorkshopEstimate, WorkshopEstimate.ComposePk> {

    override fun create(securityRecord: WorkshopEstimate): WorkshopEstimate = save(securityRecord)

    fun save(securityRecord: WorkshopEstimate): WorkshopEstimate

    override fun doesSecurityRecordExistFor(entity: Estimate): Boolean = existsByEstimateId(entity.id)

    @Query(
        value = """
            SELECT
                CASE WHEN EXISTS (
                    SELECT 1
                    FROM SR_WORKSHOP_ESTIMATE sr
                    WHERE sr.estimate_id = :estimateId
                )
            THEN 'true'
            ELSE 'false'
            END
    """,
        nativeQuery = true
    )
    fun existsByEstimateId(estimateId: UUID): Boolean

    override fun deleteByEntityId(entityId: UUID) = deleteByEstimateId(entityId)

    @Modifying
    @Query("DELETE FROM WorkshopEstimate WHERE id.estimateId = :estimateId")
    fun deleteByEstimateId(estimateId: UUID)
}

@Entity
@Table(name = "SR_WORKSHOP_ESTIMATE")
class WorkshopEstimate(
    workshopId: UUID,
    estimateId: UUID
) : WorkshopSecurityRecord {

    @EmbeddedId
    private val id: ComposePk = ComposePk(workshopId, estimateId)

    override val workshopId get() = id.workshopId

    val estimateId get() = id.estimateId

    @Embeddable
    data class ComposePk(
        @Column(name = "workshop_id")
        val workshopId: UUID,
        @Column(name = "estimate_id")
        val estimateId: UUID
    ) : Serializable {

        companion object {

            const val serialVersionUID = 45L
        }
    }
}
