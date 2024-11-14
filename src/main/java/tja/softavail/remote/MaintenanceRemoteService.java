package tja.softavail.remote;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.retry.annotation.Retryable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import tja.softavail.exception.ServerSideException;
import tja.softavail.VehicleStatusController;
import tja.softavail.model.maintenance.MaintenanceResponse;

import java.util.UUID;

@Client("${services.maintenance.url}")
public interface MaintenanceRemoteService {

    Logger LOG = LoggerFactory.getLogger(VehicleStatusController.class);

    @Get("/cars/{vin}")
    Mono<MaintenanceResponse> getMaintenanceData(@PathVariable String vin);

    @Retryable(capturedException = ServerSideException.class)
    @Get("/cars{vin}")
    default Mono<MaintenanceResponse> getMaintenanceData(@PathVariable String vin, UUID uuid) {
        LOG.info("MaintenanceService called with requestId {} and VIN {}", uuid, vin);
        try {
            return getMaintenanceData(vin)
                    .doOnSubscribe(request -> LOG.info("maintenance report endpoint called for requestId {} with reqquest {} ", uuid, request))
                    .doOnSuccess(response -> LOG.info("maintenance report endpoint called with requestId {} with response {}", uuid, response))
                    .doOnError(error -> LOG.info("Error while calling maintenance report endpoint with uuid {}, error: ", uuid, error));
        }catch(HttpClientResponseException e) {
            if (e.getStatus().getCode() >= 500 && e.getStatus().getCode() < 600) {
                throw new ServerSideException();
            }
        }catch(RuntimeException e){
            throw new ServerSideException();
        }
        return null;
    }
}
