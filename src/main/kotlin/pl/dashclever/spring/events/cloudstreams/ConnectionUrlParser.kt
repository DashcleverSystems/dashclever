package pl.dashclever.spring.events.cloudstreams

object ConnectionUrlParser {

    operator fun invoke(connectionUrl: String): RabbitMqConnectionProperties {
        val trimmedUrl = connectionUrl.trim()
        val withoutSuffix = if (trimmedUrl.startsWith("amqp://")) {
            trimmedUrl.replaceFirst("amqp://", "", ignoreCase = true)
        } else {
            trimmedUrl.replaceFirst("amqps://", "", ignoreCase = true)
        }
        val usernameDelimiterPosition = withoutSuffix.indexOfFirst { it == ':' }
        val username = withoutSuffix.substring(0, usernameDelimiterPosition)
        val passwordDelimiterPosition = withoutSuffix.indexOfFirst { it == '@' }
        val password = withoutSuffix.substring(usernameDelimiterPosition + 1, passwordDelimiterPosition)
        val virtualHostPosition = if (withoutSuffix.indexOfFirst { it == '/' } == -1) {
            null
        } else {
            withoutSuffix.indexOfLast { it == '/' } + 1
        }
        val hostDelimiterPosition = if (withoutSuffix.indexOfLast { it == ':' } != -1 && withoutSuffix.indexOfFirst { it == ':' } != withoutSuffix.indexOfLast { it == ':' }) {
            withoutSuffix.indexOfLast { it == ':' }
        } else {
            withoutSuffix.indexOfFirst { it == '/' }
        }
        val port = if (withoutSuffix.elementAt(hostDelimiterPosition) == ':') {
            withoutSuffix.substring(hostDelimiterPosition + 1, virtualHostPosition?.let { it - 1 } ?: withoutSuffix.length).toInt()
        } else {
            5672
        }

        val host = withoutSuffix.substring(passwordDelimiterPosition + 1, hostDelimiterPosition)
        val virtualHost = virtualHostPosition?.let { withoutSuffix.substring(it, withoutSuffix.length) }
        return RabbitMqConnectionProperties(host, virtualHost, username, password, port)
    }

    data class RabbitMqConnectionProperties(
        val host: String,
        val virtualHost: String?,
        val username: String,
        val password: String,
        val port: Int = 5672
    )
}
