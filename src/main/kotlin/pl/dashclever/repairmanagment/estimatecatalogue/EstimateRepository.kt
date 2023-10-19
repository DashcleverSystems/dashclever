package pl.dashclever.repairmanagment.estimatecatalogue

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface EstimateRepository : JpaSpecificationExecutor<Estimate>, JpaRepository<Estimate, UUID> {

    fun existsByEstimateId(estimateId: String): Boolean
}

object EstimateSpecifications {

    fun createdOnAfter(date: LocalDateTime): Specification<Estimate> {
        return Specification<Estimate> { root, _, cb -> cb.greaterThan(root.get("createdOn"), date) }
    }

    fun estimateId(estimateId: String): Specification<Estimate> {
        return Specification<Estimate> { root, _, cb -> cb.equal(root.get<String>("estimateId"), estimateId) }
    }
}
