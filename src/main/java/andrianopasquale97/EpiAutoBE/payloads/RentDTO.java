package andrianopasquale97.EpiAutoBE.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RentDTO(
        @NotEmpty(message = "Inserire una data")
        @Pattern(regexp = "^(?:31([/\\-.])(?:0?[13578]|1[02])\\1|(?:29|30)([/\\-.])(?:0?[13-9]|1[0-2])\\2)(?:1[6-9]|[2-9]\\d)?\\d{2}$|^29([/\\-.])0?2\\3(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)$|^(?:0?[1-9]|1\\d|2[0-8])([/\\-.])(?:0?[1-9]|1[0-2])\\4(?:1[6-9]|[2-9]\\d)?\\d{2}$",message = "Inserisci una data valida")
        String startDate,
        @NotEmpty(message = "Inserire una data")
        @Pattern(regexp = "^(?:31([/\\-.])(?:0?[13578]|1[02])\\1|(?:29|30)([/\\-.])(?:0?[13-9]|1[0-2])\\2)(?:1[6-9]|[2-9]\\d)?\\d{2}$|^29([/\\-.])0?2\\3(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)$|^(?:0?[1-9]|1\\d|2[0-8])([/\\-.])(?:0?[1-9]|1[0-2])\\4(?:1[6-9]|[2-9]\\d)?\\d{2}$",message = "Inserisci una data valida")
        String endDate,
        @NotEmpty(message = "Inserire una data")
        @Pattern(regexp = "^(?:31([/\\-.])(?:0?[13578]|1[02])\\1|(?:29|30)([/\\-.])(?:0?[13-9]|1[0-2])\\2)(?:1[6-9]|[2-9]\\d)?\\d{2}$|^29([/\\-.])0?2\\3(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)$|^(?:0?[1-9]|1\\d|2[0-8])([/\\-.])(?:0?[1-9]|1[0-2])\\4(?:1[6-9]|[2-9]\\d)?\\d{2}$",message = "Inserisci una data valida")
        String date,
        @NotEmpty(message = "Inserire l'ora di inizio")
        @Pattern(regexp = "/^(1[89]|[89])$/", message = "Inserisci un'ora valida")
        String startHour

        ) {
}
