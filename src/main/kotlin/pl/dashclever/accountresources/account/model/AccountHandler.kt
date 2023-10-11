package pl.dashclever.accountresources.account.model

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.accountresources.employee.EmployeeRepository
import java.util.UUID

@Service
class AccountHandler(
    private val accountRepository: AccountRepository,
    private val employeeRepository: EmployeeRepository,
) {

    @Transactional
    fun boundEmployee(boundEmployee: BoundEmployee) {
        val employee = employeeRepository.findById(boundEmployee.employeeId)
            .orElseThrow { ResponseStatusException(NOT_FOUND) }
        val account = accountRepository.findById(boundEmployee.accountId)
            .orElseThrow { ResponseStatusException(NOT_FOUND) }
        account.associateWith(employee)
    }

    @Transactional
    fun createWorkshop(createWorkshop: CreateWorkshop): UUID {
        val account = accountRepository.findById(createWorkshop.accountId)
            .orElseThrow {
                IllegalArgumentException(
                    "Could not find account ${createWorkshop.accountId} while creating workshop"
                )
            }
        return account.createWorkshop(createWorkshop.displayName).workshopId
    }
}
