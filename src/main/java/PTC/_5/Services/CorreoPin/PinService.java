package PTC._5.Services.CorreoPin;

import PTC._5.Utils.PinDato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PinService
{
    @Autowired
    /*Viene de uan nueva dependencia*/
    private JavaMailSender mailSender;

    private final ConcurrentHashMap<String, PinDato> pinStorage = new ConcurrentHashMap<>();

    // Generar PIN, guardar y enviar
    public void generarYEnviarPin(String emailDestino) {
        String pin = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        // Guardar en memoria
        pinStorage.put(emailDestino, new PinDato(pin, expiration));

        // Enviar por correo
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(emailDestino);
        mensaje.setSubject("Solicitud de Cambio de Contraseña");
        mensaje.setText("Tu código de verificación es: " + pin + "\nEste código expira en 5 minutos.");
        mailSender.send(mensaje);
    }

    // Validar PIN
    public boolean validarPin(String email, String pinIngresado)
    {
        //lo que ahce esto es obtener el pin se pide un valor y el pin
        //al ahcerlo hasmap permite obtener el pin por email
        //evitando que una persoan meta un pin de casualidad
        PinDato pinDato = pinStorage.get(email);

        //Si el pin va null por algun error pues retorna falso y no funciona
        if (pinDato == null) return false;
        //
        if (pinDato.isExpired()) {
            pinStorage.remove(email);
            return false;
        }
        return pinDato.getPin().equals(pinIngresado);
    }
}
