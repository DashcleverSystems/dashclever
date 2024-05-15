package pl.dashclever.readers.reporting;

import java.util.UUID;

public record ReportDto(
    UUID reportingId,
    String description
) {
    public Report toEntity() {
        return new Report(reportingId, description);
    }
}
