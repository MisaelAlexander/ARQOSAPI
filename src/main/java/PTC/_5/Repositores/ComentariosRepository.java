package PTC._5.Repositores;

import PTC._5.Entities.ComentariosEntity;
import PTC._5.Entities.InmuebleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ComentariosRepository extends JpaRepository<ComentariosEntity, Long>
{

    Page<ComentariosEntity> findAll(Pageable pageable);

    Page<ComentariosEntity> findByinmueble_IDInmueble(Long idInmueble, Pageable pageable);

    Page<ComentariosEntity> findByusuario_IDUsuario(Long idUsuario, Pageable pageable);
}
