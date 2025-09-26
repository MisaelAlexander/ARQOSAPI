package PTC._5.Services;

import PTC._5.Entities.*;
import PTC._5.Models.DTO.InmuebleDTO;
import PTC._5.Repositores.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marca la clase como un servicio Spring, que contiene la lógica de negocio para Inmuebles
public class InmuebleService
{
    // Inyección de los repositorios necesarios para manejar datos
    @Autowired
    private InmuebleRepository enmurepo;
    @Autowired
    private UsuarioRepository userrepo;
    @Autowired
    private UbicacionRepository ubirepo;
    @Autowired
    private TipoRepository tiprepo;

    @PersistenceContext
    EntityManager entitymanager; // Para manejar referencias a otras entidades sin cargarlas completamente


    public Page<InmuebleDTO> buscarActivos(Pageable pageable) {
        return enmurepo.findByestadoTrue(pageable)
                .map(this::convertirDTO);
    }
    /* GET: Obtener todos los inmuebles */
    public Page<InmuebleDTO> obtenerTodo(Pageable pageable)
    {
        // Convierte cada entidad a DTO y devuelve la lista
        return enmurepo.findAll(pageable).map(this::convertirDTO);
    }

    /* Buscar inmueble por ID */
    public InmuebleDTO buscarPorId(Long id) {
        // Busca el inmueble en la base de datos
        InmuebleEntity entity = enmurepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Inmueble no encontrado con ID: " + id
                ));

        // Convierte a DTO y lo retorna
        return convertirDTO(entity);
    }
    /* POST: Guardar un nuevo inmueble */
    public InmuebleDTO guardarInmueble(InmuebleDTO dto)
    {
        // Validación básica para asegurar que los IDs foráneos no sean nulos
        if (dto.getIdtipo() == null || dto.getIdubicacion() == null || dto.getIdusuario() == null ||
                dto.getIdbanios() == null || dto.getIdhabitaciones() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los IDs de tipo, ubicación, usuario, baños y habitaciones no pueden ser nulos");
        }

        // Creación de una nueva entidad Inmueble y asignación de campos básicos
        InmuebleEntity entity = new InmuebleEntity();
        entity.setTitulo(dto.getTitulo());
        entity.setPrecio(dto.getPrecio());
        entity.setDescripcion(dto.getDescripcion());
        entity.setLatitud(dto.getLatitud());
        entity.setLongitud(dto.getLongitud());
        entity.setEstado(dto.getEstado());

        // Se establecen las relaciones con otras entidades usando referencias (sin cargar totalmente)
        entity.setTipo(entitymanager.getReference(TipoEntity.class, dto.getIdtipo()));
        entity.setUbicacion(entitymanager.getReference(UbicacionEntity.class, dto.getIdubicacion()));
        entity.setUsuario(entitymanager.getReference(UsuarioEntity.class, dto.getIdusuario()));
        entity.setBanios(entitymanager.getReference(BaniosEntity.class, dto.getIdbanios()));
        entity.setHabitaciones(entitymanager.getReference(HabitacionesEntity.class, dto.getIdhabitaciones()));

        // Guarda el inmueble en la base de datos
        InmuebleEntity guardado = enmurepo.save(entity);

        // Retorna el DTO convertido del inmueble guardado
        return convertirDTO(guardado);
    }

    /* PUT: Actualizar un inmueble existente */
    public InmuebleDTO actualizarInmueble(Long id, InmuebleDTO dto)
    {
        // Busca el inmueble por ID o lanza error si no existe
        InmuebleEntity existeentity = enmurepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Descripción no encontrada"));

        // Actualiza los campos básicos
        existeentity.setTitulo(dto.getTitulo());
        existeentity.setPrecio(dto.getPrecio());
        existeentity.setLongitud(dto.getLongitud());
        existeentity.setLatitud(dto.getLatitud());
        existeentity.setDescripcion(dto.getDescripcion());
        existeentity.setEstado(dto.getEstado());

        // Valida que todos los IDs foráneos existan (no sean nulos)
        if(dto.getIdbanios() == null || dto.getIdubicacion() == null || dto.getIdtipo() == null ||
                dto.getIdhabitaciones() == null || dto.getIdusuario() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los id no cargan");
        }

        // Asigna las relaciones con las entidades referenciadas por IDs
        TipoEntity tipo = entitymanager.getReference(TipoEntity.class, dto.getIdtipo());
        existeentity.setTipo(tipo);

        UbicacionEntity ubicacion = entitymanager.getReference(UbicacionEntity.class, dto.getIdubicacion());
        existeentity.setUbicacion(ubicacion);

        UsuarioEntity usuario = entitymanager.getReference(UsuarioEntity.class, dto.getIdusuario());
        existeentity.setUsuario(usuario);

        HabitacionesEntity habitaciones = entitymanager.getReference(HabitacionesEntity.class, dto.getIdhabitaciones());
        existeentity.setHabitaciones(habitaciones);

        BaniosEntity banios = entitymanager.getReference(BaniosEntity.class, dto.getIdbanios());
        existeentity.setBanios(banios);

        // Guarda el inmueble actualizado
        InmuebleEntity actualizado = enmurepo.save(existeentity);

        // Retorna el DTO del inmueble actualizado
        return convertirDTO(actualizado);
    }

    /* DELETE: eliminar inmueble (comentado, pero se puede activar si se desea) */
    /*
    public void eliminarInmueble(Long id)
    {
        InmuebleEntity existe = enmurepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inmueble  no encontrado"));
        enmurepo.delete(existe);
    }
    */

    /* Buscar inmuebles por ID de usuario */
    public Page<InmuebleDTO> buscarporUser(Long id, Pageable pageable)
    {
        // Verifica que el usuario exista
        if (!userrepo.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        // Busca inmuebles asociados al usuario
        return enmurepo.findByusuario_IDUsuarioAndEstadoTrue(id, pageable).map(this::convertirDTO);
    }

    /* Buscar inmuebles por ID de ubicación */
    public Page<InmuebleDTO> buscarporUbi(Long id, Pageable pageable)
    {
        if (!ubirepo.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ubicacion no encontrada");
        }
        return enmurepo.findByubicacion_IDUbicacionAndEstadoTrue(id, pageable).map(this::convertirDTO);
    }

    /* Buscar inmuebles por ID de tipo */
    public Page<InmuebleDTO> buscarporTip(Long id, Pageable pageable)
    {
        if (!tiprepo.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Tipo no encontrado");
        }

        return enmurepo.findBytipo_IDTipoAndEstadoTrue(id, pageable).map(this::convertirDTO);
    }

    /*Buscar inmueble por ID de ubicacion y tipo*/
    public Page<InmuebleDTO> buscarporUbiyTip(Long idubicacion, Long idTipo, Pageable pageable)
    {
        if (!ubirepo.existsById(idubicacion))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ubicacion no encontrada");
        }
        if (!tiprepo.existsById(idTipo))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Tipo no encontrado");
        }
        return enmurepo.findByUbicacion_IDUbicacionAndTipo_IDTipoAndEstadoTrue(idubicacion,idTipo, pageable).map(this::convertirDTO);
    }
    /* Buscar inmuebles por título que contenga texto (ignora mayúsculas/minúsculas) */
    public Page<InmuebleDTO> buscarPorTitulo(String titulo, Pageable pageable) {

        Page<InmuebleEntity> page = enmurepo.findBytituloContainingIgnoreCaseAndEstadoTrue(titulo, pageable);
        if (page.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron inmuebles con ese título");
        }

        return page.map(this::convertirDTO);
    }

    /* Buscar inmuebles por nombre de ubicación que contenga texto */
    public Page<InmuebleDTO> buscarPorUbicacion(String ubicacion, Pageable pageable) {
        return enmurepo.findByubicacion_UbicacionContainingIgnoreCaseAndEstadoTrue(ubicacion, pageable)
                .map(this::convertirDTO);
    }

    /* Buscar inmuebles por nombre de tipo que contenga texto */
    public Page<InmuebleDTO> buscarPorTipo(String tipo, Pageable pageable) {
        return enmurepo.findBytipo_tipoContainingIgnoreCaseAndEstadoTrue(tipo, pageable)
                .map(this::convertirDTO);
    }






    /*VENDEDORES*/
    /* Buscar inmuebles por ubicación y usuario */
    public Page<InmuebleDTO> buscarPorUbiYUser(Long idUbicacion, Long idUsuario, Pageable pageable) {
        if (!ubirepo.existsById(idUbicacion)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ubicación no encontrada");
        }
        if (!userrepo.existsById(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return enmurepo.findByUbicacion_IDUbicacionAndUsuario_IDUsuarioAndEstadoTrue(idUbicacion, idUsuario, pageable)
                .map(this::convertirDTO);
    }

    /* Buscar inmuebles por tipo y usuario */
    public Page<InmuebleDTO> buscarPorTipYUser(Long idTipo, Long idUsuario, Pageable pageable) {
        if (!tiprepo.existsById(idTipo)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo no encontrado");
        }
        if (!userrepo.existsById(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return enmurepo.findByTipo_IDTipoAndUsuario_IDUsuarioAndEstadoTrue(idTipo, idUsuario, pageable)
                .map(this::convertirDTO);
    }

    /* Buscar inmuebles por título y usuario */
    public Page<InmuebleDTO> buscarPorTituloYUser(String titulo, Long idUsuario, Pageable pageable) {
        if (!userrepo.existsById(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return enmurepo.findByTituloContainingIgnoreCaseAndUsuario_IDUsuarioAndEstadoTrue(titulo, idUsuario, pageable)
                .map(this::convertirDTO);
    }

    /* Buscar inmuebles por nombre de ubicación (texto) y usuario */
    public Page<InmuebleDTO> buscarPorUbicacionYUser(String ubicacion, Long idUsuario, Pageable pageable) {
        if (!userrepo.existsById(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return enmurepo.findByUbicacion_UbicacionContainingIgnoreCaseAndUsuario_IDUsuarioAndEstadoTrue(ubicacion, idUsuario, pageable)
                .map(this::convertirDTO);
    }

    /* Buscar inmuebles por nombre de tipo (texto) y usuario */
    public Page<InmuebleDTO> buscarPorTipoTextoYUser(String tipo, Long idUsuario, Pageable pageable) {
        if (!userrepo.existsById(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return enmurepo.findByTipo_TipoContainingIgnoreCaseAndUsuario_IDUsuarioAndEstadoTrue(tipo, idUsuario, pageable)
                .map(this::convertirDTO);
    }

    /* Buscar inmuebles por ubicación, tipo y usuario */
    public Page<InmuebleDTO> buscarPorUbiTipYUser(Long idUbicacion, Long idTipo, Long idUsuario, Pageable pageable) {
        if (!ubirepo.existsById(idUbicacion)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ubicación no encontrada");
        }
        if (!tiprepo.existsById(idTipo)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo no encontrado");
        }
        if (!userrepo.existsById(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return enmurepo.findByUbicacion_IDUbicacionAndTipo_IDTipoAndUsuario_IDUsuarioAndEstadoTrue(idUbicacion, idTipo, idUsuario, pageable)
                .map(this::convertirDTO);
    }

    /* Conversor: convierte una entidad InmuebleEntity a InmuebleDTO */
    public InmuebleDTO convertirDTO(InmuebleEntity entity)
    {
        InmuebleDTO dto = new InmuebleDTO();

        // Campos básicos
        dto.setIdinmuebles(entity.getIDInmueble());
        dto.setTitulo(entity.getTitulo());
        dto.setPrecio(entity.getPrecio());
        dto.setLongitud(entity.getLongitud());
        dto.setLatitud(entity.getLatitud());
        dto.setDescripcion(entity.getDescripcion());
        dto.setEstado(entity.getEstado());

        // Datos de tipo (relación)
        if(entity.getTipo() != null)
        {
            dto.setIdtipo(entity.getTipo().getIDTipo());
            dto.setTipo(entity.getTipo().getTipo());
        }
        else {
            dto.setIdtipo(null);
            dto.setTipo("Tipo de inmueble no encontrado");
        }

        // Datos de ubicación (relación)
        if(entity.getUbicacion() != null)
        {
            dto.setIdubicacion(entity.getUbicacion().getIDUbicacion());
            dto.setUbicacion(entity.getUbicacion().getUbicacion());
        }
        else {
            dto.setIdubicacion(null);
            dto.setUbicacion("ubicacion no encontrada");
        }

        // Datos del usuario propietario (relación)
        if(entity.getUsuario() != null)
        {
            dto.setIdusuario(entity.getUsuario().getIDUsuario());
            dto.setUsuarionombre(entity.getUsuario().getUsuario());

            // Si el usuario tiene descripción, toma teléfono y correo
            if (entity.getUsuario().getDescripcion() != null)
            {
                dto.setUsuariotelefono(entity.getUsuario().getDescripcion().getTelefono());
                dto.setUsuariocorreo(entity.getUsuario().getDescripcion().getCorreo());
                dto.setUsuariofoto(entity.getUsuario().getDescripcion().getFotoPerfil());
            }
            else {
                dto.setUsuariotelefono("telefono no encontrado");
                dto.setUsuariocorreo("correo no encontrado");
            }
        }
        else {
            dto.setIdusuario(null);
            dto.setUsuarionombre("nombre no encontrado");
        }

        // Datos baños (relación)
        if (entity.getBanios() != null)
        {
            dto.setIdbanios(entity.getBanios().getIDBanios());
            dto.setBanios(entity.getBanios().getBanios());
        }
        else {
            dto.setIdbanios(null);
            dto.setBanios("baños no encontrados");
        }

        // Datos habitaciones (relación)
        if (entity.getHabitaciones() != null)
        {
            dto.setIdhabitaciones(entity.getHabitaciones().getIDHabitaciones());
            dto.setHabitaciones(entity.getHabitaciones().getHabitaciones());
        }
        else {
            dto.setIdhabitaciones(null);
            dto.setHabitaciones("habitaciones no encontradas");
        }

        return dto;
    }
}
