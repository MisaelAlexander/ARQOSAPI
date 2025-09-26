package PTC._5.Models.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HistorialDTO
{
    private Long idHistorial; // Usualmente no se valida si es autogenerado

    @NotNull(message = "La descripción es obligatoria")
    @Size( max = 500, message = "La descripción debe tener maximo 500 caracteres")
    @Size(min = 5,message = "La descripcion debe de tener minimo 5 caracteres ")
    private String descripcion;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private Date fecha;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;
}
