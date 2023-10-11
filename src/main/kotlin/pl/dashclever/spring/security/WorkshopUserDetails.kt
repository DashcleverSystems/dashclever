package pl.dashclever.spring.security

import org.springframework.security.core.GrantedAuthority
import pl.dashclever.accountresources.account.readmodel.Authority
import java.util.UUID

data class WorkshopUserDetails(
    override val id: UUID,
    private val username: String,
    val workshopId: UUID,
    val employeeId: UUID?,
    private val authorities: Set<Authority>,
) : IdUserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        authorities.map { GrantedAuthority { it.toString() } }.toMutableSet()

    override fun getPassword(): String {
        throw IllegalAccessException("For workshop specific user details password is not accessible")
    }

    override fun getUsername(): String =
        this.username

    override fun isAccountNonExpired(): Boolean =
        true

    override fun isAccountNonLocked(): Boolean =
        true

    override fun isCredentialsNonExpired(): Boolean =
        true

    override fun isEnabled(): Boolean =
        true

    fun isUserOfWorkshop(workshopId: UUID): Boolean {
        return this.workshopId == workshopId
    }
}
