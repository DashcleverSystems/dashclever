package pl.dashclever.spring.security.keycloak


import org.keycloak.Config.Scope
import org.keycloak.common.Profile
import org.keycloak.common.profile.PropertiesFileProfileConfigResolver
import org.keycloak.common.profile.PropertiesProfileConfigResolver
import org.keycloak.platform.PlatformProvider
import org.keycloak.services.ServicesLogger
import java.io.File
import kotlin.system.exitProcess


class SimplePlatformProvider : PlatformProvider {

    private var shutdownHook: Runnable? = null

    init {
        Profile.configure(PropertiesProfileConfigResolver(System.getProperties()), PropertiesFileProfileConfigResolver())
    }

    override fun onStartup(startupHook: Runnable) {
        startupHook.run()
    }

    override fun onShutdown(shutdownHook: Runnable?) {
        this.shutdownHook = shutdownHook
    }

    override fun exit(cause: Throwable?) {
        ServicesLogger.LOGGER.fatal(cause)
        object : Thread() {
            override fun run() {
                exitProcess(1)
            }
        }.start()
    }

    override fun getTmpDirectory(): File =
        File(System.getProperty("java.io.tmpdir"))

    override fun getScriptEngineClassLoader(scriptProviderConfig: Scope?): ClassLoader? {
        return null
    }

    override fun name(): String =
        "oauth-authorization-server"
}
