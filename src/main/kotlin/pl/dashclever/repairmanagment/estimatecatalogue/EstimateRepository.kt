package pl.dashclever.repairmanagment.estimatecatalogue

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
interface EstimateRepository : JpaSpecificationExecutor<Estimate>, Repository<Estimate, UUID> {

    fun save(estimate: Estimate): Estimate

    fun findById(id: UUID): Estimate?

    fun findAll(pageable: Pageable): Page<Estimate>

    fun existsByEstimateId(estimateId: String): Boolean

    fun deleteById(id: UUID)
}

object EstimateSpecifications {

    fun createdOnAfter(date: LocalDateTime): Specification<Estimate> {
        return Specification<Estimate> { root, _, cb -> cb.greaterThan(root.get("createdOn"), date) }
    }

    fun estimateId(estimateId: String): Specification<Estimate> {
        return Specification<Estimate> { root, _, cb -> cb.equal(root.get<String>("estimateId"), estimateId) }
    }
}

// @org.springframework.stereotype.Repository
// private class WorkshopSecureEstimateRepository(
//    private val
//    private var authentication: Authentication
// ): EstimateRepository, Repository<Estimate, UUID> {
//
//    override fun save(estimate: Estimate): Estimate {
//
//    }
//
//    override fun findAll(specification: Specification<Estimate>, pageRequest: PageRequest): Set<Estimate> {
//        this.findAll()
//    }
//
//    override fun existsByEstimateId(estimateId: String): Boolean {
//        TODO("Not yet implemented")
//    }
// }
//
// @Configuration
// private class EstimateRepoConfig(
//    private val jpaEstimateRepo: JpaEstimateRepo,
// ) {
//
//    @Bean
//    fun estimateRepository() {
//        return Es
//    }
// }
//
// @org.springframework.stereotype.Repository
// private interface JpaEstimateRepo : Repository<Estimate, UUID> {
// }
