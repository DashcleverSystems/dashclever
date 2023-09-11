package pl.dashclever.repairmanagment.plannig.readmodel.impl

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDate
import java.util.UUID

internal class JobEntityRowMapper : RowMapper<JobEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): JobEntity {
        return JobEntity(
            id = rs.getLong(JobEntity.ID),
            catalogueJobId = rs.getLong(JobEntity.CATALOGUE_JOB_ID),
            manMinutes = rs.getInt(JobEntity.MAN_MINUTES),
            planId = UUID.fromString(rs.getString(JobEntity.PLAN_ID)),
            hour = rs.getString(JobEntity.HOUR),
            assignedTo = rs.getString(JobEntity.ASSIGNED_TO),
            assignedAt = rs.getString(JobEntity.ASSIGNED_AT)
                ?.let { LocalDate.parse(it) }
        )
    }
}
