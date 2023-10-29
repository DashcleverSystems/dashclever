package pl.dashclever.commons.security

import pl.dashclever.commons.security.Access.WithWorkshopId

fun interface CurrentAccessProvider {

    fun currentAccess(): Access

    fun currentWorkshop(): WithWorkshopId {
        val currentAccess = this.currentAccess()
        return (currentAccess as? WithWorkshopId)
            ?: error("Could not determine current access workshop: $currentAccess")
    }
}
