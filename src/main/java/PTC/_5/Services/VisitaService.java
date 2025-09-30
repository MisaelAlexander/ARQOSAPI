package PTC._5.Services;

import PTC._5.Entities.*;
import PTC._5.Models.DTO.VisitaDTO;
import PTC._5.Repositores.VisitaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service // Indica que esta clase es un servicio (lógica de negocio)
public class VisitaService
{
    @Autowired
    private VisitaRepository visrepo; // Inyecta el repositorio para acceder a la base de datos

    @PersistenceContext
    EntityManager entitymanager; // Permite obtener referencias de entidades relacionadas por ID

    // ======================
    // MÉTODO GET - Obtener todas las visitas
    // ======================
    public Page<VisitaDTO> obtenerVisitas(Pageable pageable) {
        return visrepo.findAll(pageable).map(this::convertirDTO);
    }

    // Buscar visitas por estado y cliente
    public Page<VisitaDTO> obtenerVisitasPorEstadoYCliente(Long idEstado, Long idCliente, Pageable pageable) {
        return visrepo.findByEstado_IdEstadoAndCliente_IDUsuario(idEstado, idCliente, pageable)
                .map(this::convertirDTO);
    }

    // Buscar visitas por estado y vendedor
    public Page<VisitaDTO> obtenerVisitasPorEstadoYVendedor(Long idEstado, Long idVendedor, Pageable pageable) {
        return visrepo.findByEstado_IdEstadoAndVendedor_IDUsuario(idEstado, idVendedor, pageable)
                .map(this::convertirDTO);
    }


    // ======================
    // MÉTODO POST - Guardar nueva visita
    // ======================
    public VisitaDTO guardarVisita(VisitaDTO dto)
    {
        // Valida que los IDs importantes no vengan nulos
        if (dto.getIdcliente() == null || dto.getIdvendedor() == null || dto.getIdinmueble() == null ||
                dto.getIdestado() == null || dto.getIdtipovisita() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los IDs de cliente, vendedor, inmueble, estado y tipo de visita no pueden ser nulos");
        }

        // Crea una nueva entidad con los datos recibidos
        VisitaEntity entity = new VisitaEntity();
        entity.setFecha(dto.getFecha());
        entity.setHora(dto.getHora());
        entity.setDescripcion(dto.getDescripcion());

        // Relaciona con otras entidades usando EntityManager
        entity.setCliente(entitymanager.getReference(UsuarioEntity.class, dto.getIdcliente()));
        entity.setVendedor(entitymanager.getReference(UsuarioEntity.class, dto.getIdvendedor()));
        entity.setInmueble(entitymanager.getReference(InmuebleEntity.class, dto.getIdinmueble()));
        entity.setEstado(entitymanager.getReference(EstadoEntity.class, dto.getIdestado()));
        entity.setTipoVisita(entitymanager.getReference(TipoVisitaEntity.class, dto.getIdtipovisita()));

        // Guarda la visita en la base de datos
        VisitaEntity guardado = visrepo.save(entity);

        return convertirDTO(guardado); // Devuelve el DTO de la visita guardada
    }

    // ======================
    // MÉTODO PUT - Actualizar visita existente
    // ======================
    public VisitaDTO actualizarVisita(Long id, VisitaDTO dto)
    {
        // Verifica si la visita existe en la base de datos
        VisitaEntity existeentity = visrepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Visita no encontrada"));

        // Actualiza campos simples
        existeentity.setFecha(dto.getFecha());
        existeentity.setHora(dto.getHora());
        existeentity.setDescripcion(dto.getDescripcion());

        // Verifica que los IDs foráneos no vengan vacíos
        if (dto.getIdcliente() == null || dto.getIdvendedor() == null || dto.getIdinmueble() == null ||
                dto.getIdestado() == null || dto.getIdtipovisita() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los IDs no pueden ser nulos");
        }

        // Asocia las nuevas relaciones usando EntityManager
        existeentity.setCliente(entitymanager.getReference(UsuarioEntity.class, dto.getIdcliente()));
        existeentity.setVendedor(entitymanager.getReference(UsuarioEntity.class, dto.getIdvendedor()));
        existeentity.setInmueble(entitymanager.getReference(InmuebleEntity.class, dto.getIdinmueble()));
        existeentity.setEstado(entitymanager.getReference(EstadoEntity.class, dto.getIdestado()));
        existeentity.setTipoVisita(entitymanager.getReference(TipoVisitaEntity.class, dto.getIdtipovisita()));

        // Guarda los cambios
        VisitaEntity actualizado = visrepo.save(existeentity);

        return convertirDTO(actualizado); // Retorna el DTO actualizado
    }

    // ======================
    // Buscar visitas por ID del cliente
    // ======================
    public Page<VisitaDTO> obtenerVisitasPorCliente(Long idUsuario, Pageable pageable) {
        return visrepo.findBycliente_IDUsuario(idUsuario, pageable).map(this::convertirDTO);
    }
    // ======================
    // Buscar visitas por ID del vendedor
    // ======================
    public Page<VisitaDTO> obtenerVisitasPorVendedor(Long idUsuario, Pageable pageable) {
        return visrepo.findByvendedor_IDUsuario(idUsuario, pageable).map(this::convertirDTO);
    }
    // ======================
// Buscar visitas por título del inmueble y cliente
// ======================
    public Page<VisitaDTO> obtenerVisitasPorTituloInmuebleYCliente(String titulo, Long idUsuario, Pageable pageable) {
        return visrepo
                .findByinmueble_tituloContainingIgnoreCaseAndCliente_IDUsuario(titulo, idUsuario, pageable)
                .map(this::convertirDTO);
    }

    // ======================
// Buscar visitas por título del inmueble y cliente
// ======================
    public Page<VisitaDTO> obtenerVisitasPorTituloInmuebleYVendedor(String titulo, Long idUsuario, Pageable pageable) {
        return visrepo
                .findByinmueble_tituloContainingIgnoreCaseAndVendedor_IDUsuario(titulo, idUsuario, pageable)
                .map(this::convertirDTO);
    }

    // ======================
    // Buscar visitas por ID del inmueble
    // ======================
    public Page<VisitaDTO> obtenerVisitasPorInmueble(Long idInmueble, Pageable pageable) {
        return  visrepo.findByinmueble_IDInmueble(idInmueble,  pageable)
                .map(this::convertirDTO);
    }

    // ======================
    // Cambiar a estado negativo si ya paso la fecha y es en espera
    // ======================

    public void actualizarVisitasVencidas() {
        // Fecha actual (solo día, sin hora)
        LocalDate hoy = LocalDate.now();
        Date fechaActual = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Buscar visitas con estado = 3(En proceso) y fecha <= hoy
        List<VisitaEntity> visitas = visrepo.findByEstado_IdEstadoAndFechaLessThanEqual(3L, fechaActual);

        for (VisitaEntity v : visitas) {
            // Convertir java.util.Date → LocalDate
            LocalDate fechaVisita = ((java.sql.Date) v.getFecha()).toLocalDate();

            // Recuperar hora o usar medianoche si está null
            LocalTime horaVisita = v.getHora() != null ? v.getHora() : LocalTime.MIDNIGHT;

            // Combinar fecha y hora en LocalDateTime
            LocalDateTime fechaHoraVisita = LocalDateTime.of(fechaVisita, horaVisita);

            // Comparar con el momento actual
            if (fechaHoraVisita.isBefore(LocalDateTime.now())) {
                // Cambiar estado de 3 a 2
                //l numerito lelva L para simbolziar que es tipo Long
                v.setEstado(entitymanager.getReference(EstadoEntity.class, 2L));
                visrepo.save(v);
            }
        }
    }
    // ======================
    // Convertir entidad Visita a DTO
    // ======================
    public VisitaDTO convertirDTO(VisitaEntity entity) {
        VisitaDTO dto = new VisitaDTO();

        // Datos básicos de la visita
        dto.setIdvisita(entity.getIdvisita());
        dto.setFecha(entity.getFecha());
        dto.setHora(entity.getHora());
        dto.setDescripcion(entity.getDescripcion());

        // Datos del estado
        if (entity.getEstado() != null) {
            dto.setIdestado(entity.getEstado().getIdEstado());
            dto.setEstado(entity.getEstado().getEstado());
        } else {
            dto.setIdestado(null);
            dto.setEstado("Estado no encontrado");
        }

        // Datos del tipo de visita
        if (entity.getTipoVisita() != null) {
            dto.setIdtipovisita(entity.getTipoVisita().getIdTipoVisita());
            dto.setTipovisita(entity.getTipoVisita().getTipoVisita());
            dto.setTipodescripcion(entity.getTipoVisita().getDescripcion());
        } else {
            dto.setIdtipovisita(null);
            dto.setTipovisita("Tipo de visita no encontrado");
            dto.setTipodescripcion("Descripción no encontrada");
        }

        // Datos del inmueble
        if (entity.getInmueble() != null) {
            dto.setIdinmueble(entity.getInmueble().getIDInmueble());
            dto.setInmuebleid(entity.getInmueble().getIDInmueble());
            dto.setInmuebletitulo(entity.getInmueble().getTitulo());
            dto.setInmuebleprecio(entity.getInmueble().getPrecio());
        } else {
            dto.setIdinmueble(null);
            dto.setInmuebleid(null);
            dto.setInmuebletitulo("Inmueble no encontrado");
            dto.setInmuebleprecio(null);
        }

        // Datos del cliente
        if (entity.getCliente() != null) {
            dto.setIdcliente(entity.getCliente().getIDUsuario());
            dto.setClienteid(entity.getCliente().getIDUsuario());

            if (entity.getCliente().getDescripcion() != null) {
                dto.setClientenombre(entity.getCliente().getDescripcion().getNombre());
                dto.setClientecorreo(entity.getCliente().getDescripcion().getCorreo());
                dto.setClientetelefono(entity.getCliente().getDescripcion().getTelefono());
            } else {
                dto.setClientenombre("Nombre no encontrado");
                dto.setClientecorreo("Correo no encontrado");
                dto.setClientetelefono("Teléfono no encontrado");
            }
        } else {
            dto.setIdcliente(null);
            dto.setClienteid(null);
            dto.setClientenombre("Cliente no encontrado");
            dto.setClientecorreo("Correo no encontrado");
            dto.setClientetelefono("Teléfono no encontrado");
        }

        // Datos del vendedor (no incluye descripción)
        if (entity.getVendedor() != null) {
            dto.setIdvendedor(entity.getVendedor().getIDUsuario());
            dto.setVendedorid(entity.getVendedor().getIDUsuario());
        } else {
            dto.setIdvendedor(null);
            dto.setVendedorid(null);
        }

        return dto; // Retorna el DTO ya armado con toda la info necesaria
    }
}
