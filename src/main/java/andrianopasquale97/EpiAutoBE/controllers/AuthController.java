package andrianopasquale97.EpiAutoBE.controllers;

import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.payloads.UserDTO;
import andrianopasquale97.EpiAutoBE.payloads.UserLoginDTO;
import andrianopasquale97.EpiAutoBE.payloads.UserLoginRespDTO;
import andrianopasquale97.EpiAutoBE.payloads.UserRespDTO;
import andrianopasquale97.EpiAutoBE.services.AuthService;
import andrianopasquale97.EpiAutoBE.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRespDTO saveUser(@RequestBody @Validated UserDTO body, BindingResult validation){
        if(validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return new UserRespDTO(this.userService.save(body).email());
    }


    @PostMapping("/login")
    public UserLoginRespDTO login(@RequestBody @Validated UserLoginDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return new UserLoginRespDTO(this.authService.authenticateUserAndGenerateToken(body));
    }

}
