package PTC._5.Repositores;

import PTC._5.Entities.DescripcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescripcionRepository extends JpaRepository<DescripcionEntity, Long>
{
    boolean existsBycorreo(String correo);   // Si UsuarioEntity tiene: private DescripcionEntity descripcion;
    boolean existsBytelefono(String telefono);
    boolean existsBydui(String dui);
}
