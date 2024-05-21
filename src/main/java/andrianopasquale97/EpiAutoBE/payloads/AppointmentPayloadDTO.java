package andrianopasquale97.EpiAutoBE.payloads;

public record AppointmentPayloadDTO(
        String date,
        String hour,
        String vehicle
) {
}
