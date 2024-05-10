package andrianopasquale97.EpiAutoBE.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VehicleUpdateDTO(
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
