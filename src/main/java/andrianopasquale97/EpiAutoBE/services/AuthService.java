package andrianopasquale97.EpiAutoBE.services;

import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.exceptions.UnauthorizedException;
import andrianopasquale97.EpiAutoBE.payloads.UserLoginDTO;
import andrianopasquale97.EpiAutoBE.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticateUserAndGenerateToken(UserLoginDTO payload){
        User utente = this.userService.findByEmail(payload.email());
        if(passwordEncoder.matches(payload.password(),utente.getPassword() )) {
            return jwtTools.createToken(utente);
        } else {
            throw new UnauthorizedException("Credenziali non valide! Effettua di nuovo il login!");
        }
    }
}