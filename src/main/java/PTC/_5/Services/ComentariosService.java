package PTC._5.Services;

import PTC._5.Entities.ComentariosEntity;
import PTC._5.Entities.InmuebleEntity;
import PTC._5.Entities.UsuarioEntity;
import PTC._5.Exceptions.ComentariosException.ComentarioInmuebleNotFound;
import PTC._5.Exceptions.ComentariosException.ComentarioIntegrityViolation;
import PTC._5.Exceptions.ComentariosException.ComentarioUsuarioNotFound;
import PTC._5.Exceptions.ComentariosException.ComentariosCommonException;
import PTC._5.Models.DTO.ComentariosDTO;
import PTC._5.Repositores.ComentariosRepository;
import PTC._5.Repositores.InmuebleRepository;
import PTC._5.Repositores.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service // Indica que esta clase es un servicio de Spring
public class ComentariosService
{
    // Inyección del repositorio de comentarios
    @Autowired
    private ComentariosRepository comentarioRepo;

    // Inyección del repositorio de usuarios
    @Autowired
    private UsuarioRepository usuarioRepo;

    // Inyección del repositorio de inmuebles
    @Autowired
    private InmuebleRepository inmuebleRepo;

    // Obtener todos los comentarios (GET general)
    public Page<ComentariosDTO> obtenerTodos(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<ComentariosEntity> coment = comentarioRepo.findAll(pageable);
        return coment.map(this::convertirDTO);
    }

    // Obtener comentarios por ID de inmueble
    public Page<ComentariosDTO> obtenerPorIdInmueble(Long id, int page, int size)
    {
        if(!inmuebleRepo.existsById(id)) {
            throw new ComentarioInmuebleNotFound("Los comentario del inmueble con id " + id + " no existe.");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<ComentariosEntity> coment = comentarioRepo.findByinmueble_IDInmueble(id, pageable);
        return coment.map(this::convertirDTO);
    }



    // Obtener comentarios por ID de usuario
    public Page<ComentariosDTO> obtenerPorIdUsuario(Long id,int page, int size)
    {
        if(!usuarioRepo.existsById(id)) {
            throw new ComentarioUsuarioNotFound("Los comentario del usuario con id " + id + " no existe.");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<ComentariosEntity> coment = comentarioRepo.findByusuario_IDUsuario(id, pageable);
        return coment.map(this::convertirDTO);
    }

    // Guardar un nuevo comentario (POST)
    public ComentariosDTO guardar(ComentariosDTO dto)
    {
        ValidarCampos(dto);

            ComentariosEntity comentario = new ComentariosEntity(); // Nueva entidad
            comentario.setIdComentario(dto.getIdComentario()); // Setear ID (opcional)
            comentario.setComentario(dto.getComentario());     // Setear texto del comentario
            comentario.setPuntuacion(dto.getPuntuacion());     // Setear puntuación
            comentario.setFecha(dto.getFecha());               // Setear fecha

            // Verificar y asignar usuario
            if (dto.getIdUsuario() != null) {
                UsuarioEntity usuario = usuarioRepo.findById(dto.getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                comentario.setUsuario(usuario);
            }

            // Verificar y asignar inmueble
            if (dto.getIdInmueble() != null) {
                InmuebleEntity inmueble = inmuebleRepo.findById(dto.getIdInmueble())
                        .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));
                comentario.setInmueble(inmueble);
            }
        try
        {
            // Guardar en la base de datos
            ComentariosEntity guardado = comentarioRepo.save(comentario);
            return convertirDTO(guardado); // Retornar el DTO guardado
        }
        /*Se ejecuta cuadno se viola alguna restrccion de integridad de datos*/
        catch (DataIntegrityViolationException e)
        {
            throw new RuntimeException("Error de integridad de datos en comentarios: " + e.getMessage());
        }
    }

    // Actualizar un comentario existente (PUT)
    public ComentariosDTO actualizarComentario(Long id, ComentariosDTO dto)
    {
        ValidarCampos(dto);
        // Buscar el comentario a actualizar
        ComentariosEntity existente = comentarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado"));

        // Actualizar campos simples
        existente.setComentario(dto.getComentario());
        existente.setPuntuacion(dto.getPuntuacion());
        existente.setFecha(dto.getFecha());

        // Actualizar usuario si viene en el DTO
        if (dto.getIdUsuario() != null) {
            UsuarioEntity usuario = usuarioRepo.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            existente.setUsuario(usuario);
        }

        // Actualizar inmueble si viene en el DTO
        if (dto.getIdInmueble() != null) {
            InmuebleEntity inmueble = inmuebleRepo.findById(dto.getIdInmueble())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inmueble no encontrado"));
            existente.setInmueble(inmueble);
        }

        try
        {
            // Guardar cambios en la base de datos
            ComentariosEntity actualizado = comentarioRepo.save(existente);
            return convertirDTO(actualizado); // Retornar el DTO actualizado
        }
        catch (DataIntegrityViolationException e)
        {
            throw new RuntimeException("Error de integridad de datos: " + e.getMessage());
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error al actualizar comentario: " + e.getMessage());
        }
    }

    // Eliminar un comentario por su ID (DELETE)
    public void eliminar(Long id) {
        ComentariosEntity existente = comentarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado"));
        try
        {
            comentarioRepo.delete(existente); // Eliminar directamente
        }
        catch (DataIntegrityViolationException e)
        {
            throw new ComentarioIntegrityViolation
                    ("No se puede eliminar el dato error de integridad", e);
        }
        catch (Exception e)
        {
            throw new ComentariosCommonException(
                    "Error inesperado al borrar", e
            );
        }
    }

    //Evitar campos vacios
    private void ValidarCampos(ComentariosDTO dto)
    {
        if (dto == null)
        {
            throw new IllegalArgumentException("El comentario no puede ser nulo");
        }
        if (dto.getComentario() == null)
        {
            throw new IllegalArgumentException("El texto del comentario no puede ser nulo");
        }
        if (dto.getPuntuacion() == null)
        {
            throw new IllegalArgumentException("Se neccesita una puntuacion de uno o cinco");
        }
        if (dto.getFecha() == null)
        {
            throw new IllegalArgumentException("Se necesita capturar fecha de publicacion");
        }
    }
    // Método privado para convertir entidad a DTO
    private ComentariosDTO convertirDTO(ComentariosEntity entity) {
        ComentariosDTO dto = new ComentariosDTO();
        dto.setIdComentario(entity.getIdComentario());
        dto.setComentario(entity.getComentario());
        dto.setPuntuacion(entity.getPuntuacion());
        dto.setFecha(entity.getFecha());

        // Asignar ID de usuario si existe
        if (entity.getUsuario() != null) {
            dto.setIdUsuario(entity.getUsuario().getIDUsuario());
            dto.setNombre(entity.getUsuario().getUsuario());
        }

        // Asignar ID, título del inmueble si existe
        if (entity.getInmueble() != null) {
            dto.setIdInmueble(entity.getInmueble().getIDInmueble());
            dto.setTituloInmueble(entity.getInmueble().getTitulo());
        }

        return dto; // Retornar el DTO resultante
    }
}
