package pl.dashclever.repairmanagment.plannig.infrastructure.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.dashclever.repairmanagment.plannig.readmodel.JobDto
import pl.dashclever.repairmanagment.plannig.readmodel.JobReader
import java.util.UUID

private const val PATH = "/api/planning/{planningId}/job"

@RestController
@RequestMapping(PATH)
internal class PlanJobsController(
    private val jobsReader: JobReader
) {

    @GetMapping
    fun getAllByPlanningId(@PathVariable planningId: UUID): Set<JobDto> {
        return jobsReader.findByPlanId(planningId)
    }
}
