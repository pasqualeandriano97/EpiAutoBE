package andrianopasquale97.EpiAutoBE.controllers;

import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.payloads.AppointmentDTO;
import andrianopasquale97.EpiAutoBE.payloads.AppointmentRespDTO;
import andrianopasquale97.EpiAutoBE.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AppointmentRespDTO> getAppointments(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "date") String sortBy) {
        return appointmentService.getAppointments(page, size, sortBy);
    }

    @GetMapping("/date")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AppointmentRespDTO> getAppointmentsByDate(@RequestParam String date){
        return appointmentService.getAppointmentsByDate(date);
    }

    @GetMapping("/today")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AppointmentRespDTO> getAppointmentsByToday() {
        return appointmentService.getAppointmentsByToday();
    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AppointmentRespDTO getAppointmentById(@PathVariable int appointmentId) {
        return appointmentService.findAppointmentById(appointmentId);
    }

    @GetMapping("/me")
    public List<AppointmentRespDTO> getMyAppointments(@AuthenticationPrincipal User user) {
        return appointmentService.findAppointmentsByUserId(user.getId());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AppointmentRespDTO> getAppointmentsByUserId(@PathVariable int userId) {
        return appointmentService.findAppointmentsByUserId(userId);
    }

    @PutMapping("/me")
    public AppointmentRespDTO updateAppointment(@AuthenticationPrincipal User user,
                                                @RequestParam String appointmentId,
                                                @Validated @RequestBody AppointmentDTO appointment, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return appointmentService.findAppointmentByUserIdAndUpdate(user.getId(), Integer.parseInt(appointmentId), appointment);
    }

    @PostMapping("/me")
    public AppointmentRespDTO createAppointment(@AuthenticationPrincipal User user,
                                                @RequestParam String plate,
                                                @Validated @RequestBody AppointmentDTO appointment, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return appointmentService.save(user.getId(),plate, appointment);
    }

    @DeleteMapping("/{appointmentId}")
    public String deleteAppointment(@AuthenticationPrincipal User user, @PathVariable String appointmentId) {
        return appointmentService.deleteAppointmentByUserId(user.getId(), Integer.parseInt(appointmentId));
    }
}