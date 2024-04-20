package pl.dashclever.repairmanagment.repairing.application

import pl.dashclever.repairmanagment.repairing.model.Repair

interface RepairRepository {

    fun save(repair: Repair)
}
