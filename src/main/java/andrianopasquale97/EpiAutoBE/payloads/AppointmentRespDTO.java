package andrianopasquale97.EpiAutoBE.payloads;

public record AppointmentRespDTO(
        String id,
        String date,
        String hour,
        String vehiclePlate
) {
}
