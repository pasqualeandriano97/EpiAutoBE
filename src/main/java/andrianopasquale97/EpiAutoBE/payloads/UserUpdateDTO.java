package andrianopasquale97.EpiAutoBE.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @NotEmpty(message = "Inserisci il nome")
        @Size(message = "Il nome deve essere di almeno 3 caratteri e massimo 15", min = 3,max = 15)
        String name,
        @NotEmpty(message = "Inserisci il cognome")
        @Size(message = "Il cognome deve essere di almeno 3 caratteri e massimo 15", min = 3,max = 15)
        String surname,
        @NotEmpty(message = "Inserisci l'email")
        @Email(message = "Email non valida")
        String email
) {
}
