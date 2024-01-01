package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import pl.dashclever.repairmanagment.estimatecatalogue.VehicleInfo

internal object VehicleInfoBuilder {

    class Builder {
        var model: String = "Model"
        var brand: String = "Brand"
        var registration: String = "XXX11XX2"
    }

    operator fun invoke(init: Builder.() -> Unit): VehicleInfo {
        val builder = Builder()
        builder.init()
        return VehicleInfo(
            registration = builder.registration,
            brand = builder.brand,
            model = builder.model
        )
    }
}
