package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import java.util.UUID

@Component
internal interface EstimateTestsRepository : Repository<Estimate, UUID> {

    fun saveAll(estimates: Set<Estimate>)

    fun deleteAll()
}
