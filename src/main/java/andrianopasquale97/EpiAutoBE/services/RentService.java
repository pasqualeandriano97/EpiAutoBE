package andrianopasquale97.EpiAutoBE.services;

import andrianopasquale97.EpiAutoBE.entities.Maintenance;
import andrianopasquale97.EpiAutoBE.entities.Rent;
import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import andrianopasquale97.EpiAutoBE.entities.enums.State;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.exceptions.NotFoundException;
import andrianopasquale97.EpiAutoBE.payloads.*;
import andrianopasquale97.EpiAutoBE.repositories.MaintenanceDAO;
import andrianopasquale97.EpiAutoBE.repositories.RentDAO;
import andrianopasquale97.EpiAutoBE.repositories.UserDAO;
import andrianopasquale97.EpiAutoBE.repositories.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

@Service
public class RentService {
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleDAO vehicleDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private RentDAO rentDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private MaintenanceDAO maintenanceDAO;

    public Rent show(int id, String plate, RentDTO rent) throws ParseException {
        List<Rent> rents = rentDAO.findByVehicle(plate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(rent.date(), formatter);
        List<Rent> rents2 = rentDAO.findRentsByDate(date);

        if (!rents.isEmpty()) {
            for (Rent r : rents) {
                LocalDate startDate = r.getStartDate();
                LocalDate endDate = r.getEndDate();
                LocalDate newRentdate =LocalDate.parse( rent.startDate(),formatter);
                LocalDate newRentdate2 = LocalDate.parse(rent.endDate(), formatter);
                if ((newRentdate.isAfter(startDate)|| newRentdate.isEqual(startDate)) && (newRentdate.isBefore(endDate)|| newRentdate.isEqual(endDate))) {
                    throw new BadRequestException("Il veicolo è già in uso");
                }
                if ((newRentdate2.isAfter(startDate)|| newRentdate2.isEqual(startDate)) && (newRentdate2.isBefore(endDate)|| newRentdate2.isEqual(endDate))) {
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
       List<Maintenance> maintenances=  this.maintenanceDAO.findByDateBetweenStartAndEndDate(LocalDate.parse(rent.startDate(),formatter));
       if(!maintenances.isEmpty()) {
           for (Maintenance m : maintenances) {
               if (m.getVehicle().getPlate().equals(plate)) {
                   throw new BadRequestException("Il veicolo non può essere noleggiato in questo giorno");
               }}
       }
        if(!vehicle.getState().equals(State.AVAILABLE)){
            throw new BadRequestException("Il veicolo non è disponibile");
        }
        if (LocalDate.parse(rent.date(),formatter).isAfter(LocalDate.parse(rent.startDate(), formatter))) {
            throw new BadRequestException("La data dell'appuntamento non può essere successiva alla data di inizio del noleggio");
        }
        if(LocalDate.parse(rent.startDate(),formatter).isAfter(LocalDate.parse(rent.endDate(), formatter))) {
            throw new BadRequestException("La data di fine del noleggio non può essere precedente alla data di inizio del noleggio");
        }
        if(LocalDate.parse(rent.date(), formatter).isAfter(LocalDate.parse(rent.endDate(), formatter))) {
            throw new BadRequestException("La data di fine del noleggio non può essere precedente alla data dell'appuntamento");
        }
        if(LocalDate.parse(rent.startDate(), formatter).isBefore(LocalDate.now())|| LocalDate.parse(rent.endDate(), formatter).isBefore(LocalDate.now())) {
            throw new BadRequestException("La data di inizio o fine del noleggio non può essere precedente alla data odierna");
        }
        if(LocalDate.parse(rent.startDate(), formatter).isEqual(LocalDate.now())|| LocalDate.parse(rent.endDate(), formatter).isEqual(LocalDate.now())) {
            throw new BadRequestException("La data di inizio del noleggio non può essere uguale alla data odierna");
        }
        int time= parseInt(rent.startHour());
        return new Rent(LocalDate.parse(rent.startDate(), formatter), LocalDate.parse(rent.endDate(), formatter), LocalDate.parse(rent.date(), formatter), time, vehicle, user);
    }


    public RentRespDTO save(int userId,RentPayloadDTO rent){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        Vehicle vehicle = vehicleDAO.findById(rent.vehicle()).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        LocalDate startDate = LocalDate.parse(rent.startDate(),formatter);
        LocalDate endDate = LocalDate.parse(rent.endDate(),formatter);
        LocalDate date = LocalDate.parse(rent.date(),formatter);
        Rent newrent = new Rent(startDate, endDate, date,Integer.parseInt(rent.time()), vehicle, user);
        rentDAO.save(newrent);
        return new RentRespDTO(newrent.getId(),rent.startDate(),rent.endDate(),rent.date(),rent.time(),vehicle.getBrand(),vehicle.getModel(),vehicle.getYear());

    }

    public Page<Rent> getAllRents(int page, int size, String sortBy) {
        if(size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size,Sort.by("startDate").descending());
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
        List<Rent> rents=this.rentDAO.findActiveRentsToday();
        if (rents.isEmpty()) {
            throw new NotFoundException("Nessun noleggio attivo per oggi");
        }
        return rents;
    }

    public Rent findbyRentId(int rentId) {
        return rentDAO.findById(rentId).orElseThrow(() -> new NotFoundException("Noleggio non trovato"));
    }

    public List<Rent> findRentByDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date1 = LocalDate.parse(date, formatter);
       List<Rent> rents = this.rentDAO.findByDateBetweenStartAndEndDate(date1);
        if (rents.isEmpty()) {
            throw new NotFoundException("Nessun noleggio trovato");
        }

        return rents;
    }

    public List<Rent> findRentByUser (String email) {
        User user = userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        List<Rent> rents = this.rentDAO.findByUser(user);
        if (rents.isEmpty()) {
            throw new NotFoundException("Nessun noleggio trovato");
        }
        return rents;
    }

    public RentRespDTO findbyRentIdandUpdate(int userId,int rentId, RentPostDTO rent){
        Rent rentToUpdate = rentDAO.findById(rentId).orElseThrow(() -> new NotFoundException("Noleggio non trovato"));
        Vehicle vehicle = rentToUpdate.getVehicle();
        if (!vehicle.getState().equals(State.AVAILABLE)) {
            throw new BadRequestException("Il veicolo non è disponibile");
        }
        if(rentToUpdate.getUser().getId() != userId) {
            throw new BadRequestException("Non sei autorizzato a modificare questo noleggio");}
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (rentToUpdate.getEndDate().isBefore(LocalDate.parse(rent.postDate(), formatter))) {
            rentToUpdate.setEndDate(LocalDate.parse(rent.postDate(), formatter));
            rentToUpdate.setPrice(rentToUpdate.calculatePrice());
            this.rentDAO.save(rentToUpdate);
        }else{
            throw new BadRequestException("La data di posticipazione fine noleggio non può essere precedente alla data di fine noleggio da modificare");
        }
      return new RentRespDTO(rentToUpdate.getId(), rentToUpdate.getStartDate().toString(),rentToUpdate.getEndDate().toString(),rentToUpdate.getDate().toString(),String.valueOf(rentToUpdate.getTime()),rentToUpdate.getVehicle().getBrand(), rentToUpdate.getVehicle().getModel(), rentToUpdate.getVehicle().getYear());
    }

    public List<Rent> getUpcomingRentsByUserId(int userId) {
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException("Utente con id: " + userId + " non trovato"));
        return rentDAO.findUpcomingRentsByUserId(user.getId());
    }

    public ResponseMessageDTO dismissRentByUserId(int userId, int rentId) {
       List<Rent> rents =this.rentDAO.findByUserId(userId);
        for (Rent rent : rents) {
            if (rent.getId() == rentId ) {
                if(rent.getStartDate().isBefore(LocalDate.now())|| rent.getStartDate().equals(LocalDate.now())) {
                throw new BadRequestException("Non puoi eliminare un noleggio in corso");
            }
                this.rentDAO.delete(rent);
                return new ResponseMessageDTO("Noleggio eliminato") ;
            }

        }
        throw new NotFoundException("Noleggio con id: " + rentId + " non trovato");
    }

    public ResponseMessageDTO dismissRentByRentId(int rentId) {
        this.findbyRentId(rentId);
        this.rentDAO.deleteById(rentId);
        return new ResponseMessageDTO("Noleggio eliminato");
    }

    public Rent show1(String email, String plate,RentDTO rent) throws ParseException {
        User user = userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente non trovato"));
       return this.show(user.getId(), plate, rent);
    }


}
