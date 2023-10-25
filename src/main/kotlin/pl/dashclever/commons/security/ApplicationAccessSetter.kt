package pl.dashclever.commons.security

import pl.dashclever.commons.security.Access.WorkshopEmployeeAccess
import pl.dashclever.commons.security.Access.WorkshopOwnerAccess

interface ApplicationAccessSetter {

    fun set(access: WorkshopOwnerAccess): WorkshopOwnerAccess

    fun set(access: WorkshopEmployeeAccess): WorkshopEmployeeAccess
}
