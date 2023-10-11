package pl.dashclever.repairmanagment.estimatecatalogue

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface EstimateRepository : JpaRepository<pl.dashclever.repairmanagment.estimatecatalogue.Estimate, UUID> {

    fun findByEstimateId(estimateId: String): Optional<pl.dashclever.repairmanagment.estimatecatalogue.Estimate>
}
