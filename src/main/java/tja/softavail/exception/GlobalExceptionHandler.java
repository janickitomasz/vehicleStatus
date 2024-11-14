package tja.softavail.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tja.softavail.service.VehicleStatusService;

import java.util.UUID;

@Singleton
public class GlobalExceptionHandler implements ExceptionHandler<Exception, HttpResponse<?>>{

    private static final Logger LOG = LoggerFactory.getLogger(VehicleStatusService.class);

    @Override
    public HttpResponse<?> handle(HttpRequest request, Exception exception) {
        UUID uuid = UUID.randomUUID();
        LOG.error("error for requestID: {} ",uuid,exception.getMessage(), exception);
        return HttpResponse.serverError("Error during processing your request. requestID: " + uuid );

    }
}

