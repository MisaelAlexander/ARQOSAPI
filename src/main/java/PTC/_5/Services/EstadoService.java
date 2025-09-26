package PTC._5.Services;

import PTC._5.Entities.EstadoEntity;
import PTC._5.Models.DTO.EstadoDTO;
import PTC._5.Repositores.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Anotación que indica que esta clase es un servicio de Spring
public class EstadoService
{
    // Inyección del repositorio de estado para acceder a la base de datos
    @Autowired
    EstadoRepository estarepo;

    // Método que retorna una lista de todos los estados en forma de DTO
    public List<EstadoDTO> obtenerTodos() {
        // Busca todos los registros en la tabla Estado, los convierte a DTO y los devuelve como lista
        return estarepo.findAll().stream()
                .map(this::convertirADTO) // Convierte cada entidad a DTO
                .collect(Collectors.toList()); // Recolecta los DTO en una lista
    }

    // Método privado que convierte una entidad EstadoEntity a un DTO EstadoDTO
    private EstadoDTO convertirADTO(EstadoEntity entity) {
        EstadoDTO dto = new EstadoDTO();               // Crea una nueva instancia del DTO
        dto.setIdEstado(entity.getIdEstado());         // Asigna el ID del estado
        dto.setEstado(entity.getEstado());             // Asigna el nombre del estado
        return dto;                                     // Retorna el DTO construido
    }
}
