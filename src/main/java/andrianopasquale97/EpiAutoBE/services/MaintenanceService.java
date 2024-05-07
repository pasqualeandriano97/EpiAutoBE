package andrianopasquale97.EpiAutoBE.services;

import andrianopasquale97.EpiAutoBE.entities.Maintenance;
import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.payloads.MaintenanceDTO;
import andrianopasquale97.EpiAutoBE.payloads.MaintenancePostDTO;
import andrianopasquale97.EpiAutoBE.payloads.MaintenanceRespDTO;
import andrianopasquale97.EpiAutoBE.repositories.MaintenanceDAO;
import andrianopasquale97.EpiAutoBE.repositories.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceService {
    @Autowired
    private MaintenanceDAO maintenanceDAO;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleDAO vehicleDAO;

    public MaintenanceRespDTO save (MaintenanceDTO maintenance){
        Vehicle vehicle = vehicleService.findByPlate(maintenance.vehiclePlate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse(maintenance.startDate(), formatter);
        LocalDate endDate = LocalDate.parse(maintenance.endDate(), formatter);
        int startEndDateComparison = startDate.compareTo(endDate);
        int currentDateComparison = LocalDate.now().compareTo(startDate);
        switch (startEndDateComparison) {
            case 1:
                throw new BadRequestException("La data di inizio non può essere successiva alla data di fine");
            case 0:
                throw new BadRequestException("La data di inizio e di fine non possono essere uguali");
            case -1:
                if (endDate.isBefore(LocalDate.now())) {
                    throw new BadRequestException("La data di fine non può essere precedente alla data attuale");
                } else {
                    if (currentDateComparison == 1) {
                        vehicle.setState("IN MANUTENZIONE");
                        vehicleDAO.save(vehicle);
                    }
                }
                break;
        }
        Maintenance newMaintenance= new Maintenance(startDate,endDate,vehicle);
        maintenanceDAO.save(newMaintenance);
        return new MaintenanceRespDTO(maintenance.startDate(), maintenance.endDate(), vehicle.getPlate());
    }

    public Page<MaintenanceRespDTO> getAllMaintenances(int page, int size,String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Maintenance> maintenancesPage = maintenanceDAO.findAll(pageable);
        List<MaintenanceRespDTO> maintenanceRespDTOs = new ArrayList<>();
        for (Maintenance maintenance : maintenancesPage.getContent()) {
            maintenanceRespDTOs.add(new MaintenanceRespDTO(maintenance.getStartDate().toString(), maintenance.getEndDate().toString(), maintenance.getVehicle().getPlate()));
        }
        return new PageImpl<>(maintenanceRespDTOs, pageable, maintenancesPage.getTotalElements());
    }

    public List<MaintenanceRespDTO> findByPlate(String plate) {
       List<Maintenance> maintenance = maintenanceDAO.findByVehiclePlate(plate);
        if (maintenance.isEmpty()) {
            throw new BadRequestException("L'auto con targa "+ plate +" non ha ancora una manutenzione");
        }
        List<MaintenanceRespDTO> maintenanceRespDTOs = new ArrayList<>();
       for (Maintenance maintenance1 : maintenance) {
        maintenanceRespDTOs.add(new MaintenanceRespDTO(maintenance1.getStartDate().toString(), maintenance1.getEndDate().toString(), maintenance1.getVehicle().getPlate())) ;
    }
       return maintenanceRespDTOs;
    }

    public MaintenanceRespDTO updateMaintenance(MaintenancePostDTO maintenanceDTO) {
       Maintenance maintenance = maintenanceDAO.findById(Integer.parseInt(maintenanceDTO.maintenanceId())).orElseThrow(() -> new BadRequestException("Manutenzione non trovata"));
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate newDate = LocalDate.parse(maintenanceDTO.endDate(), formatter);
        maintenance.setEndDate(newDate);
        maintenanceDAO.save(maintenance);
        return new MaintenanceRespDTO(maintenance.getStartDate().toString(), maintenance.getEndDate().toString(), maintenance.getVehicle().getPlate());
    }

}
