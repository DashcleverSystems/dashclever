package pl.dashclever.commons.security

interface CurrentAccessProvider {

    fun currentAccountId(): WithAccountId

    fun currentWorkshopId(): WithWorkshopId
}
