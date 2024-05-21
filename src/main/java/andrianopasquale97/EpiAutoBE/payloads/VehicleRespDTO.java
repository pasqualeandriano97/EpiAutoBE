package andrianopasquale97.EpiAutoBE.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VehicleRespDTO(
        @NotEmpty(message = "Inserire una targa")
        @Pattern(regexp = "[a-zA-Z]{2}[0-9]{3}[a-zA-Z]{2}", message = "Targa non valida")
        String plate,
        @NotEmpty(message = "Inserire la marca")
        String brand,
        @NotEmpty(message = "Inserire il modello")
        String model,
        @NotEmpty(message = "Inserire il tipo di veicolo")
        String type,
        @NotEmpty(message = "Inserire il tipo di carburante")
        String fuelType,
        @NotNull(message = "Inserire l'anno")
        int year,
        String state,
        String imageUrl
) {
}
