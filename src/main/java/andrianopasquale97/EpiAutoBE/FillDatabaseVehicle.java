package andrianopasquale97.EpiAutoBE;

import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import andrianopasquale97.EpiAutoBE.repositories.VehicleDAO;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Supplier;

@Component
public class FillDatabaseVehicle {
    @Autowired
    private VehicleDAO vehicleDAO;


    public void fillDatabase() {
        Faker faker = new Faker();
        Supplier<String> plate = () -> faker.bothify("??###??").toUpperCase();
        String filePath = new File("src/main/resources/veicoli.csv").getAbsolutePath();
        boolean isFirstLine = true;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] columns = line.split(",");
                String fluelType = columns[0];
                String brand = columns[1];
                String model = columns[2];
                String type = columns[3];
                int year = Integer.parseInt(columns[4]);

                if(fluelType.isEmpty()){
                    fluelType = "GASOLINE";
                }
//
                vehicleDAO.save(new Vehicle(plate.get(), fluelType, brand, model, type, year));
                // Salva la provincia nel database

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
