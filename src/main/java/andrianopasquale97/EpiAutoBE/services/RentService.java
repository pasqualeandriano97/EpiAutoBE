package andrianopasquale97.EpiAutoBE.services;

import andrianopasquale97.EpiAutoBE.entities.Rent;
import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import andrianopasquale97.EpiAutoBE.entities.enums.State;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.exceptions.NotFoundException;
import andrianopasquale97.EpiAutoBE.payloads.RentDTO;
import andrianopasquale97.EpiAutoBE.payloads.RentPostDTO;
import andrianopasquale97.EpiAutoBE.payloads.RentRespDTO;
import andrianopasquale97.EpiAutoBE.repositories.RentDAO;
import andrianopasquale97.EpiAutoBE.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.Integer.parseInt;

@Service
public class RentService {
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;
    @Autowired
    private RentDAO rentDAO;
    @Autowired
    private UserDAO userDAO;

    public RentRespDTO save(int id, String plate, RentDTO rent) throws ParseException {
        List<Rent> rents = rentDAO.findByVehicle(plate);
        LocalDate date = LocalDate.parse(rent.date());
        List<Rent> rents2 = rentDAO.findRentsByDate(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (!rents.isEmpty()) {
            for (Rent r : rents) {
                LocalDate startDate = r.getStartDate();
                LocalDate endDate = r.getEndDate();
                LocalDate newRentdate =LocalDate.parse( rent.startDate(),formatter);
                if (newRentdate.isAfter(startDate) && newRentdate.isBefore(endDate)) {
                    throw new BadRequestException("Il veicolo è già in uso");
                }
            }
        }
        if (!rents2.isEmpty()) {
            for (Rent r : rents2) {
                if (r.getTime() == parseInt(rent.startHour())) {
                    throw new BadRequestException("L'ora inserita non è disponibile");
                }
            }
        }

        User user = userDAO.findById(id).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        Vehicle vehicle = vehicleService.findByPlate(plate);
        if(!vehicle.getState().equals(State.AVAILABLE)){
            throw new BadRequestException("Il veicolo non è disponibile");
        }
        if (LocalDate.parse(rent.date()).isAfter(LocalDate.parse(rent.startDate()))) {
            throw new BadRequestException("La data dell'appuntamento non può essere successiva alla data di inizio del noleggio");
        }
        int time= parseInt(rent.startHour());
        Rent newRent = new Rent(LocalDate.parse(rent.startDate(), formatter), LocalDate.parse(rent.endDate(), formatter), LocalDate.parse(rent.date(), formatter), time, vehicle, user);
        rentDAO.save(newRent);
        return new RentRespDTO(newRent.getId(),rent.startDate(),rent.endDate(),rent.date(),rent.startHour(),vehicle.getBrand(),vehicle.getModel(),vehicle.getYear());
    }

    public Page<Rent> getAllRents(int page, int size, String sortBy) {
        if(size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return rentDAO.findAll(pageable);
    }

    public Page<Rent> getAllRentsByUserId(int id, int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return rentDAO.findByUserId(id, pageable);
    }


    public List<Rent> getAllRentsByVehicle(String plate) {
        return rentDAO.findByVehicle(plate);
    }

    public List<Rent> getActiveRentsToday() {
        return this.rentDAO.findActiveRentsToday();
    }

    public RentRespDTO findbyRentIdandUpdate(int userId,int rentId, RentPostDTO rent){
        Rent rentToUpdate = rentDAO.findById(rentId).orElseThrow(() -> new NotFoundException("Noleggio non trovato"));
        Vehicle vehicle = rentToUpdate.getVehicle();
        if (!vehicle.getState().equals(State.AVAILABLE)) {
            throw new BadRequestException("Il veicolo non è disponibile");
        }
        if(rentToUpdate.getId() != userId) {
            throw new BadRequestException("Non sei autorizzato a modificare questo noleggio");}
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (rentToUpdate.getEndDate().isBefore(LocalDate.parse(rent.postDate(), formatter))) {
            rentToUpdate.setEndDate(LocalDate.parse(rent.postDate(), formatter));
            this.rentDAO.save(rentToUpdate);
        }else{
            throw new BadRequestException("La data di posticipazione fine noleggio non può essere precedente alla data di fine noleggio da modificare");
        }
      return new RentRespDTO(rentToUpdate.getId(), rentToUpdate.getStartDate().toString(),rentToUpdate.getEndDate().toString(),rentToUpdate.getDate().toString(),rentToUpdate.toString(),rentToUpdate.getVehicle().getBrand(), rentToUpdate.getVehicle().getModel(), rentToUpdate.getVehicle().getYear());
    }

    public List<Rent> getUpcomingRentsByUserId(int userId) {
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException("Utente con id: " + userId + " non trovato"));
        return rentDAO.findUpcomingRentsByUserId(user.getId());
    }

    public String dismissRentByUserId(int userId, int rentId) {
       List<Rent> rents = this.getUpcomingRentsByUserId(userId);
        for (Rent rent : rents) {
            if (rent.getId() == rentId) {
                this.rentDAO.delete(rent);
                return "Noleggio eliminato";
            }
        }
        throw new NotFoundException("Noleggio con id: " + rentId + " non trovato");
    }
}
