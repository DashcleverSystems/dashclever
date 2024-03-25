package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import org.springframework.boot.test.context.TestComponent
import org.springframework.data.jpa.repository.JpaRepository
import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import java.util.*

@TestComponent
internal interface EstimateTestsRepository : JpaRepository<Estimate, UUID> {

    fun saveAll(estimates: Set<Estimate>) {
        estimates.forEach { save(it) }
    }
}
