package PTC._5.Models.DTO;

import jakarta.mail.Message;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO
{
    @NotBlank(message = "Usuarios es obligatorio")
    private String usuario;

    @NotBlank(message = "Contraseña es obliagtoria")
    private String contrasena;


}
