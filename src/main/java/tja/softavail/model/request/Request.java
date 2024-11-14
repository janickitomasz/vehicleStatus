package tja.softavail.model.request;

import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

@Serdeable
public record  Request (

    @Size(min=17, max=17, message="Vin number should be 17 characters long")
    @Pattern(regexp = "^[A-Z0-9&&[^IOQ]]+$", message = "Not allowed chracters in vin")
    String vin,


    @Size(min=1, message = "At least one element expected" )
    List<FeatureTypes> features
){}

