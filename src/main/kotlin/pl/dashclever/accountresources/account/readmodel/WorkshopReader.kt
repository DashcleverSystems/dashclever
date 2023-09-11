package pl.dashclever.accountresources.account.readmodel

import java.util.Optional
import java.util.UUID

interface WorkshopReader {

    fun findById(id: UUID): Optional<WorkshopDto>
}
