package pl.dashclever.readers.infrastructure;

import pl.dashclever.readers.domain.EstimateReader;

import java.util.Set;
import java.util.UUID;

public record RepairInfo(
    String uniqueId,
    EstimateReader.CustomerInfo customerInfo,
    EstimateReader.VehicleInfo vehicleInfo,
    EstimateReader.Paint paint,
    Set<EstimateReader.Job> labourJobs,
    Set<EstimateReader.Job> varnishingJobs,
    UUID reportingId
) {
}
