package PTC._5.Services;

import PTC._5.Entities.DescripcionEntity;
import PTC._5.Entities.RolEntity;
import PTC._5.Entities.UbicacionEntity;
import PTC._5.Exceptions.DescripcionException.DatosNoEncontradosException;
import PTC._5.Models.DTO.ComentariosDTO;
import PTC._5.Models.DTO.DescripcionDTO;
import PTC._5.Models.DTO.RolDTO;
import PTC._5.Repositores.DescripcionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marca la clase como un servicio de Spring
public class DescripcionService
{
    @Autowired
    private DescripcionRepository repodescrip; // Inyección del repositorio de descripción

    @PersistenceContext
    private EntityManager entityManager; // Se usa para manejar referencias a entidades relacionadas

    /* GET - Obtener todas las descripciones */
    public Page<DescripcionDTO> obtenerTodo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DescripcionEntity> pageEntities = repodescrip.findAll(pageable);

        if (pageEntities.isEmpty()) {
            throw new DatosNoEncontradosException("No se encontraron descripciones");
        }

        return pageEntities.map(this::convertirDTO);
    }

    /* POST - Guardar una nueva descripción */
    public DescripcionDTO guardarDescripcion(DescripcionDTO dto)
    {
        ValidarCampos(dto);
        // Validación: los campos de relación no deben ser nulos
        if (dto.getIdrol() == null || dto.getIdubicacion() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de Rol y Ubicación no pueden ser nulos");
        }

        // Crear nueva entidad y asignar los campos desde el DTO
        DescripcionEntity entity = new DescripcionEntity();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setDui(dto.getDui());
        entity.setDireccion(dto.getDireccion());
        entity.setCorreo(dto.getCorreo());
        entity.setTelefono(dto.getTelefono());
        entity.setFotoPerfil(dto.getFotoperfil());
        entity.setFechaNacimiento(dto.getFechadenacimiento());
        entity.setEstado(dto.getEstado());

        // Asociar las entidades relacionadas usando EntityManager
        RolEntity rol = entityManager.getReference(RolEntity.class, dto.getIdrol());
        entity.setRol(rol);

        UbicacionEntity ubicacion = entityManager.getReference(UbicacionEntity.class, dto.getIdubicacion());
        entity.setUbicacion(ubicacion);

        // Guardar la entidad en la base de datos
        DescripcionEntity guardado = repodescrip.save(entity);

        // Retornar la entidad guardada como DTO
        return convertirDTO(guardado);
    }

    /* PUT - Actualizar una descripción existente */
    public DescripcionDTO actualizarDescripcion(Long id, DescripcionDTO dto)
    {
        // Buscar la entidad existente
        DescripcionEntity existeentity = repodescrip.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Descripción no encontrada"));

        ValidarCampos(dto);

        // Actualizar los campos
        existeentity.setNombre(dto.getNombre());
        existeentity.setApellido(dto.getApellido());
        existeentity.setDui(dto.getDui());
        existeentity.setDireccion(dto.getDireccion());
        existeentity.setCorreo(dto.getCorreo());
        existeentity.setTelefono(dto.getTelefono());
        existeentity.setFotoPerfil(dto.getFotoperfil());
        existeentity.setFechaNacimiento(dto.getFechadenacimiento());
        if(dto.getEstado() != null && !dto.getEstado().describeConstable().isEmpty()){
            existeentity.setEstado(dto.getEstado()); // solo actualiza si se envía
        }

        // Validar las relaciones
        if (dto.getIdrol() == null || dto.getIdubicacion() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol y Ubicación no pueden ser nulos");
        }

        // Actualizar relaciones con referencias
        RolEntity rol = entityManager.getReference(RolEntity.class, dto.getIdrol());
        existeentity.setRol(rol);

        UbicacionEntity ubicacion = entityManager.getReference(UbicacionEntity.class, dto.getIdubicacion());
        existeentity.setUbicacion(ubicacion);

        // Guardar cambios
        DescripcionEntity actualizado = repodescrip.save(existeentity);

        // Retornar la entidad actualizada como DTO
        return convertirDTO(actualizado);
    }

    /* DELETE - Eliminar una descripción por ID */
    public void eliminarDescripcion(Long id) {
        // Buscar la entidad
        DescripcionEntity existe = repodescrip.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Descripcion no encontrada"));

        // Eliminar de la base de datos
        repodescrip.delete(existe);
    }

    /* Método utilizado por otros servicios para buscar entidad por ID */
    public DescripcionEntity buscariddedesc(Long id) {
        return repodescrip.findById(id)
                .orElseThrow(() -> new RuntimeException("Descripcion no encontrada" + id));
    }


    //Evitar campos vacios
    private void ValidarCampos(DescripcionDTO dto)
    {
        if (dto == null)
        {
            throw new IllegalArgumentException("La descripcion no puede ser nuls");
        }
        if (dto.getNombre() == null)
        {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        if (dto.getApellido() == null)
        {
            throw new IllegalArgumentException("El apellido no puede ser nulo");
        }
        if (dto.getDui() == null)
        {
            throw new IllegalArgumentException("El DUI es obligatorio");
        }
        if (dto.getDireccion() == null)
        {
            throw new IllegalArgumentException("Es obligatoria una direccion");
        }
        if (dto.getCorreo() == null)
        {
            throw new IllegalArgumentException("El correo no puede ser nulo");
        }
        if (dto.getTelefono() == null)
        {
            throw new IllegalArgumentException("El telefono no puede ser nulo");
        }
        if (dto.getFotoperfil() == null)
        {
            throw new IllegalArgumentException("Una foto es necesaria");
        }
        if (dto.getFechadenacimiento() == null)
        {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }
        if (dto.getEstado() == null)
        {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
    }


    /* Conversión de una entidad a un DTO */
    public DescripcionDTO convertirDTO(DescripcionEntity entity) {
        DescripcionDTO dto = new DescripcionDTO();

        // Mapear campos simples
        dto.setIddescripcion(entity.getIDDescripcion());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setDui(entity.getDui());
        dto.setDireccion(entity.getDireccion());
        dto.setCorreo(entity.getCorreo());
        dto.setTelefono(entity.getTelefono());
        dto.setFotoperfil(entity.getFotoPerfil());
        dto.setFechadenacimiento(entity.getFechaNacimiento());
        dto.setEstado(entity.getEstado());

        // Mapear relación con ubicación
        if (entity.getUbicacion() != null) {
            dto.setIdubicacion(entity.getUbicacion().getIDUbicacion());
            dto.setUbicacion(entity.getUbicacion().getUbicacion());
        } else {
            dto.setIdubicacion(null);
            dto.setUbicacion("Sin ubicación asignada");
        }

        // Mapear relación con rol
        if (entity.getRol() != null) {
            dto.setIdrol(entity.getRol().getIDRol());
            dto.setRol(entity.getRol().getRol());
        } else {
            dto.setIdrol(null);
            dto.setRol("Sin rol asignado");
        }

        return dto;
    }

    /*Metodos para validaciones*/
    public boolean existeDui(String dui) {
        return repodescrip.existsBydui(dui);
    }

    public boolean existeCorreo(String correo) {
        return repodescrip.existsBycorreo(correo);
    }

    public boolean existeTelefono(String telefono) {
        return repodescrip.existsBytelefono(telefono);
    }
}


