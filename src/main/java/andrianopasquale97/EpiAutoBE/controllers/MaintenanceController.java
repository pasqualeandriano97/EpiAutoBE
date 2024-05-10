package andrianopasquale97.EpiAutoBE.controllers;

import andrianopasquale97.EpiAutoBE.entities.Maintenance;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.payloads.MaintenanceDTO;
import andrianopasquale97.EpiAutoBE.payloads.MaintenancePostDTO;
import andrianopasquale97.EpiAutoBE.payloads.MaintenanceRespDTO;
import andrianopasquale97.EpiAutoBE.services.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {
    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<MaintenanceRespDTO> getMaintenances(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id") String sortBy) {
        return maintenanceService.getAllMaintenances(page, size, sortBy);
    }

    @GetMapping("/vehicle")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<MaintenanceRespDTO> getMaintenanceById(@RequestParam String plate) {
        return maintenanceService.findByPlate(plate);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public MaintenanceRespDTO saveMaintenance(@Validated @RequestBody MaintenanceDTO maintenance,BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return maintenanceService.save(maintenance);
    }

    @PatchMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public MaintenanceRespDTO updateMaintenance(@Validated @RequestBody MaintenancePostDTO maintenance, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return maintenanceService.updateMaintenance(maintenance);
    }

}
