package pl.dashclever.tests

fun <T : Any> T.withField(variableName: String, data: Any): T {
    javaClass.getDeclaredField(variableName).let { field ->
        field.isAccessible = true
        field.set(this, data)
    }
    return this
}
