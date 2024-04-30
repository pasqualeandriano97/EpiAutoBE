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
@Table(name = "Rents")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate date;
    private int time;
    private int vehicleId;   //relazione con Vehicle
    private int userId;    //relazione con User

    public Rent(LocalDate startDate, LocalDate endDate, int time, LocalDate date, int vehicleId, int userId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.date = date;
        this.vehicleId = vehicleId;
        this.userId = userId;
    }
}
