package PTC._5.Services;

import PTC._5.Entities.FotoEntity;
import PTC._5.Entities.InmuebleEntity;
import PTC._5.Entities.RolEntity;
import PTC._5.Models.DTO.FotoDTO;
import PTC._5.Repositores.FotoRepository;
import PTC._5.Repositores.InmuebleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marca la clase como un servicio gestionado por Spring
public class FotoServices
{
    @Autowired
    private FotoRepository fotorepo; // Repositorio para acceder a los datos de Fotos

    @Autowired
    private InmuebleRepository inmurepo; // Repositorio para Inmuebles, usado para validar existencia

    @PersistenceContext
    private EntityManager entityManager; // Para manejar referencias a entidades sin cargarlas completamente

    /*
     * Método GET: Obtener todas las fotos de la base de datos
     * Devuelve una lista de FotoDTO
     */
    public List<FotoDTO> obtenerTodo() {
        List<FotoEntity> lista = fotorepo.findAll();

        // Convierte la lista de entidades a DTOs para enviarlos
        return lista.stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    /*
     * Método GET: Buscar fotos por ID de inmueble
     * Valida que el inmueble exista, si no lanza error 404
     * Luego busca y devuelve todas las fotos asociadas a ese inmueble
     */
    public List<FotoDTO> buscarPorIdInmueble(Long id) {
        if (!inmurepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inmueble no encontrado");
        }
        List<FotoEntity> lista = fotorepo.findByInmueble_IDInmueble(id);
        return lista.stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    /*
     * Método POST: Guardar una nueva foto
     * Valida que el ID de inmueble no sea nulo
     * Luego crea la entidad FotoEntity, asigna la foto y la relación al inmueble
     * Guarda la entidad en la base de datos y retorna el DTO resultante
     */
    public FotoDTO guardarFoto(FotoDTO dto) {
        if (dto.getIdInmueble() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del inmueble no puede ser nulo");
        }

        FotoEntity entity = new FotoEntity();
        entity.setFotos(dto.getFoto());

        // Obtiene una referencia al inmueble sin cargar toda la entidad para eficiencia
        InmuebleEntity inmueble = entityManager.getReference(InmuebleEntity.class, dto.getIdInmueble());
        entity.setInmueble(inmueble);

        FotoEntity guardado = fotorepo.save(entity);
        return convertirDTO(guardado);
    }

    /*
     * Método DELETE: Eliminar una foto por su ID
     * Primero verifica que exista la foto, si no lanza error 404
     * Luego la elimina
     */
    public void eliminarFoto(Long id) {
        FotoEntity existe = fotorepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto no encontrada"));
        fotorepo.delete(existe);
    }

    /*
     * Método auxiliar para convertir una entidad FotoEntity a FotoDTO
     * Esto facilita enviar sólo los datos necesarios a la capa superior
     */
    public FotoDTO convertirDTO(FotoEntity entity)
    {
        FotoDTO dto = new FotoDTO();
        dto.setIdFoto(entity.getIDFoto());  // Asignar ID de la foto
        dto.setFoto(entity.getFotos());     // Asignar la foto (puede ser ruta, URL o bytes)

        // Asignar el ID del inmueble relacionado si existe
        if (entity.getInmueble() != null)
        {
            dto.setIdInmueble(entity.getInmueble().getIDInmueble());
        }
        else
        {
            dto.setIdInmueble(null);
        }
        return dto;
    }
}
