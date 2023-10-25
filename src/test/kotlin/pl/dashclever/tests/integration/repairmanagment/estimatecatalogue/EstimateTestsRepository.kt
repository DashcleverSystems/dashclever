package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import java.util.UUID

@Component
internal interface EstimateTestsRepository : JpaRepository<Estimate, UUID> {

    fun saveAll(estimates: Set<Estimate>) {
        estimates.forEach { save(it) }
    }
}
