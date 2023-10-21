package pl.dashclever.spring.security

import pl.dashclever.commons.security.Access

internal interface WithAccess {

    val access: Access
}
