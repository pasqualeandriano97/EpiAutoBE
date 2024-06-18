package andrianopasquale97.EpiAutoBE;


import andrianopasquale97.EpiAutoBE.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class EpiAutoBeApplication {
	@Autowired
	EmailSenderService senderService;

	public static void main(String[] args) {
//		ConfigurableApplicationContext context = SpringApplication.run(EpiAutoBeApplication.class, args);
//		FillDatabaseVehicle fillDatabaseVehicle = context.getBean(FillDatabaseVehicle.class);
//		        fillDatabaseVehicle.fillDatabase();
		SpringApplication.run(EpiAutoBeApplication.class, args);

	}


}
