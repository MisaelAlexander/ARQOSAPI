package PTC._5.Models.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UsuarioDTO {
    private Long idusuario;

    @Size(max = 50, message =  "El nombre debe tener maximo 50 caracteres")
    @Size(min = 4, message = "El nombre debe tener minimo 4 caracteres")
    private String usuario;

    /*
* ^                              inicio de la cadena
(?=.*[a-z])                         al menos una letra minúscula
(?=.*[A-Z])                         al menos una letra mayúscula
(?=.*\\d)                           al menos un número
(?=.*[!@#$%^&*()_+...])             al menos un carácter especial
.{8,}                               mínimo 8 caracteres
$ */
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un símbolo especial"
    )
    @NotBlank(message = "La contraseña es obligatoria")
    private  String contrasenia;


    @NotNull(message = "El estado es obligatorio")

    private Boolean estado;

    @NotNull(message = "La fecha no de be de ser nula")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "La fecha de creacion no puede estar en el futuro")
    private Date fechadecreacion;



   @NotNull(message = "Es obligatorio el id de una descripcion")
    private Long iddescripcion;

   private  String Nombre;
   private  String Apellido;
   private  String Dui;
   private  String Ubicacion;
   private  String Rol;
   private  String Direccion;
   private  String Correo;
   private  String Telefono;
   private  String FotoPerfil;
   private  Date   FechadeNacimiento;
   private  Boolean EstadoD;
   private  Long    IDRol;
   private  Long    IDUbicacion;

}
