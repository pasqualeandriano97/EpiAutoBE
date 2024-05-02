package andrianopasquale97.EpiAutoBE.services;

import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import andrianopasquale97.EpiAutoBE.exceptions.NotFoundException;
import andrianopasquale97.EpiAutoBE.payloads.VehicleDTO;
import andrianopasquale97.EpiAutoBE.repositories.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    @Autowired
    private VehicleDAO vehicleDAO;
    public Page<Vehicle> getAllVehicles(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        if (sortBy == null) sortBy = "plate";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return vehicleDAO.findAll(pageable);
    }

    public Vehicle findByPlate(String plate) {
        return this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
    }

    public Vehicle save(VehicleDTO body) {
        return this.vehicleDAO.save(new Vehicle(body.plate(), body.fuelType(), body.brand(), body.model(), body.type(), body.year(), body.imageUrl()));
    }

    public String findByIdAndDelete(String plate) {
        Vehicle vehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        this.vehicleDAO.delete(vehicle);
        return "Veicolo eliminato con successo";
    }


    public VehicleDTO findByIdAndUpdate(String plate, VehicleDTO body) {
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
    public VehicleDTO findByPlateAndUpdateImage(String plate, String imageUrl) {
        Vehicle currentVehicle = this.vehicleDAO.findByPlate(plate).orElseThrow(() -> new NotFoundException("Veicolo non trovato"));
        currentVehicle.setImageUrl(imageUrl);
        this.vehicleDAO.save(currentVehicle);
        return new VehicleDTO(currentVehicle.getPlate(), currentVehicle.getBrand(), currentVehicle.getModel(), currentVehicle.getType(), currentVehicle.getFuelType(), currentVehicle.getYear(), currentVehicle.getImageUrl());
    }
}
