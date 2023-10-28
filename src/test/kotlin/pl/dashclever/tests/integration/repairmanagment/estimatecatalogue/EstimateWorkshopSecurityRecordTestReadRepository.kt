package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre.repository.WorkshopEstimate
import java.util.UUID

@Component
internal interface EstimateWorkshopSecurityRecordTestReadRepository : Repository<WorkshopEstimate, WorkshopEstimate.ComposePk> {

    @Query("SELECT we FROM WorkshopEstimate we WHERE we.id.workshopId = :workshopId")
    fun findAllByWorkshopId(workshopId: UUID): Set<WorkshopEstimate>
}
