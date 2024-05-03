package andrianopasquale97.EpiAutoBE.payloads;

public record RentRespDTO(
        int id,
        String startDate,
        String endDate,
        String date,
        String startHour,
        String brand,
        String model,
        int year
) {
}
