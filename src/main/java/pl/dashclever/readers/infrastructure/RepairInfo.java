package pl.dashclever.readers.infrastructure;

import pl.dashclever.readers.domain.EstimateReader;

import java.util.Set;

public record RepairInfo(
    String uniqueId,
    EstimateReader.CustomerInfo customerInfo,
    EstimateReader.VehicleInfo vehicleInfo,
    EstimateReader.Paint paint,
    Set<EstimateReader.Job> labourJobs,
    Set<EstimateReader.Job> varnishingJobs,
    String reportingId
) {
}
