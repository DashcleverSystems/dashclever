package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import pl.dashclever.repairmanagment.estimatecatalogue.PaintInfo

internal object PaintInfoBuilder {

    class Builder {
        var baseColorWithCode: String = "White 1"
        var varnishingPaintInfo: String = "pearl"
    }

    operator fun invoke(init: Builder.() -> Unit): PaintInfo {
        val builder = Builder()
        builder.init()
        return PaintInfo(
            baseColorWithCode = builder.baseColorWithCode,
            varnishingPaintInfo = builder.varnishingPaintInfo
        )
    }
}
