package pl.dashclever.commons.security

fun interface CurrentAccessProvider {

    fun currentAccess(): Access?
}
