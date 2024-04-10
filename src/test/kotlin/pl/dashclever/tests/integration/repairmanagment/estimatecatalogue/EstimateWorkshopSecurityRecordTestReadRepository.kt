package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import org.springframework.boot.test.context.TestComponent
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre.repository.WorkshopEstimate
import java.util.*

@TestComponent
internal interface EstimateWorkshopSecurityRecordTestReadRepository : Repository<WorkshopEstimate, WorkshopEstimate.ComposePk> {

    @Query("SELECT we FROM WorkshopEstimate we WHERE we.id.workshopId = :workshopId")
    fun findAllByWorkshopId(workshopId: UUID): Set<WorkshopEstimate>
}
