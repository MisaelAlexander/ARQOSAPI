package PTC._5.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoDTO
{
    private Long IdFoto;

    @NotBlank(message = "La foto no puede estar vacia")
    @Size(max = 500, message = "La url de la fot a de tener maximo 500 caracteres")
    private String Foto;

    @NotNull(message = "El ID del inmueble es obligatorio")
    private Long idInmueble;
}
