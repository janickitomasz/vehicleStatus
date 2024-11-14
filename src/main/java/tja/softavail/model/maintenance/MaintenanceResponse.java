package tja.softavail.model.maintenance;

import io.micronaut.serde.annotation.Serdeable;
import tja.softavail.model.request.MaintenanceFrequency;

@Serdeable
public class MaintenanceResponse {
    private final MaintenanceFrequency maintenanceFrequency;

    public MaintenanceResponse(MaintenanceFrequency maintenanceFrequency) {
        this.maintenanceFrequency = maintenanceFrequency;
    }

    public MaintenanceFrequency getMaintenanceFrequency() {
        return maintenanceFrequency;
    }


}
