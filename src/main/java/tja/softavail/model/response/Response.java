package tja.softavail.model.response;

import io.micronaut.serde.annotation.Serdeable;

import java.util.UUID;

@Serdeable
public class Response {
    private UUID requestId;
    private String vin;
    Boolean accidentFree;
    MaintenanceScoreType maintenanceScoreType;

    public Response() {
    }

    public Response(UUID requestId, String vin, boolean accidentFree, MaintenanceScoreType maintenanceScoreType) {
        this.requestId = requestId;
        this.vin = vin;
        this.accidentFree = accidentFree;
        this.maintenanceScoreType = maintenanceScoreType;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setAccidentFree(Boolean accidentFree) {
        this.accidentFree = accidentFree;
    }

    public void setMaintenanceScoreType(MaintenanceScoreType maintenanceScoreType) {
        this.maintenanceScoreType = maintenanceScoreType;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public String getVin() {
        return vin;
    }

    public Boolean isAccidentFree() {
        return accidentFree;
    }

    public MaintenanceScoreType getMaintenanceScoreType() {
        return maintenanceScoreType;
    }
}
