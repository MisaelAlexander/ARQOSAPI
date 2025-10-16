package PTC._5.Repositores;

import PTC._5.Entities.FavoritosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritosRepository extends JpaRepository<FavoritosEntity, Long>
{
    // Buscar favoritos por id de usuario
    Page<FavoritosEntity> findByUsuario_IDUsuarioAndInmueble_EstadoTrue(Long idUsuario, Pageable pageable);

}
