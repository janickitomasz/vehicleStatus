package tja.softavail.model.response;

import tja.softavail.model.request.FeatureTypes;

import java.util.List;


public class ResponseConfig {

    private final boolean insurance;
    private final boolean maintenance;

    public ResponseConfig(List<FeatureTypes> features){
        this.insurance=features.contains(FeatureTypes.ACCIDENT_FREE);
        this.maintenance=features.contains(FeatureTypes.MAINTENANCE);
    }

    public boolean isInsurance() {
        return insurance;
    }

    public boolean isMaintenance() {
        return maintenance;
    }
}
