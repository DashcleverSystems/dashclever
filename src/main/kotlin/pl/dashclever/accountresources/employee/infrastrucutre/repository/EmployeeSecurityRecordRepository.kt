package pl.dashclever.accountresources.employee.infrastrucutre.repository

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.accountresources.employee.Employee
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.commons.security.WorkshopSecurityRecord
import java.io.Serializable
import java.util.UUID

@Component
interface EmployeeSecurityRecordRepository :
    EntitySecurityRecordRepository<Employee, UUID, WorkshopEmployee>,
    Repository<WorkshopEmployee, WorkshopEmployee.ComposePk> {

    override fun create(securityRecord: WorkshopEmployee): WorkshopEmployee =
        save(securityRecord)

    fun save(securityRecord: WorkshopEmployee): WorkshopEmployee

    override fun doesSecurityRecordExistFor(entity: Employee): Boolean =
        existsByEmployeeId(entity.id)

    @Query(
        value = """
            SELECT
                CASE WHEN EXISTS (
                    SELECT 1
                    FROM AR_SR_WORKSHOP_EMPLOYEE sr
                    WHERE sr.employee_id = :employeeId
                )
            THEN 'true'
            ELSE 'false'
            END
    """,
        nativeQuery = true
    )
    fun existsByEmployeeId(employeeId: UUID): Boolean

    override fun deleteByEntityId(entityId: UUID) =
        deleteByEmployeeId(entityId)

    @Modifying
    @Query("DELETE FROM WorkshopEmployee we WHERE we.id.employeeId = :employeeId")
    fun deleteByEmployeeId(employeeId: UUID)
}

@Entity
@Table(name = "AR_SR_WORKSHOP_EMPLOYEE")
class WorkshopEmployee(
    workshopId: UUID,
    employeeId: UUID
) : WorkshopSecurityRecord {

    @EmbeddedId
    private val id: ComposePk = ComposePk(workshopId, employeeId)

    override val workshopId get() = id.workshopId

    val employeeId get() = id.employeeId

    @Embeddable
    data class ComposePk(
        @Column(name = "workshop_id")
        val workshopId: UUID,
        @Column(name = "employee_id")
        val employeeId: UUID
    ) : Serializable {

        companion object {
            const val serialVersionUID = 99L
        }
    }
}
