package andrianopasquale97.EpiAutoBE.services;

import andrianopasquale97.EpiAutoBE.entities.Maintenance;
import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import andrianopasquale97.EpiAutoBE.entities.enums.State;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.exceptions.NotFoundException;
import andrianopasquale97.EpiAutoBE.payloads.*;
import andrianopasquale97.EpiAutoBE.repositories.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class VehicleService {
    @Autowired
    private VehicleDAO vehicleDAO;
    public Page<Vehicle> getAllVehicles(int page, int size, String sortBy) {
        if (size > 20) size = 20;


        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Pageable pageable2 = PageRequest.of(page, size);

        if (sortBy == null || sortBy.equals("imageUrl")) {
            return vehicleDAO.findAllOrderedByImageUrl(pageable2);
        } else {
            return vehicleDAO.findAll(pageable);
        }
    }

    public Vehicle findByPlate(String plate) {
        return this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
    }

    public Vehicle save(VehicleDTO body) {
        if (this.vehicleDAO.findByPlate(body.plate()).isPresent()) {
            throw new BadRequestException("Veicolo già presente");
        }
      Vehicle newVehicle= new Vehicle(body.plate(), body.fuelType(), body.brand(), body.model(), body.type(), body.year(), body.imageUrl());
      newVehicle.setState("DISPONIBILE");
      this.vehicleDAO.save(newVehicle);
        return newVehicle;
    }

    public ResponseMessageDTO findByPlateAndDelete(String plate) {
        Vehicle vehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        this.vehicleDAO.delete(vehicle);
        return new ResponseMessageDTO("Veicolo eliminato con successo") ;
    }


    public VehicleDTO findByPlateAndUpdate(String plate, VehicleUpdateDTO body) {
        Vehicle currentVehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        currentVehicle.setBrand(body.brand());
        currentVehicle.setModel(body.model());
        currentVehicle.setType(body.type());
        currentVehicle.setFuelType(body.fuelType());
        currentVehicle.setYear(body.year());
        currentVehicle.setImageUrl(body.imageUrl());
        this.vehicleDAO.save(currentVehicle);
        return new VehicleDTO(currentVehicle.getPlate(), currentVehicle.getFuelType(),currentVehicle.getBrand(), currentVehicle.getModel(), currentVehicle.getType(), currentVehicle.getYear(), currentVehicle.getImageUrl());
    }

    public VehicleDTO findByPlateAndUpdateImage(String plate, VehicleImageDTO imageUrl) {
        Vehicle currentVehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        currentVehicle.setImageUrl(imageUrl.imageUrl());
        this.vehicleDAO.save(currentVehicle);
        return new VehicleDTO(currentVehicle.getPlate(),currentVehicle.getFuelType(), currentVehicle.getBrand(), currentVehicle.getModel(), currentVehicle.getType(),  currentVehicle.getYear(), currentVehicle.getImageUrl());
    }

    public ResponseMessageDTO updateAllVehicles() {
        List<Vehicle> vehicles = this.vehicleDAO.findAll();
        Iterator<Vehicle> vehicleIterator = vehicles.iterator();
        while (vehicleIterator.hasNext()) {
            Vehicle v = vehicleIterator.next();
            List<Maintenance> maintenances = new ArrayList<>(v.getMaintenances());
            Iterator<Maintenance> maintenanceIterator = maintenances.iterator();
            while (maintenanceIterator.hasNext()) {
                Maintenance m = maintenanceIterator.next();
                if (LocalDate.now().isAfter(m.getStartDate().minusDays(3)) && LocalDate.now().isBefore(m.getEndDate().plusDays(3))) {
                    v.setState("IN MANUTENZIONE");
                    this.vehicleDAO.save(v);
                } else {
                    v.setState("DISPONIBILE");
                }
            }
        }
        return new ResponseMessageDTO("Stato dei veicoli aggiornato con successo");
    }

    public VehicleRespDTO rentCar(String plate) {
        Vehicle currentVehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        if(currentVehicle.getState().equals(State.SOLD)){
            throw new BadRequestException("Il veicolo è stato venduto");
        }
        if(currentVehicle.getState().equals(State.RENTED)){
            throw new BadRequestException("Il veicolo è già noleggiato");
        }
        currentVehicle.setState("NOLEGGIATA");
        this.vehicleDAO.save(currentVehicle);
        return new VehicleRespDTO(currentVehicle.getPlate(), currentVehicle.getBrand(), currentVehicle.getModel(), currentVehicle.getType(), currentVehicle.getFuelType(), currentVehicle.getYear(),currentVehicle.getState().toString(), currentVehicle.getImageUrl());
    }

    public VehicleRespDTO returnCar(String plate) {
        Vehicle currentVehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        if (currentVehicle.getState().equals(State.AVAILABLE)) {
            throw new BadRequestException("Veicolo già disponibile");
        }
        if (currentVehicle.getState().equals(State.SOLD)) {
            throw new BadRequestException("Il veicolo è stato venduto");
        }
        currentVehicle.setState("DISPONIBILE");
        this.vehicleDAO.save(currentVehicle);
        return new VehicleRespDTO(currentVehicle.getPlate(), currentVehicle.getBrand(), currentVehicle.getModel(), currentVehicle.getType(), currentVehicle.getFuelType(), currentVehicle.getYear(),currentVehicle.getState().toString(), currentVehicle.getImageUrl());
    }

    public VehicleRespDTO sellCar(String vehicle) {
        Vehicle currentVehicle = this.vehicleDAO.findByPlate(vehicle).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        if(currentVehicle.getState().equals(State.RENTED)){
            throw new BadRequestException("Veicolo in noleggio");
        }
        if(currentVehicle.getState().equals(State.SOLD)){
            throw new BadRequestException("Veicolo già venduto");
        }
        currentVehicle.setState("VENDUTA");
        this.vehicleDAO.save(currentVehicle);
        return new VehicleRespDTO(currentVehicle.getPlate(), currentVehicle.getBrand(), currentVehicle.getModel(), currentVehicle.getType(), currentVehicle.getFuelType(), currentVehicle.getYear(), currentVehicle.getState().toString(), currentVehicle.getImageUrl());
    }
}
