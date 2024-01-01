package pl.dashclever.repairmanagment.estimatecatalogue

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime
import java.util.UUID

interface EstimateRepository {

    fun save(estimate: Estimate): Estimate

    fun findById(id: UUID): Estimate?

    fun findAll(specification: Specification<Estimate>, pageable: Pageable): Page<Estimate>

    fun findAll(pageable: Pageable): Page<Estimate>

    fun existsByEstimateId(estimateId: String): Boolean

    fun deleteById(id: UUID)

    object EstimateSpecifications {

        fun createdOnAfter(date: LocalDateTime): Specification<Estimate> {
            return Specification<Estimate> { root, _, cb -> cb.greaterThan(root.get("createdOn"), date) }
        }

        fun estimateId(estimateId: String): Specification<Estimate> {
            return Specification<Estimate> { root, _, cb -> cb.equal(root.get<String>("estimateId"), estimateId) }
        }
    }
}
