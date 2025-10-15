package PTC._5.Repositores;

import PTC._5.Entities.InmuebleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InmuebleRepository extends JpaRepository<InmuebleEntity, Long>
{
    Page<InmuebleEntity> findByusuario_IDUsuarioAndEstadoTrue(Long id, Pageable pageable);

    Page<InmuebleEntity> findByubicacion_IDUbicacionAndEstadoTrue(Long id, Pageable pageable);

    Page<InmuebleEntity> findBytipo_IDTipoAndEstadoTrue(Long id, Pageable pageable);

    Page<InmuebleEntity> findBytituloContainingIgnoreCaseAndEstadoTrue(String titulo, Pageable pageable);

    Page<InmuebleEntity> findByubicacion_UbicacionContainingIgnoreCaseAndEstadoTrue(String ubicacion, Pageable pageable);

    Page<InmuebleEntity> findBytipo_tipoContainingIgnoreCaseAndEstadoTrue(String tipo, Pageable pageable);

    Page<InmuebleEntity> findByUbicacion_IDUbicacionAndTipo_IDTipoAndEstadoTrue(Long idUbicacion, Long idTipo, Pageable pageable);

    Page<InmuebleEntity> findByestadoTrue(Pageable pageable);

    /*para busqueda de vndedores*/
    // Buscar por ubicación y usuario
    Page<InmuebleEntity> findByUbicacion_IDUbicacionAndUsuario_IDUsuarioAndEstadoTrue(
            Long idUbicacion, Long idUsuario, Pageable pageable);
    
    // Buscar por tipo y usuario
    Page<InmuebleEntity> findByTipo_IDTipoAndUsuario_IDUsuarioAndEstadoTrue(
            Long idTipo, Long idUsuario, Pageable pageable);

    // Buscar por título y usuario
    Page<InmuebleEntity> findByTituloContainingIgnoreCaseAndUsuario_IDUsuarioAndEstadoTrue(
            String titulo, Long idUsuario, Pageable pageable);

    // Buscar por nombre de ubicación y usuario
    Page<InmuebleEntity> findByUbicacion_UbicacionContainingIgnoreCaseAndUsuario_IDUsuarioAndEstadoTrue(
            String ubicacion, Long idUsuario, Pageable pageable);

    // Buscar por nombre de tipo y usuario
    Page<InmuebleEntity> findByTipo_TipoContainingIgnoreCaseAndUsuario_IDUsuarioAndEstadoTrue(
            String tipo, Long idUsuario, Pageable pageable);

    // Buscar por ubicación, tipo y usuario
    Page<InmuebleEntity> findByUbicacion_IDUbicacionAndTipo_IDTipoAndUsuario_IDUsuarioAndEstadoTrue(
            Long idUbicacion, Long idTipo, Long idUsuario, Pageable pageable);

}

