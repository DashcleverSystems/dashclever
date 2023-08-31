package pl.dashclever.tests.integration.repairmanagment

import pl.dashclever.publishedlanguage.Currency.PLN
import pl.dashclever.publishedlanguage.Money
import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import pl.dashclever.repairmanagment.estimatecatalogue.Job
import pl.dashclever.repairmanagment.estimatecatalogue.JobType.LABOUR
import pl.dashclever.repairmanagment.estimatecatalogue.JobType.VARNISHING
import pl.dashclever.repairmanagment.estimatecatalogue.PaintInfo
import pl.dashclever.repairmanagment.estimatecatalogue.VehicleInfo
import java.math.BigDecimal

internal fun `new estimate`(estimateId: String): Estimate {
    return Estimate(
        estimateId = estimateId,
        paintInfo = PaintInfo(
            baseColorWithCode = "red 222",
            varnishingPaintInfo = "2 layers/ pearl"
        ),
        vehicleInfo = VehicleInfo(
            registration = "zko8gg2",
            brand = "Peugeot",
            model = "307"
        ),
        jobs = setOf(
            Job(
                name = "bumper fix",
                manMinutes = 60,
                worth = Money(
                    denomination = BigDecimal.valueOf(2400),
                    currency = PLN
                ),
                jobType = LABOUR
            ),
            Job(
                name = "paint bumper",
                manMinutes = 120,
                worth = Money(
                    denomination = BigDecimal.valueOf(4800),
                    currency = PLN
                ),
                jobType = VARNISHING
            )
        )
    )
}
