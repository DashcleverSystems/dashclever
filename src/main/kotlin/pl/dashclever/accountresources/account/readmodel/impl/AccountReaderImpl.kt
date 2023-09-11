package pl.dashclever.accountresources.account.readmodel.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import pl.dashclever.accountresources.account.readmodel.AccountDto
import pl.dashclever.accountresources.account.readmodel.AccountReader
import java.util.Optional
import java.util.UUID

@Component
class AccountReaderImpl(
    private val jdbcTemplate: JdbcTemplate
) : AccountReader {

    override fun findByUsername(username: String): Optional<AccountDto> {
        val query = """
            SELECT
                    acc.id,
                    acc.username,
                    acc.password_hash,
                    acc.email
            FROM
                    ACCOUNT acc
            WHERE
                    acc.username = ?
        """.trimIndent()
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                query,
                { rs, _ ->
                    AccountDto(
                        id = UUID.fromString(rs.getString("id")),
                        username = rs.getString("username"),
                        email = rs.getString("email"),
                        passwordHash = rs.getString("password_hash")
                    )
                },
                username
            )
        )
    }
}
