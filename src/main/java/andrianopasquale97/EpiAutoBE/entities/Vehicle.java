package andrianopasquale97.EpiAutoBE.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public Vehicle(String plate, String fuelType, String brand, String model, String type, int year) {
        this.plate = plate;
        this.fuelType = fuelType;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.year = year;
    }
}
