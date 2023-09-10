package pl.dashclever.repairmanagment.plannig.infrastructure.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.dashclever.repairmanagment.plannig.readmodel.JobDto
import pl.dashclever.repairmanagment.plannig.readmodel.JobReader
import java.util.UUID

private const val PATH = "/api/planning/{planId}/job"

@RestController
@RequestMapping(PATH)
internal class JobReaderController(
    private val jobReader: JobReader
) {

    @GetMapping
    fun findPlanJobs(@PathVariable planId: UUID): Set<JobDto> =
        jobReader.findByPlanId(planId)
}
