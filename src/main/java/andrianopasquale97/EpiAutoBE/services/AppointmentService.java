package andrianopasquale97.EpiAutoBE.services;

import andrianopasquale97.EpiAutoBE.entities.Appointment;
import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.exceptions.NotFoundException;
import andrianopasquale97.EpiAutoBE.payloads.AppointmentDTO;
import andrianopasquale97.EpiAutoBE.payloads.AppointmentRespDTO;
import andrianopasquale97.EpiAutoBE.repositories.AppointmentDAO;
import andrianopasquale97.EpiAutoBE.repositories.UserDAO;
import andrianopasquale97.EpiAutoBE.repositories.VehicleDAO;
import com.github.javafaker.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.Integer.parseInt;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentDAO appointmentDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private VehicleDAO vehicleDAO;

    public AppointmentRespDTO save (int userId,String plate,AppointmentDTO appointment) {
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        Vehicle vehicle = vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(appointment.date(), formatter);
        List<Appointment> appointments= appointmentDAO.findAppointmentsByDate(date);
        if(date.isBefore(LocalDate.now())|| date.isEqual(LocalDate.now())) {
            throw new BadRequestException("Data non valida");
        }
        Appointment appointment1= this.appointmentDAO.findByVehicleAndDate(vehicle, date).orElseThrow();
        if (appointmentDAO.findByVehicleAndDate(vehicle, date).isPresent() && appointment1.getHour() == parseInt(appointment.hour())) {
            throw new BadRequestException("Veicolo già prenotato");
        }
        if (!appointments.isEmpty()) {
            for (Appointment a : appointments) {
                if (a.getHour()== parseInt(appointment.hour())) {
                    throw new BadRequestException("L'ora inserita non è disponibile");
                }
            }
        }
        Appointment newAppointment = new Appointment(LocalDate.parse(appointment.date(),formatter),parseInt(appointment.hour()) , vehicle, user);
        this.appointmentDAO.save(newAppointment);
        return new AppointmentRespDTO(Integer.toString(newAppointment.getId()), newAppointment.getDate().toString(),Integer.toString(newAppointment.getHour()), newAppointment.getVehicle().getPlate());
    }
}
