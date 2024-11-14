package tja.softavail.model.insurance;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Report {
    private final Long claims;

    public Report(Long claims) {
        this.claims = claims;
    }

    public Long getClaims() {
        return claims;
    }

}
