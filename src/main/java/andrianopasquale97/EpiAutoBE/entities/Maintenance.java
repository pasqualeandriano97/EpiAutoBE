package andrianopasquale97.EpiAutoBE.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="maintenances")
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "vehicle_plate")
    private Vehicle vehicle;

    public Maintenance(LocalDate startDate, LocalDate endDate, Vehicle vehicleId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.vehicle = vehicleId;
    }
}
