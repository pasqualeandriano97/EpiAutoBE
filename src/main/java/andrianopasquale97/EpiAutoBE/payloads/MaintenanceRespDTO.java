package andrianopasquale97.EpiAutoBE.payloads;

public record MaintenanceRespDTO(
        String id,
        String startDate,
        String endDate,
        String vehiclePlate
) {
}
