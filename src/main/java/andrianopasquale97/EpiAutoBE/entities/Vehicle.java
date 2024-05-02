package andrianopasquale97.EpiAutoBE.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Vehicles")
public class Vehicle {
    @Id
    @Setter(AccessLevel.NONE)
    private String plate;
    private String fuelType;
    private String brand;
    private String model;
    private String type;
    private int year;
    private String imageUrl;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Rent> rents;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    public Vehicle(String plate, String fuelType, String brand, String model, String type, int year) {
        this.plate = plate;
        this.fuelType = fuelType;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.year = year;
    }
}
