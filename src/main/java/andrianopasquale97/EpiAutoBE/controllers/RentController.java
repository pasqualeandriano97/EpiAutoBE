package andrianopasquale97.EpiAutoBE.controllers;

import andrianopasquale97.EpiAutoBE.entities.Rent;
import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.payloads.RentDTO;
import andrianopasquale97.EpiAutoBE.payloads.RentPostDTO;
import andrianopasquale97.EpiAutoBE.payloads.RentRespDTO;
import andrianopasquale97.EpiAutoBE.services.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/rent")
public class RentController {
    @Autowired
    private RentService rentService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Rent> getRent(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(defaultValue = "id") String sortBy) {
        return rentService.getAllRents(page, size, sortBy);
    }

    @GetMapping("/me")
    public Page<Rent> getMyRents(@AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size,
                                 @RequestParam(defaultValue = "id") String sortBy) {
       return this.rentService.getAllRentsByUserId(user.getId(), page, size, sortBy);
    }

    @GetMapping("/vehicle/plate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Rent> getRentByVehicle(@RequestParam String plate) {
        return this.rentService.getAllRentsByVehicle(plate);
    }
    @GetMapping("/vehicle/today")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Rent> getRentByVehicleActiveNow() {
        return this.rentService.getActiveRentsToday();
    }

    @PatchMapping("/post")
    public RentRespDTO postRent(@AuthenticationPrincipal User user,@RequestParam int rentId,@Validated @RequestBody RentPostDTO date,BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());}
        return this.rentService.findbyRentIdandUpdate(user.getId(),rentId, date);
    }

    @DeleteMapping("/me")
    public String deleteMyRents(@AuthenticationPrincipal User user,@RequestParam int rentId) {
        return this.rentService.dismissRentByUserId(user.getId(), rentId);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RentRespDTO saveRent(@AuthenticationPrincipal User user, @RequestParam String plate, @Validated @RequestBody RentDTO rent, BindingResult validation) throws ParseException {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return this.rentService.save(user.getId(), plate,rent);
    }
}
