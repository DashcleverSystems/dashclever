package pl.dashclever.repairmanagment.repairing.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "RM_REPAIRING_REPAIR")
class Repair(
    val planId: UUID,
) {

    @Id
    val id: UUID = UUID.randomUUID()
}
