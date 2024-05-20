package pl.dashclever.commons.security

interface ApplicationAccessSetter {

    fun set(access: WorkshopOwner): WorkshopOwner

    fun set(access: WorkshopEmployee): WorkshopEmployee
}
