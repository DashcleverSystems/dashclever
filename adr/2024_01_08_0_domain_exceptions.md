## Domain exception
Domain exception is thrown when one of the domain rules (Domain Driven Design) is violated.

All domain rules are protected on backend side with code. If domain requirements are not met
`DomainException` is thrown.

This domain rules, although protected by backend code, have to bo also protected on the front end level.
Actions which may result in `DomainException` should not be possible to execute.

It might happen that `DomainException` will be propagated and returned by API.
In this case backend will always return response with the same `error.message` i18n translation key
(For now it is `error.domain.validation.failure`). In this case we will inform user that actions
he took are not allowed. We will ask to report this problem to us.
