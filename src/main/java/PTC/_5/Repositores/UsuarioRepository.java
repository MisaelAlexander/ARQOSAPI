package PTC._5.Repositores;

import PTC._5.Entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>
{
    //Count by {nombre de variable} paraq eu funcione debe de tener el nombre de
    //lo que almacena la fecha en Entity
    Long countByfechaDeCreacion(java.sql.Date fechadeCreacion);

    //Usmaos boolean por que es para validacion devuelve un si o no si ya esta o no esta
    boolean existsByusuario(String User);

    Optional<UsuarioEntity> findByUsuarioAnddescripcion_ESTADOTrue(String usuario);
    Optional<UsuarioEntity> findBydescripcion_CorreoAnddescripcion_ESTADOTrue(String correo);
}