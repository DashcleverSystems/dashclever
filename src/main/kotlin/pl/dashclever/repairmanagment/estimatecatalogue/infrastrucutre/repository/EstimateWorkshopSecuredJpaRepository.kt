package pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import java.util.UUID

@Component
interface EstimateWorkshopSecuredJpaRepository :
    Repository<Estimate, UUID>,
    JpaSpecificationExecutor<Estimate> {

    @Query("SELECT e FROM Estimate e INNER JOIN WorkshopEstimate sr ON sr.id.estimateId = e.id WHERE sr.id.workshopId = :currentAccessWorkshopId AND e.id = :estimateId")
    fun findById(currentAccessWorkshopId: UUID, estimateId: UUID): Estimate?

    @Query(
        value = "SELECT e FROM Estimate e INNER JOIN WorkshopEstimate we ON e.id = we.id.estimateId WHERE we.id.workshopId = :currentAccessWorkshopId",
        countQuery = "SELECT COUNT(e.id) FROM Estimate e INNER JOIN WorkshopEstimate we ON e.id = we.id.estimateId WHERE we.id.workshopId = :currentAccessWorkshopId GROUP BY e.id"
    )
    fun findAll(
        currentAccessWorkshopId: UUID,
        pageable: Pageable
    ): Page<Estimate>

    @Query(
        value = """
        SELECT
            CASE WHEN EXISTS (
                SELECT 1
                FROM RM_ESTIMATECATALOGUE_ESTIMATE e
                INNER JOIN SR_WORKSHOP_ESTIMATE sr ON sr.estimate_id = e.id
                WHERE e.name = :estimateName AND sr.workshop_id = :currentAccessWorkshopId
            )
            THEN 'true'
            ELSE 'false'
            END
        """,
        nativeQuery = true
    )
    fun existsByEstimateName(currentAccessWorkshopId: UUID, estimateName: String): Boolean

    @Modifying
    @Query(
        value = """
        DELETE FROM RM_ESTIMATECATALOGUE_ESTIMATE e
        USING SR_WORKSHOP_ESTIMATE sr
        WHERE sr.workshop_id = :currentAccessWorkshopId AND sr.estimate_id = e.id AND e.id = :id
    """,
        nativeQuery = true
    )
    fun deleteById(currentAccessWorkshopId: UUID, id: UUID)
}
