package tja.softavail.service;

import jakarta.inject.Inject;
import reactor.core.publisher.Mono;
import tja.softavail.model.insurance.InsuranceResponse;
import tja.softavail.model.insurance.Report;
import tja.softavail.model.response.Response;
import tja.softavail.model.response.ResponseConfig;
import tja.softavail.remote.InsuranceRemoteService;

import java.util.UUID;

public class InsuranceService {

    InsuranceRemoteService insuranceRemoteService;

    @Inject
    public InsuranceService(InsuranceRemoteService insuranceRemoteService) {
        this.insuranceRemoteService = insuranceRemoteService;
    }

    public Mono<InsuranceResponse> getInsuranceMono(String vin, ResponseConfig responseConfig, UUID uuid){
        if(responseConfig.isInsurance()) return insuranceRemoteService.getInsuranceReport(vin, uuid);
        else return Mono.just(new InsuranceResponse(new Report(null)));
    }

    public void processAccidents(Response response, InsuranceResponse insuranceResponse){
        if(insuranceResponse==null || insuranceResponse.getReport()==null || insuranceResponse.getReport().getClaims()==null) return;
        response.setAccidentFree(insuranceResponse.getReport().getClaims()==0L);
    }

}
