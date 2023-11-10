package pl.dashclever.repairmanagment.plannig.infrastructure.rest

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.repairmanagment.plannig.infrastructure.rest.requestes.AssignReq
import pl.dashclever.repairmanagment.plannig.model.Plan
import pl.dashclever.repairmanagment.plannig.model.PlanCreating
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import java.net.URI
import java.util.UUID

private const val PATH = "/api/planning"

@RestController
@RequestMapping(PATH)
@Tag(name = "planning-api")
internal class PlanningRestApi(
    private val planRepository: PlanRepository,
    private val planCreating: PlanCreating
) {

    @PatchMapping("/{planId}/job/{jobId}")
    @Transactional
    @ResponseStatus(NO_CONTENT)
    fun assignJob(
        @PathVariable planId: UUID,
        @PathVariable jobId: Long,
        @Valid @RequestBody
        assignReq: AssignReq
    ) {
        val plan = planRepository.findById(planId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        if (assignReq.hour == null) {
            plan.assign(jobId, assignReq.to, assignReq.at)
        } else {
            plan.assignWithTime(jobId, assignReq.to, assignReq.at, assignReq.hour)
        }
        planRepository.save(plan)
    }

    @PostMapping
    fun createFromEstimateId(
        @RequestParam(name = "estimateId", required = true) estimateId: UUID
    ) = ResponseEntity.created(URI.create("$PATH/${planCreating.create(estimateId)}")).build<Plan>()
}
