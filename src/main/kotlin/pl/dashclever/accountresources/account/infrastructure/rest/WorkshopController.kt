package pl.dashclever.accountresources.account.infrastructure.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.accountresources.account.readmodel.WorkshopDto
import pl.dashclever.accountresources.account.readmodel.WorkshopReader
import java.util.UUID

private const val PATH = "/api/workshop"

@RestController
@RequestMapping(PATH)
internal class WorkshopController(
    private val workshopReader: WorkshopReader
) {

    @GetMapping("/{workshopId}")
    fun findById(@PathVariable workshopId: UUID): WorkshopDto {
        return workshopReader.findById(workshopId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
    }
}
