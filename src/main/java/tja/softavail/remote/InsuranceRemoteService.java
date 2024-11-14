package tja.softavail.remote;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.retry.annotation.Retryable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import tja.softavail.exception.ServerSideException;
import tja.softavail.VehicleStatusController;
import tja.softavail.model.insurance.InsuranceResponse;

import java.util.UUID;


@Client("${services.insurance.url}")
public interface InsuranceRemoteService {

    Logger LOG = LoggerFactory.getLogger(VehicleStatusController.class);


    @Get("/report{?vin}")
    @SingleResult
    Mono<InsuranceResponse> getInsuranceReport(String vin);

    @Retryable(capturedException = ServerSideException.class)
    @Get("/report{?vin}")
    default Mono<InsuranceResponse> getInsuranceReport(String vin, UUID uuid) {
        LOG.info("InsuranceService called with requestId {} and VIN {}", uuid, vin);
        try {
            return getInsuranceReport(vin)
                    .doOnSubscribe(request -> LOG.info("insurance report endpoint called for requestId {} with reqquest {} ", uuid, request))
                    .doOnSuccess(response -> LOG.info("insurance report endpoint called with requestId {} with response {}", uuid, response))
                    .doOnError(error -> LOG.info("Error while calling insurance report endpoint with uuid {}, error: ", uuid, error));
        }catch(HttpClientResponseException e) {
            if(e.getStatus().getCode()>=500 && e.getStatus().getCode()<600) {
                throw new ServerSideException();
            }
        }catch(RuntimeException e){
            throw new ServerSideException();
        }
        return null;
    }

}
