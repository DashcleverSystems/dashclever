package pl.dashclever.spring.security.keycloak

import io.github.oshai.kotlinlogging.KotlinLogging
import org.keycloak.Config
import org.keycloak.exportimport.ExportImportManager
import org.keycloak.services.managers.ApplianceBootstrap
import org.keycloak.services.resources.KeycloakApplication
import org.keycloak.services.util.JsonConfigProviderFactory
import pl.dashclever.spring.security.keycloak.KeycloakServerProperties.AdminUser


private val logger = KotlinLogging.logger { }

class EmbeddedKeycloakApplication : KeycloakApplication() {

    override fun loadConfig() {
        val factory: JsonConfigProviderFactory = RegularJsonConfigProviderFactory()
        Config.init(factory.create()
            .orElseThrow { NoSuchElementException("No value present") })
    }

    override fun bootstrap(): ExportImportManager {
        val exportImportManager: ExportImportManager = super.bootstrap()
        createMasterRealmAdminUser()
        createDashcleverRealm()
        return exportImportManager
    }

    private fun createMasterRealmAdminUser() {
        val session = getSessionFactory().create()

        val applianceBootstrap = ApplianceBootstrap(session)

        val admin: AdminUser = keycloakServerProperties!!.adminUser

        try {
            session.transactionManager.begin()
            applianceBootstrap.createMasterRealmUser(admin.username, admin.password)
            session.transactionManager.commit()
        } catch (ex: Exception) {
            logger.warn { "Couldn't create keycloak master admin user: ${ex.message}" }
            session.transactionManager.rollback()
        }

        session.close()
    }

    private fun createDashcleverRealm() {
//        val session = getSessionFactory().create()
//
//        try {
//            session.transactionManager.begin()
//
//            val manager = RealmManager(session)
//            val lessonRealmImportFile: Resource = ClassPathResource(keycloakServerProperties!!.realmImportFile)
//
//            manager.importRealm(
//                JsonSerialization.readValue(lessonRealmImportFile.inputStream, RealmRepresentation::class.java)
//            )
//            session.transactionManager.commit()
//        } catch (ex: Exception) {
//            logger.warn { "Failed to import Realm json file: ${ex.message}" }
//            session.transactionManager.rollback()
//        }
//
//        session.close()
    }

    companion object {
        var keycloakServerProperties: KeycloakServerProperties? = null
    }
}
