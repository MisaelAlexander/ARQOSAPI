package PTC._5.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class VisitaDTO
{
    private Long idvisita;

    @NotNull(message = "La fecha es obligatoria")
    private Date fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;


    @Size(max = 300, message = "La descripción puede tener máximo 300 caracteres")
    private String descripcion;

    @NotNull(message = "El ID del estado es obligatorio")
    private Long idestado;

    @NotNull(message = "El ID del inmueble es obligatorio")
    private Long idinmueble;

    @NotNull(message = "El ID del vendedor es obligatorio")
    private Long idvendedor;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idcliente;

    @NotNull(message = "El tipo de visita es obligatorio")
    private Long idtipovisita;

    // Datos extraídos - cliente
    private Long clienteid;
    private String clientenombre;
    private String clientecorreo;
    private String clientetelefono;

    // Datos extraídos - vendedor
    private Long vendedorid;

    // Datos extraídos - inmueble
    private Long inmuebleid;
    private String inmuebletitulo;
    private Double inmuebleprecio;

    // Datos extraídos - estado
    private String estado;

    // Datos extraídos - tipo visita
    private String tipovisita;
    private String tipodescripcion;
}
