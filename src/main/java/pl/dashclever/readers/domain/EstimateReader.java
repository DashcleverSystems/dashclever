package pl.dashclever.readers.domain;

import java.util.List;
import java.util.Set;

public abstract class EstimateReader {

    protected final List<String> EstimatePages;

    protected EstimateReader(List<String> estimatePages) {
        EstimatePages = estimatePages;
    }


    public abstract Set<Job> findLabourJobs() throws ReaderException;
    public abstract String findUniqueNr() throws ReaderException;
    public abstract Set<Job> FindVarnishingJobs();
    public abstract Paint FindPaintInfo();
    public abstract VehicleInfo FindVehicleInfo();

    public record Job(String name, int manMinutes, Money worth) {}
    public record Paint(String baseColorWithCode, List<String> varnishingPaintInfo) {}
    public record VehicleInfo(String registration, String brand, String model) {}
    public record Money(long denomination, Currency currency) {}
    public enum Currency {
        EUR, PLN
    }
}
