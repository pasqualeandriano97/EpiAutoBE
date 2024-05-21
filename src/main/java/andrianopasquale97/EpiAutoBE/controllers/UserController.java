package andrianopasquale97.EpiAutoBE.controllers;

import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.payloads.ResponseMessageDTO;
import andrianopasquale97.EpiAutoBE.payloads.UserDTO;
import andrianopasquale97.EpiAutoBE.payloads.UserRespDTO;
import andrianopasquale97.EpiAutoBE.payloads.UserUpdateDTO;
import andrianopasquale97.EpiAutoBE.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sortBy) {
        return userService.getAll(page, size, sortBy);
    }

    @GetMapping("/userName")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO getUserByNameAndSurname(@RequestParam String name, @RequestParam String surname) {
        return userService.getByNameAndSurname(name, surname);
    }


    @GetMapping("/email")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public User getUserByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public User getUser(@AuthenticationPrincipal User currentUser) {
        return userService.getById(currentUser.getId());
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseMessageDTO deleteUser(@PathVariable String email) {
        return userService.findByEmailAndDelete(email);
    }


    @PutMapping("/me")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public UserUpdateDTO updateUser(@AuthenticationPrincipal User currentUser, @Validated @RequestBody UserUpdateDTO user, BindingResult validation) {
        if(validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return userService.findByIdAndUpdate(currentUser.getId(), user);
    }

   @DeleteMapping("/me")
   @PreAuthorize("hasAuthority('USER')")
   @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseMessageDTO deleteCurrentUser(@AuthenticationPrincipal User currentUser) {
       return userService.findByIdAndDelete(currentUser.getId());
   }


}
