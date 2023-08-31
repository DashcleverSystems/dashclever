package pl.dashclever.accountresources.account.infrastructure.rest

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
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
import pl.dashclever.accountresources.account.readmodel.WorkshopAccessesDto
import pl.dashclever.spring.security.IdUserDetails
import pl.dashclever.spring.security.WorkshopUserDetailsService
import java.net.URI

private const val PATH = "/api/account"

@RestController
@RequestMapping(PATH)
internal class AccountController(
    private val accountService: AccountService,
    private val accountHandler: AccountHandler,
    private val accessesReader: AccessesReader,
    private val accountReader: AccountReader,
    private val workshopUserDetailsService: WorkshopUserDetailsService
) {

    @PostMapping
    fun register(@Valid @RequestBody req: RegisterReq): ResponseEntity<AccountDto> {
        accountService.registerAccount(
            username = req.username,
            password = req.password,
            email = req.email
        )
        return ResponseEntity.accepted().build()
    }

    @PostMapping("/workshop")
    fun createWorkshop(
        @Valid @RequestBody req: CreateWorkshopReq,
        authentication: Authentication
    ): ResponseEntity<AccessDto> {
        val accountId = (authentication.principal as? IdUserDetails?)?.id
            ?: throw IllegalAccessException("Could not determine account id")
        val workshopId = accountHandler.createWorkshop(
            CreateWorkshop(
                accountId = accountId,
                displayName = req.displayName
            )
        )
        return ResponseEntity.created(URI.create("$PATH/workshop/$workshopId")).build()
    }

    @GetMapping("/access")
    fun login(authentication: Authentication?): Set<WorkshopAccessesDto> {
        if (authentication == null)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        val loggedUserDetails = authentication.principal as UserDetails?
            ?: throw IllegalArgumentException("Could not find authenticated user")
        val accDto = accountReader.findByUsername(loggedUserDetails.username)
            .orElseThrow { IllegalArgumentException("Could not find authenticated user") }
        return accessesReader.findAccountWorkshopAccesses(accDto.id)
    }

    @PostMapping("/access")
    fun choseSessionAccess(
        @RequestBody accessReq: AccessReq,
        currentAuthentication: Authentication
    ) {
        val workshopUserDetails = accessReq.employeeId?.let {
            workshopUserDetailsService.employeeSpecificUserOfAuthentication(it, currentAuthentication)
        } ?: workshopUserDetailsService.workshopSpecificUserOfAuthentication(accessReq.workshopId, currentAuthentication)
        SecurityContextHolder.getContext().authentication = object : Authentication {
            private var isAuth = true

            override fun getName(): String = currentAuthentication.name

            override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
                workshopUserDetails.authorities

            override fun getCredentials(): Any =
                throw IllegalAccessException()

            override fun getDetails(): Any =
                currentAuthentication.details

            override fun getPrincipal(): Any =
                workshopUserDetails

            override fun isAuthenticated(): Boolean =
                this.isAuth

            override fun setAuthenticated(isAuthenticated: Boolean) {
                this.isAuth = isAuthenticated
            }
        }
    }
}
