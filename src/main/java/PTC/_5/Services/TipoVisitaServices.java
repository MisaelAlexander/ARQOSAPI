package PTC._5.Services;

import PTC._5.Entities.TipoVisitaEntity;
import PTC._5.Models.DTO.TipoVisitaDTO;
import PTC._5.Repositores.TipoVisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marca esta clase como un servicio de Spring para la lógica relacionada con TipoVisita
public class TipoVisitaServices
{
    @Autowired
    private TipoVisitaRepository tvrepo; // Inyecta el repositorio para acceder a la tabla TipoVisita

    // Método para obtener todos los tipos de visita registrados en la base de datos
    public List<TipoVisitaDTO> obtenerTodos() {
        // Busca todas las entidades TipoVisitaEntity, las convierte a DTO y las retorna como lista
        return tvrepo.findAll().stream()
                .map(this::convertirADTO) // Convierte cada entidad a un DTO
                .collect(Collectors.toList());
    }

    // Método privado que convierte una entidad TipoVisitaEntity a TipoVisitaDTO
    private TipoVisitaDTO convertirADTO(TipoVisitaEntity entity) {
        TipoVisitaDTO dto = new TipoVisitaDTO();

        dto.setIdtipoVisita(entity.getIdTipoVisita()); // Asigna el ID del tipo de visita
        dto.setTipoVisita(entity.getTipoVisita());     // Asigna el nombre o tipo de la visita
        dto.setDescripcion(entity.getDescripcion());   // Asigna la descripción del tipo de visita

        return dto; // Retorna el DTO listo para ser enviado al cliente
    }
}
