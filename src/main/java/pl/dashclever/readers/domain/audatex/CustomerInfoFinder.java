package pl.dashclever.readers.domain.audatex;

import java.util.List;

import pl.dashclever.readers.domain.EstimateReader;

class CustomerInfoFinder {

    private static final String CUSTOMER_INFO_SECTION_PREFIX = "właściciel pojazdu";

    private final List<String> pageLinesWithCustomerInfo;

    CustomerInfoFinder(List<String> pageLinesWithCustomerInfo) {
        this.pageLinesWithCustomerInfo = pageLinesWithCustomerInfo;
    }

    public EstimateReader.CustomerInfo findCustomerInfo() {
        final String lineWithCustomerName = pageLinesWithCustomerInfo.stream().filter(line -> line.toLowerCase().contains(CUSTOMER_INFO_SECTION_PREFIX)).findFirst().orElse("").trim();
        final int indexOfFirstSemicolon = lineWithCustomerName.indexOf(":");
        if (indexOfFirstSemicolon == -1) {
            return new EstimateReader.CustomerInfo("");
        }
        final String customerName = lineWithCustomerName.substring(indexOfFirstSemicolon + 1).trim();
        return new EstimateReader.CustomerInfo(customerName);
    }
}
