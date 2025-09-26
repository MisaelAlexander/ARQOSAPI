package PTC._5.Services;

import PTC._5.Entities.InmuebleEntity;
import PTC._5.Entities.NotificacionesEntity;
import PTC._5.Models.DTO.NotificacionesDTO;
import PTC._5.Repositores.NotificacionesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marca esta clase como servicio de Spring que maneja la lógica para Notificaciones
public class NotificacionesService
{
    @Autowired
    private NotificacionesRepository reponoti; // Repositorio para acceder a la tabla de notificaciones

    @PersistenceContext
    private EntityManager entityManager; // Permite manejar referencias a entidades relacionadas

    /* GET: obtener todas las notificaciones */
    public List<NotificacionesDTO> obtenerTodo() {
        // Obtiene todas las notificaciones de la base de datos
        List<NotificacionesEntity> lista = reponoti.findAll();

        // Convierte cada entidad en un DTO para enviar al cliente
        return lista.stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    /* GET: obtener notificaciones por ID de usuario */
    public List<NotificacionesDTO> obtenerPorIdUsuario(Long idusuario) {
        // Busca notificaciones que correspondan al usuario con el ID dado
        List<NotificacionesEntity> lista = reponoti.findByusuario_IDUsuario(idusuario);

        // Convierte a DTOs y retorna la lista
        return lista.stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    /* POST: Guardar nueva notificación */
    public NotificacionesDTO guardarNotificacion(NotificacionesDTO dto) {
        // Valida que el ID de usuario no sea nulo (es obligatorio)
        if (dto.getIdusuario() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de Usuario no puede ser nulo");
        }

        // Crea una nueva entidad de notificación y asigna la descripción
        NotificacionesEntity entity = new NotificacionesEntity();
        entity.setDescripcion(dto.getDescripcion());

        // Asocia la notificación al usuario (referencia rápida sin cargar toda la entidad)
        var usuario = entityManager.getReference(PTC._5.Entities.UsuarioEntity.class, dto.getIdusuario());
        entity.setUsuario(usuario);

        // Guarda la notificación en la base de datos
        NotificacionesEntity guardado = reponoti.save(entity);

        // Retorna la notificación guardada convertida a DTO
        return convertirDTO(guardado);
    }


    public void eliminarNotificacion(Long id)
    {
        NotificacionesEntity existe = reponoti.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inmueble  no encontrado"));
        reponoti.delete(existe);
    }

    /* Conversión de entidad NotificacionesEntity a NotificacionesDTO */
    public NotificacionesDTO convertirDTO(NotificacionesEntity entity) {
        NotificacionesDTO dto = new NotificacionesDTO();

        dto.setIdnotificacion(entity.getIDNotificacion());  // ID de la notificación
        dto.setDescripcion(entity.getDescripcion());         // Descripción de la notificación

        // Si la notificación tiene un usuario relacionado, asigna su ID
        if (entity.getUsuario() != null) {
            dto.setIdusuario(entity.getUsuario().getIDUsuario());
        } else {
            dto.setIdusuario(null);
        }

        return dto; // Retorna el DTO listo para usar en el frontend
    }
}
