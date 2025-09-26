package PTC._5.Services;

import PTC._5.Entities.HistorialEntity;
import PTC._5.Models.DTO.HistorialDTO;
import PTC._5.Repositores.HistorialRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialService
{

    @Autowired
    private HistorialRepository hisrepo;

    @PersistenceContext
    private EntityManager entityManager;

    /* GET*/
    public Page<HistorialDTO> obtenerTodo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistorialEntity> lista = hisrepo.findAll(pageable);
        return lista.map(this::convertirDTO);
    }

    /* GET por idsuario*/
    public Page<HistorialDTO> obtenerPorIdUsuario(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistorialEntity> lista = hisrepo.findByusuario_IDUsuarioOrderByIdHistorialDesc(id, pageable);
        return lista.map(this::convertirDTO);
    }

    /* POST: Guardar historial */
    public HistorialDTO guardarHistorial(HistorialDTO dto) {
        if (dto.getIdUsuario() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de Usuario no puede ser nulo");
        }

        HistorialEntity entity = new HistorialEntity();

        entity.setDescripcion(dto.getDescripcion());
        entity.setFecha(dto.getFecha());

        // Relación ManyToOne con UsuarioEntity
        var usuario = entityManager.getReference(PTC._5.Entities.UsuarioEntity.class, dto.getIdUsuario());
        entity.setUsuario(usuario);

        HistorialEntity guardado = hisrepo.save(entity);

        return convertirDTO(guardado);
    }

    /* Conversión Entity a DTO */
    public HistorialDTO convertirDTO(HistorialEntity entity) {
        HistorialDTO dto = new HistorialDTO();

        dto.setIdHistorial(entity.getIdHistorial());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFecha(entity.getFecha());

        if (entity.getUsuario() != null) {
            dto.setIdUsuario(entity.getUsuario().getIDUsuario());
        } else {
            dto.setIdUsuario(null);
        }

        return dto;
    }
}
