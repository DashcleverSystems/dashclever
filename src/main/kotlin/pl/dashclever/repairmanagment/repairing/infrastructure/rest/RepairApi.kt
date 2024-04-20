package pl.dashclever.repairmanagment.repairing.infrastructure.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.dashclever.repairmanagment.repairing.application.RepairService
import pl.dashclever.spring.TransactionalRunner
import java.util.UUID

private const val PATH = "/api/repairing"

@RestController
@RequestMapping(PATH)
@Tag(name = "repairing-api")
internal class RepairApi(
    private val repairService: RepairService,
    private val transactionalRunner: TransactionalRunner,
) {

    @PostMapping("/repair")
    fun startRepairOfPlan(@RequestParam planId: UUID) =
        transactionalRunner.run { repairService.createRepairOfPlan(planId) }
}
