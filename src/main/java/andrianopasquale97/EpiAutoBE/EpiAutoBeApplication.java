package andrianopasquale97.EpiAutoBE;

import andrianopasquale97.EpiAutoBE.repositories.VehicleDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SpringBootApplication
public class EpiAutoBeApplication {

	public static void main(String[] args) {
//		ConfigurableApplicationContext context = SpringApplication.run(EpiAutoBeApplication.class, args);
//		FillDatabaseVehicle fillDatabaseVehicle = context.getBean(FillDatabaseVehicle.class);
//		        fillDatabaseVehicle.fillDatabase();
		SpringApplication.run(EpiAutoBeApplication.class, args);

	}

}
