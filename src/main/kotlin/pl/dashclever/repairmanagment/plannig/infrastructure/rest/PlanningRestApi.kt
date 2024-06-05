package pl.dashclever.repairmanagment.plannig.infrastructure.rest

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.repairmanagment.plannig.model.AssignJobHandler
import pl.dashclever.repairmanagment.plannig.model.AssignJobHandler.AssignJob
import pl.dashclever.repairmanagment.plannig.model.Plan
import pl.dashclever.repairmanagment.plannig.model.PlanCreating
import pl.dashclever.repairmanagment.plannig.model.PlanService
import pl.dashclever.repairmanagment.plannig.readmodel.JobDto
import pl.dashclever.repairmanagment.plannig.readmodel.JobReader
import java.net.URI
import java.time.LocalDate
import java.util.*

private const val PATH = "/api/planning"

@RestController
@RequestMapping(PATH, produces = [APPLICATION_JSON_VALUE])
@Tag(name = "planning-api")
internal class PlanningRestApi(
    private val planCreating: PlanCreating,
    private val planService: PlanService,
    private val assignJobHandler: AssignJobHandler,
    private val currentAccessProvider: CurrentAccessProvider,
    private val jobReader: JobReader
) {

    @PostMapping
    fun createFromEstimateId(
        @RequestParam(name = "estimateId", required = true) estimateId: UUID
    ) = ResponseEntity.created(URI.create("$PATH/${planCreating.create(estimateId)}")).build<Plan>()

    @PatchMapping("/{planId}/job")
    fun assignJobs(
        @PathVariable planId: UUID,
        @Valid @RequestBody
        assignments: List<Assignment>
    ): List<JobDto> {
        val commands = assignments.map { AssignJob(planId, it.employeeId, it.catalogueJobId, it.at, it.hour) }.toSet()
        assignJobHandler.handle(commands)
        val currentAccess = currentAccessProvider.currentWorkshopId()
        return jobReader.findByPlanId(currentAccess.workshopId, planId).toList()
    }

    @DeleteMapping("/{planId}/job/{catalogueJobId}")
    @Transactional
    fun removeAssignment(@PathVariable planId: UUID, @PathVariable catalogueJobId: Long) = planService.removeAssignment(planId, catalogueJobId)

    data class Assignment(
        val employeeId: String,
        val catalogueJobId: Long,
        val at: LocalDate,
        val hour: Int?
    )
}
