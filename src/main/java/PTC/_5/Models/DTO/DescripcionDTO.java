package PTC._5.Models.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@EqualsAndHashCode
public class DescripcionDTO {
    private Long iddescripcion;

    @NotBlank(message = "El nombre no puede ser nulo")
    @Size(max = 75, message = "El nombre no debe tener más de 75 caracteres")
    @Size(min = 3, message = "El nombre no puede tener menos de 3 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ '\\-]+$", message = "El nombre solo debe contener letras, espacios, apóstrofes o guiones")
    private String nombre;

    @NotBlank(message = "El apellido no puede ser nulo")
    @Size(max = 75, message = "El apellido no debe tener más de 75 caracteres")
    @Size(min = 3, message = "El apellido no puede tener menos de 3 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ '\\-]+$", message = "El apellido solo debe contener letras, espacios, apóstrofes o guiones")
    private String apellido;

    @NotBlank(message = "El DUI no puede ser nulo")
    @Pattern(regexp = "\\d{8}-\\d", message = "Formato de DUI inválido (ej: 12345678-9)")
    private String dui;

    @NotNull(message = "El ID de ubicación no debe ser nulo")
    private Long idubicacion;

    @NotNull(message = "El ID de rol no debe ser nulo")
    private Long idrol;

    @NotBlank(message = "La dirección no debe ser nula")
    @Size(max = 300, message = "La dirección no debe superar los 300 caracteres")
    @Size(min = 10, message = "La dirección es muy corta")
    private String direccion;

    @NotBlank(message = "El correo no debe ser nulo")
    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @NotBlank(message = "El teléfono no debe ser nulo")
    @Pattern(regexp = "\\+503\\d{8}", message = "El teléfono debe iniciar con +503 y tener 8 dígitos")
    private String telefono;


    @NotBlank(message = "Debe proporcionar una foto para su perfil")
    @Size(max = 500, message = "El enlace de la foto no debe superar los 500 caracteres")
    private String fotoperfil;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private Date fechadenacimiento;

    @NotNull(message = "El estado no puede ser nulo")
    private Boolean estado;

    // Campos opcionales para uso en GET
    private String ubicacion;
    private String rol;
}
