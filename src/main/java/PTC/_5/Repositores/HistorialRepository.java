    package PTC._5.Repositores;

    import PTC._5.Entities.HistorialEntity;
    import PTC._5.Entities.InmuebleEntity;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface HistorialRepository extends JpaRepository<HistorialEntity,Long>
    {
        //Para que busque por uusrio y lo ordene de la fecha mas reciente al mas antiguo
        Page<HistorialEntity> findByusuario_IDUsuarioOrderByIdHistorialDesc(Long idUsuario, Pageable pageable);
    }
