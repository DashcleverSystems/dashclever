package pl.dashclever.readers.domain.audatex;

import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.ReaderException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AudatexReader extends EstimateReader {

    public AudatexReader(List<String> pages){
        super(pages);
    }

    @Override
    public VehicleInfo FindVehicleInfo() {
        var lines = new OwnerVehicleSectionFinder(super.EstimatePages).findSectionLines();
        var brandLine = lines.stream()
                .filter(line -> line.toLowerCase().contains("marka"))
                .findFirst().orElse("");
        String brand = findBrandName(brandLine);
        var modelLine = lines.stream()
                .filter(line -> line.toLowerCase().contains("model"))
                .findFirst().orElse("");
        var model = findModel(modelLine);
        var platesLine = lines.stream()
                .filter(line -> line.toLowerCase().contains("rejestracyjny"))
                .findFirst().orElse("");
        var plates = findPlates(platesLine).replaceAll("\\s+", "");

        return new VehicleInfo(plates, brand, model);
    }

    private String findPaintBaseColorWithCodeInfo(String paintLine) {
        Pattern pattern = Pattern.compile(".*:\\s+(.*)");
        Matcher matcher = pattern.matcher(paintLine);
        if (matcher.matches())
            return matcher.group(1);
        else
            return "";
    }

    private String findPlates(String platesLine) {
        Pattern pattern = Pattern.compile(".? ?:\\s?([A-Za-z0-9 ]+)");
        Matcher matcher = pattern.matcher(platesLine);
        if (matcher.find())
            return matcher.group(1);
        else
            return "";
    }

    private String findModel(String modelLine) {
        Pattern pattern = Pattern.compile(".*:\\s+(?<model>.*)");
        Matcher matcher = pattern.matcher(modelLine);
        if (matcher.find())
            return matcher.group("model");
        else
            return "";
    }

    private String findBrandName(String brandLine) {
        Pattern pattern = Pattern.compile(".*:\\s+(?<brand>[A-Za-z]+)");
        Matcher matcher = pattern.matcher(brandLine);
        if (matcher.matches())
            return matcher.group("brand");
        else
            return "";
    }

    @Override
    public Set<Job> findLabourJobs() throws ReaderException {
        var finder = new LabourInfoFinder(this.EstimatePages);
        return new HashSet<>(finder.find());
    }

    @Override
    public String findUniqueNr() throws ReaderException {
        return this.EstimatePages.get(0).lines()
                .filter(line -> line.toLowerCase().contains("nr"))
                .findFirst()
                .flatMap(this::extractUniqueNumber)
                .orElseThrow(() -> new ReaderException("Colud not find uniqie id of esitmate. Unique ID example: 22/503kt"));
    }

    private Optional<String> extractUniqueNumber(String line) {
        Pattern pattern = Pattern.compile("nr\\s+(\\w+/\\w+)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find())
            return Optional.of(matcher.group(1));
        else
            return Optional.empty();
    }

    @Override
    public Set<Job> FindVarnishingJobs() {
        var finder = new VarnishingInfoFinder(this.EstimatePages);
        return new HashSet<>(finder.find());
    }

    @Override
    public Paint FindPaintInfo() {
        var firstPage = super.EstimatePages.get(0);
        var lines = firstPage.split("\n");
        var paintLine = Arrays.stream(lines)
                .filter(line -> line.toLowerCase().contains("lakier"))
                .findFirst().orElse("");
        var paint = findPaintBaseColorWithCodeInfo(paintLine);
        var varnishingPaintInfoFinder = new VarnishingInfoFinder(super.EstimatePages);
        var additionalPaintInfoLines = varnishingPaintInfoFinder.findVarnishingPaintInfo();
        return new Paint(paint, additionalPaintInfoLines);
    }

}
