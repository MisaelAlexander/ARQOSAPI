package PTC._5.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ComentariosUsuarioDTO
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

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "El ID del vendedor es obligatorio")
    private Long idVendedor;

    // Datos del vendedor (solo para mostrar)
    private String nombreVendedor;
    private String apellidoVendedor;
    private String correoVendedor;
    private String usuarioVendedor;
    private String duiVendedor;
    private String telefonoVendedor;
    private String nombre;
}
