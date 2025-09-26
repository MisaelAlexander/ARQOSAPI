package PTC._5.Repositores;

import PTC._5.Entities.HabitacionesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitacionesRepository extends JpaRepository<HabitacionesEntity, Long> {
}
