package PTC._5.Services;

import PTC._5.Entities.ComentariosUsuarioEntity;
import PTC._5.Exceptions.ComentariosUsuarioException.ComentarioClienteNotFound;
import PTC._5.Exceptions.ComentariosUsuarioException.ComentarioVendedorNotFound;
import PTC._5.Models.DTO.ComentariosUsuarioDTO;
import PTC._5.Repositores.ComentariosUsuarioRepository;
import PTC._5.Repositores.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ComentariosUsuarioService
{

        @Autowired // Inyección automática del repositorio de comentarios entre usuarios
        private ComentariosUsuarioRepository couserrepo;

        @Autowired // Inyección automática del repositorio de usuarios
        private UsuarioRepository userrepo;

        // Obtener todos los comentarios donde el usuario sea vendedor (por su ID)
        public Page<ComentariosUsuarioDTO> obtenerPorIdVendedor(Long id, int page, int size)
        {
            if(!userrepo.existsById(id)) {
                throw new ComentarioVendedorNotFound("Los comentario del vendedor con id " + id + " no existe.");
            }
            Pageable pageable = PageRequest.of(page,size);
            Page<ComentariosUsuarioEntity> coment = couserrepo.findByvendedor_IDUsuario(id, pageable);
            return coment.map(this::convertirDTO);
        }

        // Obtener todos los comentarios donde el usuario sea cliente (por su ID)
        public Page<ComentariosUsuarioDTO> obtenerPorIdCliente(Long id, int page, int size)
        {
            if (!userrepo.existsById(id))
            {
                throw new ComentarioClienteNotFound("Los comentario del vendedor con id " + id + " no existe.");
            }
            Pageable pageable = PageRequest.of(page,size);
            Page<ComentariosUsuarioEntity> coment = couserrepo.findBycliente_IDUsuario(id, pageable);
            return coment.map(this::convertirDTO);
        }

        // Obtener todos los comentarios registrados entre usuarios
        public Page<ComentariosUsuarioDTO> obtenerTodos(int page, int size)
        {
            Pageable pageable = PageRequest.of(page, size);
            Page<ComentariosUsuarioEntity> comment = couserrepo.findAll(pageable);
            return comment.map(this::convertirDTO);
        }

        // Guardar un nuevo comentario entre usuarios
        public ComentariosUsuarioDTO guardarComentario(ComentariosUsuarioDTO dto)
        {
            ValidarCampos(dto);
            if (dto.getComentario() == null || dto.getComentario().isBlank()) {
                throw new IllegalArgumentException("El comentario no puede estar vacío");
            }
            ComentariosUsuarioEntity entity = new ComentariosUsuarioEntity();

            // Asignar los campos básicos del comentario
            entity.setComentario(dto.getComentario());
            entity.setPuntuacion(dto.getPuntuacion());
            entity.setFecha(dto.getFecha());

            // Buscar y asignar el cliente si se proporcionó un ID válido
            if (dto.getIdCliente() != null) {
                entity.setCliente(userrepo.findById(dto.getIdCliente())
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + dto.getIdCliente())));
            }

            // Buscar y asignar el vendedor si se proporcionó un ID válido
            if (dto.getIdVendedor() != null) {
                entity.setVendedor(userrepo.findById(dto.getIdVendedor())
                        .orElseThrow(() -> new RuntimeException("Vendedor no encontrado con ID: " + dto.getIdVendedor())));
            }
            try
            {
                // Guardar en base de datos y devolver DTO resultante
                ComentariosUsuarioEntity guardado = couserrepo.save(entity);
                return convertirDTO(guardado);
            }
            catch (DataIntegrityViolationException e)
            {
                throw new RuntimeException("Error de datos" + e.getMessage());
            }
        }

        // Actualizar un comentario existente por su ID
        public ComentariosUsuarioDTO actualizarComentario(Long id, ComentariosUsuarioDTO dto) {
            ValidarCampos(dto);
            // Buscar el comentario a actualizar
            ComentariosUsuarioEntity entity = couserrepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comentario no encontrado con ID: " + id));

            // Actualizar los campos del comentario
            entity.setComentario(dto.getComentario());
            entity.setPuntuacion(dto.getPuntuacion());
            entity.setFecha(dto.getFecha());

            try {
                ComentariosUsuarioEntity actualizado = couserrepo.save(entity);
                return convertirDTO(actualizado);
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Error de integridad de datos: " + e.getMessage());
            }
        }

        // Eliminar un comentario por su ID
        public void eliminarComentario(Long id) {
            ComentariosUsuarioEntity entity = couserrepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comentario no encontrado con ID: " + id));
            try {
                couserrepo.delete(entity);
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("No se pudo eliminar el comentario por restricciones de integridad: " + e.getMessage());
            }
        }

        // Convertir una entidad a DTO para facilitar la respuesta en el controlador
        private ComentariosUsuarioDTO convertirDTO(ComentariosUsuarioEntity entity) {
            ComentariosUsuarioDTO dto = new ComentariosUsuarioDTO();

            // Asignar los campos básicos
            dto.setIdComentario(entity.getIdComentario());
            dto.setComentario(entity.getComentario());
            dto.setPuntuacion(entity.getPuntuacion());
            dto.setFecha(entity.getFecha());

            // Asignar datos del cliente
            if (entity.getCliente() != null) {
                dto.setIdCliente(entity.getCliente().getIDUsuario());
                dto.setNombre(entity.getCliente().getUsuario());
            }

            // Asignar datos del vendedor y su descripción si existen
            if (entity.getVendedor() != null) {
                dto.setIdVendedor(entity.getVendedor().getIDUsuario());
                dto.setNombreVendedor(entity.getVendedor().getDescripcion().getNombre());
                dto.setApellidoVendedor(entity.getVendedor().getDescripcion().getApellido());
                dto.setCorreoVendedor(entity.getVendedor().getDescripcion().getCorreo());
                dto.setUsuarioVendedor(entity.getVendedor().getUsuario());
                dto.setDuiVendedor(entity.getVendedor().getDescripcion().getDui());
                dto.setTelefonoVendedor(entity.getVendedor().getDescripcion().getTelefono());
            }

           return dto;
       }

    private void ValidarCampos(ComentariosUsuarioDTO dto)
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
}