package tja.softavail.model.request;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public enum FeatureTypes {
    ACCIDENT_FREE("accident_free"),
    MAINTENANCE("maintenance");

    final String name;

    FeatureTypes(String name) {
        this.name = name;
    }
}
