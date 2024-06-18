package andrianopasquale97.EpiAutoBE.controllers;

import andrianopasquale97.EpiAutoBE.payloads.ResponseMessageDTO;
import andrianopasquale97.EpiAutoBE.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/request-password-reset")
    public ResponseMessageDTO requestPasswordReset(@RequestParam("email") String email) {
    return passwordResetService.request(email);
    }

    @PostMapping("/reset-password")
    public ResponseMessageDTO resetPassword(@RequestParam("token") UUID token, @RequestParam("newPassword") String newPassword) {
    return passwordResetService.reset(token, newPassword);
    }
}
