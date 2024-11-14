package tja.softavail;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import tja.softavail.model.request.Request;
import tja.softavail.model.response.Response;

import jakarta.validation.Valid;
import tja.softavail.service.VehicleStatusService;

import java.util.UUID;

@Controller("/check")
public class VehicleStatusController {

    @Inject
    VehicleStatusService vehicleStatusService;

    private static final Logger LOG = LoggerFactory.getLogger(VehicleStatusController.class);

    @Post("/")
    public Mono<Response> getCarDetailsbyVin(@Valid @Body Request request){
        UUID uuid = UUID.randomUUID();
        LOG.info("Controller.getCarDetailsbyVin called with uuid {} and request {}", uuid, request);

        return vehicleStatusService.getData(request, uuid);
    }
}
