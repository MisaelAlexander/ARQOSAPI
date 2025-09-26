package PTC._5.Repositores;

import PTC._5.Entities.HistorialEntity;
import PTC._5.Entities.NotificacionesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionesRepository extends JpaRepository<NotificacionesEntity,Long>
{
    List<NotificacionesEntity> findByusuario_IDUsuario(Long idUsuario);
}
