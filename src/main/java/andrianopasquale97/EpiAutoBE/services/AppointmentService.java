package andrianopasquale97.EpiAutoBE.services;

import andrianopasquale97.EpiAutoBE.entities.Appointment;
import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import andrianopasquale97.EpiAutoBE.entities.enums.State;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.exceptions.NotFoundException;
import andrianopasquale97.EpiAutoBE.payloads.AppointmentDTO;
import andrianopasquale97.EpiAutoBE.payloads.AppointmentPayloadDTO;
import andrianopasquale97.EpiAutoBE.payloads.AppointmentRespDTO;
import andrianopasquale97.EpiAutoBE.payloads.ResponseMessageDTO;
import andrianopasquale97.EpiAutoBE.repositories.AppointmentDAO;
import andrianopasquale97.EpiAutoBE.repositories.UserDAO;
import andrianopasquale97.EpiAutoBE.repositories.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentDAO appointmentDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private VehicleDAO vehicleDAO;

    public List<AppointmentRespDTO> getAppointments(int page, int size,String sortby) {
        if (sortby == null) {
            sortby = "date";
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortby).descending());
        Page<Appointment> appointments = appointmentDAO.findAll(pageable);
        List<Appointment> appointmentList = appointments.getContent();
        List<AppointmentRespDTO> appointmentRespDTOS = new ArrayList<>();
        for (Appointment a : appointmentList) {
            appointmentRespDTOS.add(new AppointmentRespDTO(Integer.toString(a.getId()), a.getDate().toString(), Integer.toString(a.getHour()), a.getVehicle().getPlate(), a.getUser().getName(),a.getUser().getSurname(), a.getUser().getEmail()));
        }
        return appointmentRespDTOS;
    }

    public Appointment show (int userId,String plate,AppointmentDTO appointment) {
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        Vehicle vehicle = vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(appointment.date(), formatter);
        List<Appointment> appointments= appointmentDAO.findAppointmentsByDate(date);
        if(!vehicle.getState().equals(State.AVAILABLE)){
            throw new BadRequestException("Veicolo non disponibile");
        }
        if(date.isBefore(LocalDate.now())|| date.isEqual(LocalDate.now())) {
            throw new BadRequestException("Data non valida");
        }

        if (appointmentDAO.findByVehicleAndDate(vehicle, date).isPresent()) {
          Appointment appointment1 = this.appointmentDAO.findByVehicleAndDate(vehicle, date).get();
            if( appointment1.getHour() == parseInt(appointment.hour())){
                throw new BadRequestException("Veicolo già prenotato");
            }
        }
        if (!appointments.isEmpty()) {
            for (Appointment a : appointments) {
                if (a.getHour() == parseInt(appointment.hour())) {
                    throw new BadRequestException("L'ora inserita non è disponibile");
                }
            }
        }
        Appointment newAppointment = new Appointment(LocalDate.parse(appointment.date(),formatter),Integer.parseInt(appointment.hour()), vehicle, user);

        return newAppointment;
    }

    public AppointmentRespDTO save(int userId, AppointmentPayloadDTO appointment){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(appointment.date(), formatter);
        int hour =Integer.parseInt(appointment.hour());
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        Vehicle vehicle = vehicleDAO.findByPlate(appointment.vehicle()).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        Appointment newAppointment= new Appointment(date, hour, vehicle, user);
        this.appointmentDAO.save(newAppointment);
        return new AppointmentRespDTO(Integer.toString(newAppointment.getId()), newAppointment.getDate().toString(), Integer.toString(newAppointment.getHour()), newAppointment.getVehicle().getPlate(), newAppointment.getUser().getName(), newAppointment.getUser().getSurname(), newAppointment.getUser().getEmail());
    }

    public AppointmentRespDTO findAppointmentById(int appointmentId) {
        Appointment appointment = appointmentDAO.findById(appointmentId).orElseThrow(() -> new NotFoundException("Appuntamento non trovato"));
        return new AppointmentRespDTO(Integer.toString(appointment.getId()), appointment.getDate().toString(), Integer.toString(appointment.getHour()), appointment.getVehicle().getPlate(), appointment.getUser().getName(), appointment.getUser().getSurname(), appointment.getUser().getEmail());
    }

    public List<Appointment> findAppointmentsByUserId(int userId) {
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException("Utente non trovato"));

        return appointmentDAO.findAppointmentsByUserId(user.getId());
    }

    public List<AppointmentRespDTO> findAppointmentsByUserEmail(String email) {
        User user = userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        List<Appointment> appointments = appointmentDAO.findAppointmentsByUserId(user.getId());
        List<AppointmentRespDTO> appointmentRespDTOS = new ArrayList<>();
        for (Appointment a : appointments) {
            appointmentRespDTOS.add(new AppointmentRespDTO(Integer.toString(a.getId()), a.getDate().toString(), Integer.toString(a.getHour()), a.getVehicle().getPlate(), a.getUser().getName(), a.getUser().getSurname(), a.getUser().getEmail()));
        }
        return appointmentRespDTOS;
    }

    public AppointmentRespDTO findAppointmentByUserIdAndUpdate(int userId,int appointmentId, AppointmentDTO appointment) {
        List<Appointment> appointments = appointmentDAO.findAppointmentsByUserId(userId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(appointment.date(), formatter);
        List<Appointment> appointmentDate = appointmentDAO.findAppointmentsByDate(date);
        AppointmentRespDTO response = null;
        for (Appointment a : appointments) {
            if (a.getId() == appointmentId) {
                if (!a.getVehicle().getState().equals(State.AVAILABLE)) {
                    throw new BadRequestException("Veicolo non disponibile");
                }
                if (LocalDate.parse(appointment.date(), formatter).isBefore(LocalDate.now()) || LocalDate.parse(appointment.date(), formatter).isEqual(LocalDate.now())) {
                    throw new BadRequestException("Data non valida");
                }
                a.setDate(LocalDate.parse(appointment.date(), formatter));
                for (Appointment b : appointmentDate) {
                    if (b.getHour() == parseInt(appointment.hour())) {
                        throw new BadRequestException("L'ora inserita non è disponibile");
                    }
                }
                a.setHour(parseInt(appointment.hour()));
                appointmentDAO.save(a);
                response = new AppointmentRespDTO(Integer.toString(a.getId()), a.getDate().toString(), Integer.toString(a.getHour()), a.getVehicle().getPlate(), a.getUser().getName(), a.getUser().getSurname(), a.getUser().getEmail());
            }
        }
        return response;
    }

    public ResponseMessageDTO deleteAppointmentByUserId(int userId, int appointmentId) {
        Appointment appointment = appointmentDAO.findById(appointmentId).orElseThrow(() -> new NotFoundException("Prenotazione non trovata"));
        if (appointment.getUser().getId() == userId) {
            appointmentDAO.deleteById(appointmentId);
            return new ResponseMessageDTO( "Prenotazione eliminata");
        }
        throw new BadRequestException("Non sei autorizzato a cancellare questa prenotazione");
    }

    public List<AppointmentRespDTO> getAppointmentsByDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        List<Appointment> appointments = appointmentDAO.findAppointmentsByDate(LocalDate.parse(date, formatter));
        List<AppointmentRespDTO> appointmentRespDTOS = new ArrayList<>();
        if (appointments.isEmpty()) {
            throw new NotFoundException("Non ci sono prenotazioni per questa data");
        }
        for (Appointment a : appointments) {
            appointmentRespDTOS.add(new AppointmentRespDTO(Integer.toString(a.getId()), a.getDate().toString(), Integer.toString(a.getHour()), a.getVehicle().getPlate(), a.getUser().getName(), a.getUser().getSurname(), a.getUser().getEmail()));
        }
        return appointmentRespDTOS;
    }

    public List<AppointmentRespDTO> getAppointmentsByToday() {
        List<Appointment> appointments = appointmentDAO.findAppointmentsByToday(LocalDate.now());
        List<AppointmentRespDTO> appointmentRespDTOS = new ArrayList<>();
        if (appointments.isEmpty()) {
            throw new NotFoundException("Non ci sono prenotazioni per oggi");
        }
        for (Appointment a : appointments) {
            appointmentRespDTOS.add(new AppointmentRespDTO(Integer.toString(a.getId()), a.getDate().toString(), Integer.toString(a.getHour()), a.getVehicle().getPlate(), a.getUser().getName(), a.getUser().getSurname(), a.getUser().getEmail()));
        }
        return appointmentRespDTOS;
    }
}
