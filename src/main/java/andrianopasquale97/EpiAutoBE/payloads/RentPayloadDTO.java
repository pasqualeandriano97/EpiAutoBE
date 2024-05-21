package andrianopasquale97.EpiAutoBE.payloads;

public record RentPayloadDTO(
        String startDate,
        String endDate,
        String date,
        String time,
        String vehicle
) {
}
