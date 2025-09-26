package PTC._5.Services;

import PTC._5.Entities.TipoEntity;
import PTC._5.Models.DTO.TipoDTO;
import PTC._5.Repositores.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Indica que esta clase es un servicio de Spring para manejar la lógica de Tipo
public class TipoServices
{
    @Autowired
    private TipoRepository tipRepo; // Inyecta el repositorio para acceder a los datos de Tipo

    // Método para obtener todos los registros de Tipo de la base de datos
    public List<TipoDTO> obtenerTodos()
    {
        List<TipoEntity> list = tipRepo.findAll(); // Obtiene todas las entidades TipoEntity

        // Convierte cada entidad a DTO y lo devuelve como lista
        return list.stream()
                .map(this::convertirDTO) // Utiliza el método convertirDTO para cada elemento
                .collect(Collectors.toList());
    }

    // Método privado que convierte una entidad TipoEntity a un DTO TipoDTO
    private TipoDTO convertirDTO(TipoEntity entity)
    {
        TipoDTO dto = new TipoDTO();
        dto.setIdtipo(entity.getIDTipo()); // Asigna el ID del tipo
        dto.setTipo(entity.getTipo());     // Asigna el nombre o descripción del tipo
        return dto; // Retorna el DTO preparado para enviar a la capa presentación
    }
}
