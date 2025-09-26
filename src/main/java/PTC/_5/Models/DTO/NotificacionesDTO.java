package PTC._5.Models.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacionesDTO
{
    private Long idnotificacion;

    @NotNull(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no debe superar 500 caracteres")
    private String descripcion;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idusuario;
}
