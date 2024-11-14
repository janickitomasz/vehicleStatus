package tja.softavail.service;

import jakarta.inject.Inject;
import reactor.core.publisher.Mono;
import tja.softavail.model.maintenance.MaintenanceResponse;
import tja.softavail.model.request.MaintenanceFrequency;
import tja.softavail.model.response.MaintenanceScoreType;
import tja.softavail.model.response.Response;
import tja.softavail.model.response.ResponseConfig;
import tja.softavail.remote.MaintenanceRemoteService;

import java.util.UUID;

public class MaintenanceService {

    MaintenanceRemoteService maintenanceRemoteService;

    @Inject
    public MaintenanceService(MaintenanceRemoteService maintenanceRemoteService) {
        this.maintenanceRemoteService = maintenanceRemoteService;
    }

    public Mono<MaintenanceResponse> getMaintenanceMono(String vin, ResponseConfig responseConfig, UUID uuid){
        if(responseConfig.isMaintenance())  return maintenanceRemoteService.getMaintenanceData(vin, uuid);
        return Mono.just(new MaintenanceResponse(null));
    }

    public void processMaintenance(Response response, MaintenanceResponse maintenanceResponse){
        if(maintenanceResponse==null || maintenanceResponse.getMaintenanceFrequency()==null) return;
        response.setMaintenanceScoreType(selectScore(maintenanceResponse.getMaintenanceFrequency()));
    }

    private MaintenanceScoreType selectScore(MaintenanceFrequency maintenanceFrequency){
        if(maintenanceFrequency==MaintenanceFrequency.VERY_LOW || maintenanceFrequency==MaintenanceFrequency.LOW){
            return MaintenanceScoreType.POOR;
        }

        if(maintenanceFrequency==MaintenanceFrequency.MEDIUM){
            return MaintenanceScoreType.AVERAGE;
        }
        return MaintenanceScoreType.GOOD;
    }
}
