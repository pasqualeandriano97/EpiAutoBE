package andrianopasquale97.EpiAutoBE.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserLoginDTO(
        @NotEmpty(message = "Inserisci l'email")
        @Email(message = "Email non valida")
        String email,
        @NotEmpty(message = "Inserisci la password")
        String password
) {
}
