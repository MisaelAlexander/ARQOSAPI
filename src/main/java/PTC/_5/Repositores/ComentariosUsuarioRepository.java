package PTC._5.Repositores;

import PTC._5.Entities.ComentariosUsuarioEntity;
import jakarta.persistence.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentariosUsuarioRepository extends JpaRepository<ComentariosUsuarioEntity, Long>
{
    Page<ComentariosUsuarioEntity> findByvendedor_IDUsuario(Long idVendedor, Pageable pageable);
    Page<ComentariosUsuarioEntity> findBycliente_IDUsuario(Long idCliente, Pageable pageable);

}

