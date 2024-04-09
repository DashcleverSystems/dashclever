package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import pl.dashclever.repairmanagment.estimatecatalogue.Job
import pl.dashclever.repairmanagment.estimatecatalogue.PaintInfo
import pl.dashclever.repairmanagment.estimatecatalogue.VehicleInfo

internal object EstimateBuilder {

    class Builder {
        var estimateName: String = "01/2022WK"
        var customerName: String = "Test customer"
        var vehicleInfo: VehicleInfo = VehicleInfoBuilder { }
        var paintInfo: PaintInfo = PaintInfoBuilder { }
        var jobs: Set<Job> = emptySet()
    }

    operator fun invoke(init: Builder.() -> Unit): Estimate {
        val builder = Builder()
        builder.init()
        return Estimate(
            name = builder.estimateName,
            customerName = builder.customerName,
            vehicleInfo = builder.vehicleInfo,
            paintInfo = builder.paintInfo,
            jobs = builder.jobs
        )
    }
}
