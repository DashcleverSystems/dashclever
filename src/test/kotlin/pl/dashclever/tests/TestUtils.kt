package pl.dashclever.tests

fun <T : Any> T.withField(fieldName: String, value: Any): T {
    javaClass.getDeclaredField(fieldName).let { field ->
        field.isAccessible = true
        field.set(this, value)
    }
    return this
}
