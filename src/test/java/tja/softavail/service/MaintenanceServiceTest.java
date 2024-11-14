package tja.softavail.service;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import tja.softavail.model.maintenance.MaintenanceResponse;
import tja.softavail.model.request.FeatureTypes;
import tja.softavail.model.request.MaintenanceFrequency;
import tja.softavail.model.response.MaintenanceScoreType;
import tja.softavail.model.response.Response;
import tja.softavail.model.response.ResponseConfig;
import tja.softavail.remote.MaintenanceRemoteService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@MicronautTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MaintenanceServiceTest {

    @Mock
    MaintenanceRemoteService maintenanceRemoteService;

    @Inject
    @InjectMocks
    MaintenanceService maintenanceService;

    private static final String PROPER_VIN = "12345678901234";
    private static final UUID PROPER_UUID = UUID.randomUUID();
    private static final List<FeatureTypes> featuresWithMaintenance = new ArrayList();
    private static final List<FeatureTypes> featuresWithoutMaintenance= new ArrayList();

    @BeforeAll
    static void prepareAll() {
        featuresWithMaintenance.add(FeatureTypes.MAINTENANCE);
        featuresWithoutMaintenance.add(FeatureTypes.ACCIDENT_FREE);
    }

    @BeforeEach
    void setUp(){
        when(maintenanceRemoteService.getMaintenanceData(PROPER_VIN, PROPER_UUID)).thenReturn(Mono.just(new MaintenanceResponse(MaintenanceFrequency.VERY_LOW)));
    }

    @Test
    void getMaintenanceMono_null() {
        ResponseConfig responseConfig = new ResponseConfig(featuresWithoutMaintenance);
        Mono<MaintenanceResponse> response = maintenanceService.getMaintenanceMono(PROPER_VIN,responseConfig, PROPER_UUID);

        assertNull(response.block().getMaintenanceFrequency(), "Maintenance frequency should be null, when not selected to call");
    }

    @Test
    void getMaintenanceMono_notNull() {
        ResponseConfig responseConfig = new ResponseConfig(featuresWithMaintenance);
        Mono<MaintenanceResponse> response = maintenanceService.getMaintenanceMono(PROPER_VIN,responseConfig, PROPER_UUID);

        assertNotNull(response.block().getMaintenanceFrequency(), "Maintenance frequency should not be null, when selected to call");
    }

    @Test
    void processMaintenance_poor() {
        Response responseVeryLow = new Response();
        MaintenanceResponse maintenanceResponseVeryLow = new MaintenanceResponse(MaintenanceFrequency.VERY_LOW);
        Response responseLow = new Response();
        MaintenanceResponse maintenanceResponseLow = new MaintenanceResponse(MaintenanceFrequency.LOW);

        maintenanceService.processMaintenance(responseVeryLow,maintenanceResponseVeryLow);
        maintenanceService.processMaintenance(responseLow,maintenanceResponseLow);

        assertEquals(responseLow.getMaintenanceScoreType(), MaintenanceScoreType.POOR, "Maintenance score should be POOR for very low frequence");
        assertEquals(responseVeryLow.getMaintenanceScoreType(), MaintenanceScoreType.POOR, "Maintenance score should be POOR for low frequence");
    }

    @Test
    void processMaintenance_average() {
        Response responseMedium = new Response();
        MaintenanceResponse maintenanceResponseMedium = new MaintenanceResponse(MaintenanceFrequency.MEDIUM);

        maintenanceService.processMaintenance(responseMedium,maintenanceResponseMedium);

        assertEquals(responseMedium.getMaintenanceScoreType(), MaintenanceScoreType.AVERAGE, "Maintenance score should be Average for medium frequence");
    }

    @Test
    void processMaintenance_good() {
        Response responseHigh = new Response();
        MaintenanceResponse maintenanceResponseHigh = new MaintenanceResponse(MaintenanceFrequency.HIGH);

        maintenanceService.processMaintenance(responseHigh,maintenanceResponseHigh);

        assertEquals(responseHigh.getMaintenanceScoreType(), MaintenanceScoreType.GOOD, "Maintenance score should be Good for high frequence");
    }
}