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
@Table(name = "Appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;
    private LocalDate date;
    private int time;
    @ManyToOne
    @JoinColumn(name = "vehicle_plate")
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Appointment(LocalDate date, int time, Vehicle vehicleId, User userId) {
        this.date = date;
        this.time = time;
        this.vehicle = vehicleId;
        this.user = userId;
    }
}
