package andrianopasquale97.EpiAutoBE.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record AppointmentDTO(
      @NotEmpty(message = "Inserire una data")
      @Pattern(regexp = "/^(0?[1-9]|[12][0-9]|3[01])[-:/](0?[1-9]|1[0-2])[-:/](\\d{4})$/")
      String date,
      @NotEmpty(message = "Inserire l'ora")
      @Pattern(regexp = "/^(0[8-9]|1[0-8]):[0-5]\\d$/")
      String hour
) {
}
