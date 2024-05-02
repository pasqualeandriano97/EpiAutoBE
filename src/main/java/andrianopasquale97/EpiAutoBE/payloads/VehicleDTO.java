package andrianopasquale97.EpiAutoBE.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VehicleDTO(
        @NotEmpty(message = "Inserire una targa")
         @Pattern(regexp = "/[a-zA-Z]{2}[0-9]{3}[a-zA-Z]{2}/", message = "Targa non valida")
        String plate,
        @NotEmpty(message = "Inserire il tipo di carburante")
        String fuelType,
        @NotEmpty(message = "Inserire la marca")
        String brand,
        @NotEmpty(message = "Inserire il modello")
        String model,
        @NotEmpty(message = "Inserire il tipo di veicolo")
        String type,
        @NotNull(message = "Inserire l'anno")
        int year,
        String imageUrl
) {
}
