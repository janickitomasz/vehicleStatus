package tja.softavail.service;

import io.micronaut.http.annotation.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import tja.softavail.model.insurance.InsuranceResponse;
import tja.softavail.model.maintenance.MaintenanceResponse;
import tja.softavail.model.request.Request;
import tja.softavail.model.response.Response;
import tja.softavail.model.response.ResponseConfig;

import java.util.UUID;

@Singleton
@Produces
public class VehicleStatusService {

    @Inject
    InsuranceService insuranceService;

    @Inject
    MaintenanceService maintenanceService;

    private static final Logger LOG = LoggerFactory.getLogger(VehicleStatusService.class);

    public Mono<Response> getData(@Valid Request request, UUID uuid){
        LOG.info("Service.getData called with uuid {} and request {}", uuid, request);

        String vin = request.vin();
        ResponseConfig responseConfig = new ResponseConfig(request.features());

        Mono<InsuranceResponse> insuranceServiceCall = insuranceService.getInsuranceMono(vin, responseConfig, uuid);
        Mono<MaintenanceResponse> maintenanceServiceCall = maintenanceService.getMaintenanceMono(vin, responseConfig, uuid);

        Response response = new Response();
        response.setVin(vin);
        response.setRequestId(uuid);
        return Mono.zip(insuranceServiceCall, maintenanceServiceCall).map(
                t->{insuranceService.processAccidents(response, t.getT1());
                    maintenanceService.processMaintenance(response, t.getT2());
                    return response;}
        );
    }







}
