package tja.softavail.model.insurance;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class InsuranceResponse {
    private final Report report;

    public InsuranceResponse(Report report) {
        this.report=report;
    }

    public Report getReport() {
        return report;
    }


}
