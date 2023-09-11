package pl.dashclever.repairmanagment.plannig.readmodel.impl

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

internal class PlanEntityRowMapper : RowMapper<PlanEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PlanEntity {
        return PlanEntity(
            id = UUID.fromString(rs.getString("id")),
            estimateId = rs.getString("estimate_id")
        )
    }
}
