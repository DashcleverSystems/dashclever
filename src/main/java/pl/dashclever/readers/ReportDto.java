package pl.dashclever.readers;

public record ReportDto(
    String pdfName,
    String content
) {
    public Report toEntity() {
        return new Report(pdfName, content);
    }
}
