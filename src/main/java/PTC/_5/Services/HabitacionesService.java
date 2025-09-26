package PTC._5.Services;

import PTC._5.Entities.HabitacionesEntity;
import PTC._5.Models.DTO.HabitacionesDTO;
import PTC._5.Models.DTO.TipoDTO;
import PTC._5.Repositores.HabitacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marca esta clase como un servicio gestionado por Spring
public class HabitacionesService
{
    @Autowired
    private HabitacionesRepository habrepo; // Repositorio para acceder a los datos de Habitaciones

    /*
     * Método para obtener todas las habitaciones de la base de datos
     * Retorna una lista de HabitacionesDTO
     */
    public List<HabitacionesDTO> obtenerTodos() {
        // Obtiene todas las entidades HabitacionesEntity desde la base de datos
        List<HabitacionesEntity> list = habrepo.findAll();

        // Convierte cada entidad a su DTO correspondiente usando un método auxiliar
        return list.stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    /*
     * Método para convertir una entidad HabitacionesEntity a HabitacionesDTO
     * Esto es útil para enviar solo los datos necesarios al cliente o capa de presentación
     */
    public HabitacionesDTO convertirDTO(HabitacionesEntity entity)
    {
        HabitacionesDTO dto = new HabitacionesDTO();

        // Asigna el ID de la habitación al DTO
        dto.setIdhabitaciones(entity.getIDHabitaciones());

        // Asigna la cantidad o descripción de habitaciones al DTO
        dto.setHabitaciones(entity.getHabitaciones());

        return dto; // Retorna el DTO listo para ser utilizado o enviado
    }
}
