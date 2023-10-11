package pl.dashclever.readers.infrastructure;


import org.springframework.stereotype.Service;

import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.EstimateReaderFactory;
import pl.dashclever.readers.domain.ReaderException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Service
public class EstimateReading {

    public RepairInfo retrieveRepairInfo(EstimateReader estimateReader) throws ReaderException {
        EstimateReader.VehicleInfo vehicleInfo = estimateReader.FindVehicleInfo();
        EstimateReader.Paint paintInfo = estimateReader.FindPaintInfo();
        Set<EstimateReader.Job> labourJobs = estimateReader.findLabourJobs();
        Set<EstimateReader.Job> varnishingJobs = estimateReader.FindVarnishingJobs();
        String uniqueId = estimateReader.findUniqueNr();
        return new RepairInfo(uniqueId, vehicleInfo, paintInfo, labourJobs, varnishingJobs);
    }

    public RepairInfo retrieveRepairInfo(InputStream file) throws IOException, ReaderException {
        EstimateReader estimateReader = new EstimateReaderFactory(file).withPdfStreamExtractor();
        return this.retrieveRepairInfo(estimateReader);
    }

}
