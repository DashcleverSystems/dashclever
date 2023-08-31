package pl.dashclever.accountresources.account.model

import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.Version
import pl.dashclever.accountresources.employee.Employee
import pl.dashclever.publishedlanguage.DomainException
import java.util.UUID
import kotlin.jvm.Throws

@Entity
@Table(name = "ACCOUNT")
data class Account(
    val username: String,
    val passwordHash: String,
    val email: String
) {
    @Id
    val id: UUID = UUID.randomUUID()
    @OneToMany(cascade = [ALL], orphanRemoval = true) @JoinColumn(name = "owner_account_id")
    private val ownerships: MutableSet<Workshop> = mutableSetOf()
    @OneToMany(cascade = [ALL], orphanRemoval = true) @JoinColumn(name = "account_id")
    private val employeeships: MutableSet<Employeeship> = mutableSetOf()
    @Version @Suppress("UnusedPrivateMember")
    private var version: Int = 0

    @Throws(DomainException::class)
    fun createWorkshop(displayName: String): AccountCreatedWorkshop {
        if (this.ownerships.size >= MAXIMUM_ACCOUNTS_WORKSHOPS)
            throw DomainException("Your account can be owner of only two workshops")
        val newWorkshop = Workshop(displayName)
        this.ownerships += newWorkshop
        return AccountCreatedWorkshop(this.id, newWorkshop.id)
    }

    @Throws(DomainException::class)
    fun associateWith(employee: Employee): AddedEmployeeship {
        if (this.employeeships.size >= MAXIMUM_ACCOUNT_EMPLOYEESHIPS)
            throw DomainException("Reached limit of maximum associations")
        if (this.employeeships.any { it.employeeship.employeeId == employee.id })
            throw DomainException("Employee already associated with this account")
        val newEmployeeship = Employeeship(EmployeeshipId(this.id, employee.id))
        this.employeeships += newEmployeeship
        return AddedEmployeeship(this.id, employee.id)
    }

    companion object {
        const val MAXIMUM_ACCOUNT_EMPLOYEESHIPS = 5
        const val MAXIMUM_ACCOUNTS_WORKSHOPS = 2
    }
}
