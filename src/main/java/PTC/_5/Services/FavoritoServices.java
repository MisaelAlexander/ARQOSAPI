package PTC._5.Services;

import PTC._5.Entities.FavoritosEntity;
import PTC._5.Entities.InmuebleEntity;
import PTC._5.Entities.UsuarioEntity;
import PTC._5.Models.DTO.FavoritosDTO;
import PTC._5.Repositores.FavoritosRepository;
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

@Service // Servicio para manejo de favoritos
public class FavoritoServices
{
    @Autowired
    private FavoritosRepository favrepo;

    @PersistenceContext
    private EntityManager entityManager;

    // Obtener favoritos por ID de usuario
    public Page<FavoritosDTO> obtenerFavoritosPorUsuario(Long idUsuario, Pageable pageable)
    {
        Page<FavoritosEntity> page = favrepo.findByusuario_IDUsuario(idUsuario,pageable);
        return page.map(this::convertirDTO);
    }


    // Guardar nuevo favorito
    public FavoritosDTO guardarFavorito(FavoritosDTO dto) {
        if (dto.getIdUsuario() == null || dto.getIdInmueble() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de usuario y de inmueble no pueden ser nulos");
        }

        FavoritosEntity entity = new FavoritosEntity();
        entity.setUsuario(entityManager.getReference(UsuarioEntity.class, dto.getIdUsuario()));
        entity.setInmueble(entityManager.getReference(InmuebleEntity.class, dto.getIdInmueble()));

        FavoritosEntity guardado = favrepo.save(entity);
        return convertirDTO(guardado);
    }

    // Eliminar favorito por ID
    public void eliminarFavorito(Long idFavorito) {
        if (!favrepo.existsById(idFavorito)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorito no encontrado");
        }
        favrepo.deleteById(idFavorito);
    }

    // Convertir de entidad a DTO
    private FavoritosDTO convertirDTO(FavoritosEntity entity) {
        FavoritosDTO dto = new FavoritosDTO();
        dto.setIdFavorito(entity.getIdfavorito());

        if (entity.getUsuario() != null) {
            dto.setIdUsuario(entity.getUsuario().getIDUsuario());
        }

        if (entity.getInmueble() != null) {
            dto.setIdInmueble(entity.getInmueble().getIDInmueble());
            dto.setTitulo(entity.getInmueble().getTitulo());
            dto.setPrecio(entity.getInmueble().getPrecio());
            dto.setDescripcion(entity.getInmueble().getDescripcion());

            if (entity.getInmueble().getUbicacion() != null) {
                dto.setUbicacion(entity.getInmueble().getUbicacion().getUbicacion());
            }

            if (entity.getInmueble().getBanios() != null)
            {
                dto.setIdbanios(entity.getInmueble().getBanios().getIDBanios());
                dto.setBanios(entity.getInmueble().getBanios().getBanios());
            }

            if (entity.getInmueble().getHabitaciones() != null) {
                dto.setIdhabitaciones(entity.getInmueble().getHabitaciones().getIDHabitaciones());
                dto.setHabitaciones(entity.getInmueble().getHabitaciones().getHabitaciones());
            }
        }

        return dto;
    }
}
