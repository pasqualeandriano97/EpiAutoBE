package andrianopasquale97.EpiAutoBE.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
    private Time time;
    private double price;
    @ManyToOne
    @JoinColumn(name = "vehicle_plate")
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Rent(LocalDate startDate, LocalDate endDate,  LocalDate date,Time time, Vehicle vehicleId, User userId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.date = date;
        this.vehicle = vehicleId;
        this.user = userId;
        this.price = calculatePrice();
    }

    private double calculatePrice() {
        double startPrice= 50;
        if (vehicle.getType().equals("super car")) {
            startPrice = 300;
        }
        long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        return startPrice * daysDifference;
    }
}
