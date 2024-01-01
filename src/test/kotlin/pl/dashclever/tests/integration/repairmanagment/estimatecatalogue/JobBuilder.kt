package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import pl.dashclever.publishedlanguage.Currency.PLN
import pl.dashclever.publishedlanguage.Money
import pl.dashclever.repairmanagment.estimatecatalogue.Job
import pl.dashclever.repairmanagment.estimatecatalogue.JobType
import pl.dashclever.repairmanagment.estimatecatalogue.JobType.LABOUR

internal object JobBuilder {

    class Builder {
        var description: String = "Some job"
        var jobType: JobType = LABOUR
        var manMinutes: Int = 60
        var worth: Money = Money(1000, PLN)
    }

    operator fun invoke(init: Builder.() -> Unit): Job {
        val builder = Builder()
        builder.init()
        return Job(
            name = builder.description,
            manMinutes = builder.manMinutes,
            worth = builder.worth,
            jobType = builder.jobType
        )
    }
}
