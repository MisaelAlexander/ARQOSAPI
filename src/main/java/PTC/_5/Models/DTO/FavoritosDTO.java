package PTC._5.Models.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoritosDTO
{
    private Long idFavorito;

    // Solo ID de usuario
    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long idUsuario;

    // Inmueble (datos seleccionados)
    @NotNull(message = "El ID del inmueble no puede ser nulo")
    private Long idInmueble;
    private String titulo;
    private Double precio;
    private String ubicacion;
    private String descripcion;
    private Long   idbanios;
    private String banios;
    private Long idhabitaciones;
    private String habitaciones;
}
