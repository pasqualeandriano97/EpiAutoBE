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
    private int vehicleId;  //relazione con Vehicle
    private int userId;  //relazione con User

    public Appointment(LocalDate date, int time, int vehicleId, int userId) {
        this.date = date;
        this.time = time;
        this.vehicleId = vehicleId;
        this.userId = userId;
    }
}
