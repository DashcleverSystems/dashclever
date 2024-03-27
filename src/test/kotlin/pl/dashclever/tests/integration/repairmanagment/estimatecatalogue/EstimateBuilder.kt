package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import pl.dashclever.repairmanagment.estimatecatalogue.Job
import pl.dashclever.repairmanagment.estimatecatalogue.PaintInfo
import pl.dashclever.repairmanagment.estimatecatalogue.VehicleInfo
import java.util.*

internal object EstimateBuilder {

    class Builder {
        var estimateName: String = "01/2022WK"
        var vehicleInfo: VehicleInfo = VehicleInfoBuilder { }
        var paintInfo: PaintInfo = PaintInfoBuilder { }
        var jobs: Set<Job> = emptySet()
    }

    operator fun invoke(init: Builder.() -> Unit): Estimate {
        val builder = Builder()
        builder.init()
        return Estimate(
            name = builder.estimateName,
            vehicleInfo = builder.vehicleInfo,
            paintInfo = builder.paintInfo,
            jobs = builder.jobs
        )
    }
}
