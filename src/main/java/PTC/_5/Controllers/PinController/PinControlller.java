package PTC._5.Controllers.PinController;

import PTC._5.Services.CorreoPin.PinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Pin")
@CrossOrigin
public class PinControlller
{

    @Autowired
    private PinService pinService;

    // Endpoint para generar y enviar el PIN
    @PostMapping("/enviar")
    public String enviarPin(@RequestParam String email) {
        pinService.generarYEnviarPin(email);
        return "PIN enviado a " + email;
    }

    // Endpoint para validar el PIN
    @PostMapping("/validar")
    public boolean validarPin(@RequestParam String email, @RequestParam String pin) {
        return pinService.validarPin(email, pin);
    }
}   
