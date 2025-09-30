package PTC._5.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InmuebleDTO
{

    private Long idinmuebles;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 150, message = "El titulo no debe superar los 150 caracteres")
    @Size(min = 4, message = "El titulo debe de ser mas de 4 caracteres")
    private String titulo;

    @NotNull(message = "El tipo es obligatorio")
    private Long idtipo;

    @NotNull(message = "La ubicacion es obligatoria")
    private Long idubicacion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @DecimalMin(value = "50.0", inclusive = true, message = "El precio debe ser al menos 50 dólares")
    private Double precio;

    @NotNull(message = "La longitud es obigatoria")
    @DecimalMin(value = "-180.00", message = "La longitud minima es -180")
    @DecimalMax(value = "180.00", message = "La longitud maxima es 180 ")
    private Double longitud;

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.00", message = "La latitud  minima es -90")
    @DecimalMax(value = "90.00", message = "La latitud maxima es 90 ")
    private Double latitud;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 1000, message = "La descripicon de la casa puede tener maximo 1000 caracteres")
    private String descripcion;
    @NotNull(message = "El estado no es nulo")
    private Boolean estado;

    @NotNull(message = "El usuario es obligatoria")
    private Long idusuario;

    @NotNull(message = "El numero de baños es obligatoria")
    private Long idbanios;

    @NotNull(message = "El numero de habitaciones es obligatoria")
    private Long idhabitaciones;

    private String tipo;
    private String ubicacion;
    private String usuariofoto;
    private String usuarionombre;
    private String usuariocorreo;
    private String usuariotelefono;
    private String banios;
    private String habitaciones;


}
