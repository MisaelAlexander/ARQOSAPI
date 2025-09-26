package PTC._5.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ComentariosDTO
{
    private Long idComentario;

    @NotBlank(message = "El comentario no puede estar vacío")
    @Size(max = 300, message = "Máximo 300 caracteres permitidos")
    private String comentario;

    @NotNull(message = "La puntuación es obligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "La puntuación mínima es 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "La puntuación máxima es 5.0")
    private BigDecimal puntuacion;

    @NotNull(message = "La fecha es obligatoria")
    private Date fecha;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID del inmueble es obligatorio")
    private Long idInmueble;

    // Solo título del inmueble (para mostrar)
    private String tituloInmueble;
    private String nombre;
}
