package andrianopasquale97.EpiAutoBE.controllers;

import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.payloads.VehicleDTO;
import andrianopasquale97.EpiAutoBE.payloads.VehicleImageDTO;
import andrianopasquale97.EpiAutoBE.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<Vehicle> getVehicles(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     @RequestParam(defaultValue = "plate") String sortBy){
        return vehicleService.getAllVehicles(page, size, sortBy);
    }

    @GetMapping("/plate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Vehicle getVehicleByPlate(@RequestParam String plate) {
        return vehicleService.findByPlate(plate);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Vehicle saveVehicle(@Validated @RequestBody VehicleDTO vehicle, BindingResult validation) {
        if(validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return vehicleService.save(vehicle);
    }


    @DeleteMapping("/plate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteVehicleByPlate(@RequestParam String plate) {
        return vehicleService.findByPlateAndDelete(plate);
    }

    @PutMapping("/plate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public VehicleDTO updateVehicleByPlate(@RequestParam String plate, @Validated @RequestBody VehicleDTO vehicle, BindingResult validation) {
        if(validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return vehicleService.findByPlateAndUpdate(plate, vehicle);
    }

    @PatchMapping("/plate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public VehicleDTO patchVehicleByPlate(@RequestParam String plate,@Validated @RequestBody VehicleImageDTO vehicle, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return vehicleService.findByPlateAndUpdateImage(plate, vehicle);
    }
}
