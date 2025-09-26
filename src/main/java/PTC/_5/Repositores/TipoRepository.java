package PTC._5.Repositores;

import PTC._5.Entities.TipoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoRepository extends JpaRepository<TipoEntity,Long> {}
