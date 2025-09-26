package PTC._5.Repositores;

import PTC._5.Entities.FotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoRepository extends JpaRepository<FotoEntity, Long>
{

    List<FotoEntity> findByInmueble_IDInmueble (Long id);
}
