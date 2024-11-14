package tja.softavail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;
import tja.softavail.model.insurance.InsuranceResponse;
import tja.softavail.model.insurance.Report;
import tja.softavail.model.maintenance.MaintenanceResponse;
import tja.softavail.model.request.FeatureTypes;
import tja.softavail.model.request.MaintenanceFrequency;
import tja.softavail.model.request.Request;
import tja.softavail.model.response.MaintenanceScoreType;
import tja.softavail.model.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@MicronautTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class VehicleStatusServiceTest {

    private static final String PROPER_VIN = "12345678901234";
    private static final UUID PROPER_UUID = UUID.randomUUID();
    private static final List<FeatureTypes> featuresWithBothFeatures = new ArrayList();
    private static final List<FeatureTypes> featuresWithMaintenance = new ArrayList();
    private static final List<FeatureTypes> featuresWithInsurance = new ArrayList();

    @RegisterExtension
    static WireMockExtension insuranceMock = WireMockExtension.newInstance()
            .options(options().port(8081))
            .build();

    @RegisterExtension
    static WireMockExtension maintenanceMock = WireMockExtension.newInstance()
            .options(options().port(8082))
            .build();

    @Inject
    @InjectMocks
    VehicleStatusService vehicleStatusService;


    @BeforeEach
    void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        InsuranceResponse insuranceResponse = new InsuranceResponse(new Report(1L));
        MaintenanceResponse maintenanceResponse = new MaintenanceResponse(MaintenanceFrequency.VERY_LOW);

        String insuranceResponseJSON = mapper.writeValueAsString(insuranceResponse);
        String maintenanceResponseJSON = mapper.writeValueAsString(maintenanceResponse);

        insuranceMock.stubFor(get("/accidents/report?vin="+PROPER_VIN)
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(insuranceResponseJSON)
                        .withStatus(200)));

        maintenanceMock.stubFor(get("/cars/"+PROPER_VIN)
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(maintenanceResponseJSON)
                        .withStatus(200)));

        featuresWithBothFeatures.add(FeatureTypes.ACCIDENT_FREE);
        featuresWithBothFeatures.add(FeatureTypes.MAINTENANCE);

        featuresWithMaintenance.add(FeatureTypes.MAINTENANCE);

        featuresWithInsurance.add(FeatureTypes.ACCIDENT_FREE);

    }

    @Test
    void getData_BothEndpoints(){
        Request request = new Request(PROPER_VIN, featuresWithBothFeatures);
        Response response = vehicleStatusService.getData(request,PROPER_UUID).block();

        assert response != null;
        assertEquals(MaintenanceScoreType.POOR,response.getMaintenanceScoreType(),"Wrong maintenance score");
        assertFalse(response.isAccidentFree(),"Accident free should be false");
        assertEquals(PROPER_UUID, response.getRequestId(),"Wrong request id");
        assertEquals(PROPER_VIN, response.getVin(),"Wrong vin");
    }

    @Test
    void getData_Insurance(){
        Request request = new Request(PROPER_VIN, featuresWithInsurance);
        Response response = vehicleStatusService.getData(request,PROPER_UUID).block();

        assert response != null;
        assertNull(response.getMaintenanceScoreType(),"Maintenance score should me Null, when not selected by features in request");
        assertFalse(response.isAccidentFree(),"Accident free should be false");
        assertEquals(PROPER_UUID, response.getRequestId(),"Wrong request id");
        assertEquals(PROPER_VIN, response.getVin(),"Wrong vin");
    }

    @Test
    void getData_Maintenance(){
        Request request = new Request(PROPER_VIN, featuresWithMaintenance);
        Response response = vehicleStatusService.getData(request,PROPER_UUID).block();

        assert response != null;
        assertEquals(MaintenanceScoreType.POOR,response.getMaintenanceScoreType(),"Wrong maintenance score");
        assertNull(response.isAccidentFree(),"Accident free should be null, when not selected by features in request");
        assertEquals(PROPER_UUID, response.getRequestId(),"Wrong request id");
        assertEquals(PROPER_VIN, response.getVin(), "Wrong vin");
    }
}