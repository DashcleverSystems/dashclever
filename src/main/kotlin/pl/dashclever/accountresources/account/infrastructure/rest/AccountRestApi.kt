package pl.dashclever.accountresources.account.infrastructure.rest

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.accountresources.account.infrastructure.AccountService
import pl.dashclever.accountresources.account.infrastructure.rest.requests.AccessReq
import pl.dashclever.accountresources.account.infrastructure.rest.requests.CreateWorkshopReq
import pl.dashclever.accountresources.account.infrastructure.rest.requests.RegisterReq
import pl.dashclever.accountresources.account.model.AccountHandler
import pl.dashclever.accountresources.account.model.CreateWorkshop
import pl.dashclever.accountresources.account.readmodel.AccessDto
import pl.dashclever.accountresources.account.readmodel.AccessesReader
import pl.dashclever.accountresources.account.readmodel.AccountDto
import pl.dashclever.accountresources.account.readmodel.AccountReader
import pl.dashclever.accountresources.account.readmodel.AuthorityDto
import pl.dashclever.accountresources.account.readmodel.WorkshopAccessesDto
import pl.dashclever.commons.security.Access
import pl.dashclever.commons.security.Access.WithAuthorities.Authority
import pl.dashclever.commons.security.Access.WithAuthorities.Authority.INSIGHT_REPAIR
import pl.dashclever.commons.security.Access.WithAuthorities.Authority.MANAGE_STAFF
import pl.dashclever.commons.security.Access.WithAuthorities.Authority.REPAIR_PROCESS
import pl.dashclever.commons.security.Access.WorkshopEmployeeAccess
import pl.dashclever.commons.security.Access.WorkshopOwnerAccess
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.spring.security.SpringSecurityApplicationFacade
import java.net.URI

private const val PATH = "/api/account"

@RestController
@RequestMapping(PATH)
internal class AccountRestApi(
    private val accountService: AccountService,
    private val accountHandler: AccountHandler,
    private val accessesReader: AccessesReader,
    private val accountReader: AccountReader,
    private val springSecurityApplicationFacade: SpringSecurityApplicationFacade,
    private val currentAccessProvider: CurrentAccessProvider
) {

    @PostMapping
    fun register(
        @Valid @RequestBody
        req: RegisterReq
    ): ResponseEntity<AccountDto> {
        accountService.registerAccount(
            username = req.username,
            password = req.password,
            email = req.email
        )
        return ResponseEntity.accepted().build()
    }

    @PostMapping("/workshop")
    fun createWorkshop(
        @Valid @RequestBody
        req: CreateWorkshopReq,
        authentication: Authentication
    ): ResponseEntity<AccessDto> {
        val accountId = (authentication.principal as? Access?)?.accountId
            ?: throw IllegalAccessException("Could not determine account id")
        val workshopId = accountHandler.createWorkshop(
            CreateWorkshop(
                accountId = accountId,
                displayName = req.displayName
            )
        )
        return ResponseEntity.created(URI.create("$PATH/workshop/$workshopId")).build()
    }

    @GetMapping("/access", produces = ["application/json"])
    fun getAccesses(authentication: Authentication?): Set<WorkshopAccessesDto> {
        if (authentication == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }
        val loggedUserDetails = authentication.principal as UserDetails?
            ?: throw IllegalArgumentException("Could not find authenticated user")
        val accDto = accountReader.findByUsername(loggedUserDetails.username)
            .orElseThrow { IllegalArgumentException("Could not find authenticated user") }
        return accessesReader.findAccountWorkshopAccesses(accDto.id)
    }

    @GetMapping(produces = ["application/json"])
    fun currentUser(authentication: Authentication?): AccessDto? {
        if (authentication == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        val currentAccess = currentAccessProvider.currentAccess()

        fun WorkshopOwnerAccess.toAccessDto(): AccessDto = accessesReader.findWorkshopOwnerAccesses(accountId)
            .firstOrNull { it.workshopId == workshopId }
            ?.let { ownerAccessDto ->
                AccessDto(
                    workshopId = ownerAccessDto.workshopId,
                    workshopName = ownerAccessDto.workshopName,
                    isOwnerAccess = true,
                    employeeId = null,
                    employeeFirstName = null,
                    authorities = this.authorities.map { it.toDto() }.toSet()
                )
            }
            ?: throw IllegalArgumentException("Could not find corresponding owner access for: $this")

        fun WorkshopEmployeeAccess.toAccessDto(): AccessDto = accessesReader.findEmployeeAccesses(this.accountId)
            .firstOrNull { it.workshopId == this.workshopId && it.employeeId == this.employeeId }
            ?.let { employeeAccessDto ->
                AccessDto(
                    workshopId = employeeAccessDto.workshopId,
                    workshopName = employeeAccessDto.workshopName,
                    isOwnerAccess = false,
                    employeeId = employeeAccessDto.employeeId,
                    employeeFirstName = employeeAccessDto.employeeFirstName,
                    authorities = this.authorities.map { it.toDto() }.toSet()
                )
            } ?: throw IllegalArgumentException("Could not find corresponding employee access for: $this")

        return when (currentAccess) {
            is WorkshopOwnerAccess -> currentAccess.toAccessDto()
            is WorkshopEmployeeAccess -> currentAccess.toAccessDto()
            else -> null
        }
    }

    private fun Authority.toDto(): AuthorityDto = when (this) {
        MANAGE_STAFF -> AuthorityDto.MANAGE_STAFF
        INSIGHT_REPAIR -> AuthorityDto.INSIGHT_REPAIR
        REPAIR_PROCESS -> AuthorityDto.REPAIR_PROCESS
    }

    @PostMapping("/access")
    fun choseSessionAccess(
        @RequestBody accessReq: AccessReq,
        currentAuthentication: Authentication
    ) {
        accessReq.employeeId?.let { springSecurityApplicationFacade.setEmployeeAccess(it) }
            ?: springSecurityApplicationFacade.setOwnerAccess(accessReq.workshopId)
    }
}
