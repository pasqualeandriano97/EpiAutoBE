package andrianopasquale97.EpiAutoBE.controllers;

import andrianopasquale97.EpiAutoBE.entities.Rent;
import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.payloads.*;
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
    public Page<Rent> getRents(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(defaultValue = "startDate") String sortBy) {
        return rentService.getAllRents(page, size, sortBy);
    }
    @GetMapping("/{rentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Rent getRentById(@PathVariable int rentId) {
        return rentService.findbyRentId(rentId);
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
    @GetMapping("/today")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Rent> getRentByVehicleActiveNow() {
        return this.rentService.getActiveRentsToday();
    }

    @GetMapping("/date")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Rent> getRentByVehicleActiveOnDate(@RequestParam String date) throws ParseException {
        return this.rentService.findRentByDate(date);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Rent> getRentByUser(@RequestParam String email) {
        return this.rentService.findRentByUser(email);
    }

    @PatchMapping("/post")
    public RentRespDTO postRent(@AuthenticationPrincipal User user,@RequestParam int rentId,@Validated @RequestBody RentPostDTO date,BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());}
        return this.rentService.findbyRentIdandUpdate(user.getId(),rentId, date);
    }

    @DeleteMapping("/me")
    public ResponseMessageDTO deleteMyRents(@AuthenticationPrincipal User user, @RequestParam int rentId) {
        return this.rentService.dismissRentByUserId(user.getId(), rentId);
    }


    @DeleteMapping("/{rentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseMessageDTO deleteRent( @PathVariable int rentId) {
        return this.rentService.dismissRentByRentId( rentId);
    }

    @PostMapping("")
    public Rent showRent(@AuthenticationPrincipal User user, @RequestParam String plate, @Validated @RequestBody RentDTO rent, BindingResult validation) throws ParseException {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return this.rentService.show(user.getId(), plate,rent);
    }

    @PostMapping("/save")
    public RentRespDTO saveRent(@AuthenticationPrincipal User user,@RequestBody RentPayloadDTO rent) {
        return this.rentService.save(user.getId(),rent);
    }
    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Rent showRentAdmin(@RequestParam String email,@RequestParam String plate, @Validated @RequestBody RentDTO rent, BindingResult validation) throws ParseException {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return this.rentService.show1(email, plate,rent);
    }

    @PostMapping("/save/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RentRespDTO saveRentAdmin(@RequestParam int id,@RequestBody RentPayloadDTO rent) {
        return this.rentService.save(id,rent);
    }
}
