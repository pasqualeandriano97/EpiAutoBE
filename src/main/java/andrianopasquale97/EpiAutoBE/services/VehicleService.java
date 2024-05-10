package andrianopasquale97.EpiAutoBE.services;

import andrianopasquale97.EpiAutoBE.entities.Maintenance;
import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.exceptions.NotFoundException;
import andrianopasquale97.EpiAutoBE.payloads.VehicleDTO;
import andrianopasquale97.EpiAutoBE.payloads.VehicleImageDTO;
import andrianopasquale97.EpiAutoBE.payloads.VehicleRespDTO;
import andrianopasquale97.EpiAutoBE.payloads.VehicleUpdateDTO;
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
import java.util.Vector;

@Service
public class VehicleService {
    @Autowired
    private VehicleDAO vehicleDAO;
    public Page<Vehicle> getAllVehicles(int page, int size, String sortBy) {
        if (size > 20) size = 20;
        if (sortBy == null) sortBy = "plate";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return vehicleDAO.findAll(pageable);
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

    public String findByPlateAndDelete(String plate) {
        Vehicle vehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        this.vehicleDAO.delete(vehicle);
        return "Veicolo eliminato con successo";
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
        return new VehicleDTO(currentVehicle.getPlate(),currentVehicle.getBrand(), currentVehicle.getModel(), currentVehicle.getType(), currentVehicle.getFuelType(), currentVehicle.getYear(), currentVehicle.getImageUrl());
    }
    public VehicleDTO findByPlateAndUpdateImage(String plate, VehicleImageDTO imageUrl) {
        Vehicle currentVehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        currentVehicle.setImageUrl(imageUrl.imageUrl());
        this.vehicleDAO.save(currentVehicle);
        return new VehicleDTO(currentVehicle.getPlate(), currentVehicle.getBrand(), currentVehicle.getModel(), currentVehicle.getType(), currentVehicle.getFuelType(), currentVehicle.getYear(), currentVehicle.getImageUrl());
    }

    public void updateAllVehicles() {
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
    }

    public VehicleRespDTO rentCar(String plate) {
        Vehicle currentVehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        currentVehicle.setState("NOLEGGIATA");
        this.vehicleDAO.save(currentVehicle);
        return new VehicleRespDTO(currentVehicle.getPlate(), currentVehicle.getBrand(), currentVehicle.getModel(), currentVehicle.getType(), currentVehicle.getFuelType(), currentVehicle.getYear(),currentVehicle.getState().toString(), currentVehicle.getImageUrl());
    }

    public VehicleRespDTO returnCar(String plate) {
        Vehicle currentVehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        currentVehicle.setState("DISPONIBILE");
        this.vehicleDAO.save(currentVehicle);
        return new VehicleRespDTO(currentVehicle.getPlate(), currentVehicle.getBrand(), currentVehicle.getModel(), currentVehicle.getType(), currentVehicle.getFuelType(), currentVehicle.getYear(),currentVehicle.getState().toString(), currentVehicle.getImageUrl());
    }
}
