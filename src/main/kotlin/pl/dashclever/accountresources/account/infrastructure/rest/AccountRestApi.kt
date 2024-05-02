package pl.dashclever.accountresources.account.infrastructure.rest

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
import pl.dashclever.accountresources.account.model.AccountCommand.CreateWorkshop
import pl.dashclever.accountresources.account.model.AccountHandler
import pl.dashclever.accountresources.account.readmodel.AccessDto
import pl.dashclever.accountresources.account.readmodel.AccessesReader
import pl.dashclever.accountresources.account.readmodel.AccountDto
import pl.dashclever.accountresources.account.readmodel.AccountReader
import pl.dashclever.accountresources.account.readmodel.AuthorityDto
import pl.dashclever.accountresources.account.readmodel.WorkshopAccessesDto
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.WithAccountId
import pl.dashclever.commons.security.WithAuthorities
import pl.dashclever.commons.security.WithAuthorities.Authority
import pl.dashclever.commons.security.WithAuthorities.Authority.INSIGHT_REPAIR
import pl.dashclever.commons.security.WithAuthorities.Authority.MANAGE_STAFF
import pl.dashclever.commons.security.WithAuthorities.Authority.REPAIR_PROCESS
import pl.dashclever.commons.security.WithWorkshopId
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
        val accountId = (authentication.principal as? WithAccountId)?.accountId
            ?: throw IllegalAccessException("Could not determine account id")
        val workshopId = accountHandler.createWorkshop(
            CreateWorkshop(
                accountId = accountId,
                displayName = req.displayName
            )
        )
        return ResponseEntity.created(URI.create("$PATH/workshop/$workshopId")).build()
    }

    @GetMapping("/access", produces = [MediaType.APPLICATION_JSON_VALUE])
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

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun currentUser(authentication: Authentication?): AccessDto? {
        if (authentication == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        val currentAccount = currentAccessProvider.currentAccountId()
        val workshopId = (currentAccount as? WithWorkshopId)?.workshopId ?: return null
        val workshopName = accessesReader.findWorkshopName(workshopId)
        val isOwnerAccess = accessesReader.findWorkshopOwnerAccesses(currentAccount.accountId).any { it.workshopId == workshopId }
        return AccessDto(
            workshopId,
            workshopName,
            isOwnerAccess,
            null, // null until we introduce possibility to register and login as employee
            null, // null until we introduce possibility to register and login as employee
            (currentAccount as? WithAuthorities)?.authorities?.map { it.toDto() }?.toSet() ?: emptySet()
        )
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
