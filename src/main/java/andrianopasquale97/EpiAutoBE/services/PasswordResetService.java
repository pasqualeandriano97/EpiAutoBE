package andrianopasquale97.EpiAutoBE.services;


import andrianopasquale97.EpiAutoBE.entities.PasswordResetToken;
import andrianopasquale97.EpiAutoBE.entities.User;
import andrianopasquale97.EpiAutoBE.exceptions.BadRequestException;
import andrianopasquale97.EpiAutoBE.exceptions.NotFoundException;
import andrianopasquale97.EpiAutoBE.payloads.ResponseMessageDTO;
import andrianopasquale97.EpiAutoBE.repositories.PasswordResetTokenDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenDAO tokenRepository;

    @Autowired
    EmailSenderService emailSenderService;

    public ResponseMessageDTO reset (UUID token, String password){
        PasswordResetToken resetToken = tokenRepository.findById(token).orElseThrow(()->new NotFoundException("Il token non è stato trovato"));

        if (resetToken == null || resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new BadRequestException("Il token è scaduto");

        }

        User user = resetToken.getUser();
        userService.updatePassword(user, password);

        tokenRepository.delete(resetToken);

        return new ResponseMessageDTO("Password reimpostata con successo");
    }

    public ResponseMessageDTO request (String email){

        User user = userService.findByEmail(email);

        UUID token =userService.createPasswordResetTokenForUser(user);

        String resetUrl = "https://epi-auto-fe.vercel.app/auth/reset-password/" + token;
        emailSenderService.sendPasswordResetEmail(user.getEmail(), resetUrl);

        return new ResponseMessageDTO("Email di reimpostazione della password inviata");

    }

}
