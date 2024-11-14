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
import tja.softavail.model.insurance.InsuranceResponse;
import tja.softavail.model.insurance.Report;
import tja.softavail.model.request.FeatureTypes;
import tja.softavail.model.response.Response;
import tja.softavail.model.response.ResponseConfig;
import tja.softavail.remote.InsuranceRemoteService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@MicronautTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class InsuranceServiceTest {

    @Mock
    InsuranceRemoteService insuranceRemoteService;


    @Inject
    @InjectMocks
    InsuranceService insuranceService;

    private static final String PROPER_VIN = "12345678901234";
    private static final UUID PROPER_UUID = UUID.randomUUID();
    private static final List<FeatureTypes> featuresWithInsurance = new ArrayList();
    private static final List<FeatureTypes> featuresWithoutInsurance= new ArrayList();
    private static InsuranceResponse insuranceResponseWithAccidents;
    private static InsuranceResponse insuranceResponseWithoutAccidents;

    @BeforeAll
    static void prepareAll(){
        featuresWithoutInsurance.add(FeatureTypes.MAINTENANCE);
        featuresWithInsurance.add(FeatureTypes.ACCIDENT_FREE);
        insuranceResponseWithAccidents = new InsuranceResponse(new Report(1L));
        insuranceResponseWithoutAccidents = new InsuranceResponse(new Report(0L));
    }

    @BeforeEach
    void setUp(){
        when(insuranceRemoteService.getInsuranceReport(PROPER_VIN, PROPER_UUID)).thenReturn(Mono.just(new InsuranceResponse(new Report(1L))));
    }

    @Test
    void getInsuranceMon_Null() {
        ResponseConfig responseConfig = new ResponseConfig(featuresWithoutInsurance);
        Mono<InsuranceResponse> response =  insuranceService.getInsuranceMono(PROPER_VIN, responseConfig, PROPER_UUID);

        assertNull(response.block().getReport().getClaims(),"getInsurance should return mono with null value, when feature ACCIDEN_FREE is not present");
    }

    @Test
    void getInsuranceMon_NotNull() {
        ResponseConfig responseConfig = new ResponseConfig(featuresWithInsurance);
        Mono<InsuranceResponse> response =  insuranceService.getInsuranceMono(PROPER_VIN, responseConfig, PROPER_UUID);

        assertNotNull(response.block().getReport().getClaims(),"getInsurance should return mono from remote service Call, when feature ACCIDEN_FREE is present");
    }

    @Test
    void processAccidents_notZero() {
        Response response = new Response();
        insuranceService.processAccidents(response, insuranceResponseWithAccidents);

        assertFalse(response.isAccidentFree());
    }

    @Test
    void processAccidents_zero() {
        Response response = new Response();
        insuranceService.processAccidents(response, insuranceResponseWithoutAccidents);

        assertTrue(response.isAccidentFree());
    }
}