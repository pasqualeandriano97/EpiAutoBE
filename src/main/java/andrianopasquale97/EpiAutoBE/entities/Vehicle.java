package andrianopasquale97.EpiAutoBE.entities;

import andrianopasquale97.EpiAutoBE.entities.enums.State;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"rents", "appointments", "maintenances"})
public class Vehicle {
    @Id
    @Setter(AccessLevel.NONE)
    private String plate;
    private String fuelType;
    private String brand;
    private String model;
    private String type;
    private int year;
    @Enumerated(EnumType.STRING)
    private State state;
    private String imageUrl;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Rent> rents;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Appointment> appointments;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Maintenance> maintenances;

    public Vehicle(String plate, String fuelType, String brand, String model, String type, int year,String image) {
        this.plate = plate;
        this.fuelType = fuelType;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.year = year;
        this.imageUrl = image;
    }

    public void setState(String stateS) {
        switch (stateS) {
            case "DISPONIBILE":
                this.state = State.AVAILABLE;
                break;
            case "NOLEGGIATA":
                this.state = State.RENTED;
                break;
            case "VENDUTA":
                this.state = State.SOLD;
                break;
            case "IN MANUTENZIONE":
                this.state = State.MAINTENANCE;
                break;
                default:
                    throw new IllegalArgumentException("Stato non valido");
        }
    }


}
