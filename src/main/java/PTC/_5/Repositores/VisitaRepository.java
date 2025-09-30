package PTC._5.Repositores;

import PTC._5.Entities.VisitaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<VisitaEntity, Long>
{
    Page<VisitaEntity> findBycliente_IDUsuario(Long idUsuario, Pageable pageable);

    Page<VisitaEntity> findByvendedor_IDUsuario(Long idUsuario, Pageable pageable);

    Page<VisitaEntity> findByinmueble_tituloContainingIgnoreCaseAndCliente_IDUsuario(String titulo, Long idUsuario, Pageable pageable);

    Page<VisitaEntity> findByinmueble_tituloContainingIgnoreCaseAndVendedor_IDUsuario(String titulo, Long idVendedor, Pageable pageable);

    List<VisitaEntity> findByinmueble_IDInmueble(Long idInmueble);

    // Buscar visitas por estado y cliente
    Page<VisitaEntity> findByEstado_IdEstadoAndCliente_IDUsuario(Long idEstado, Long idCliente, Pageable pageable);

    // Buscar visitas por estado y vendedor
    Page<VisitaEntity> findByEstado_IdEstadoAndVendedor_IDUsuario(Long idEstado, Long idVendedor, Pageable pageable);

    // Buscar todas las visitas con un estado específico y fecha <= a la fecha actual
    List<VisitaEntity> findByEstado_IdEstadoAndFechaLessThanEqual(Long idEstado, Date fechaActual);
}
