package PTC._5.Services;

import PTC._5.Entities.UbicacionEntity;
import PTC._5.Models.DTO.UbicacionDTO;
import PTC._5.Repositores.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marca esta clase como un servicio de Spring que maneja la lógica de negocio para Ubicaciones
public class UbicacionServices
{
    @Autowired
    private UbicacionRepository ubirepo; // Inyecta el repositorio para acceder a la tabla de Ubicaciones

    // Método para obtener todas las ubicaciones registradas en la base de datos
    public List<UbicacionDTO> obtenertodos()
    {
        // Obtiene todas las entidades UbicacionEntity
        List<UbicacionEntity> lista = ubirepo.findAll();

        // lanzar excepción si está vacío, para control en Controller
        if (lista.isEmpty())
        {
            throw new RuntimeException("No se encontraron ubicaciones");
        }

        // Convierte cada entidad en un DTO para enviar datos simples al frontend
        return lista.stream()
                .map(this::convertirDTO) // usa el método convertirDTO para la conversión
                .collect(Collectors.toList());
    }

    // Método privado para convertir una entidad UbicacionEntity en un DTO UbicacionDTO
    private UbicacionDTO convertirDTO(UbicacionEntity entity)
    {
        UbicacionDTO dto = new UbicacionDTO();

        dto.setIdubicacion(entity.getIDUbicacion()); // asigna el ID de la ubicación
        dto.setUbicacion(entity.getUbicacion());     // asigna el nombre o descripción de la ubicación

        return dto; // retorna el DTO con datos simplificados para la capa de presentación
    }
}
