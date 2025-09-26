package PTC._5.Repositores;

import PTC._5.Entities.TipoVisitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoVisitaRepository extends JpaRepository<TipoVisitaEntity, Long> {}
