package PTC._5.Repositores;

import PTC._5.Entities.UbicacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<UbicacionEntity, Long> {}
