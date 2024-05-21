package andrianopasquale97.EpiAutoBE;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class EpiAutoBeApplication {

	public static void main(String[] args) {
//		ConfigurableApplicationContext context = SpringApplication.run(EpiAutoBeApplication.class, args);
//		FillDatabaseVehicle fillDatabaseVehicle = context.getBean(FillDatabaseVehicle.class);
//		        fillDatabaseVehicle.fillDatabase();
		SpringApplication.run(EpiAutoBeApplication.class, args);

	}

}
