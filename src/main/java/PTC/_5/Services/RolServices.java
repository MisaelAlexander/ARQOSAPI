package PTC._5.Services;

import PTC._5.Entities.RolEntity;
import PTC._5.Models.DTO.RolDTO;
import PTC._5.Repositores.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marca esta clase como un servicio de Spring para manejar la lógica relacionada con Roles
public class RolServices
{
    @Autowired
    private RolRepository rolRepo; // Inyecta el repositorio para acceder a la tabla Roles

    // Método para obtener todos los roles existentes en la base de datos
    public List<RolDTO> obtenerTodos()
    {
        // Obtiene todas las entidades RolEntity
        List<RolEntity> lista = rolRepo.findAll();

        //lanzar excepción si está vacío, para control en Controller
        if (lista.isEmpty())
        {
            throw new RuntimeException("No se encontraron roles");
        }
        // Convierte cada entidad a un DTO para facilitar su uso en la capa presentación
        return lista.stream()
                .map(this::convertirDTO) // Convierte cada RolEntity a RolDTO
                .collect(Collectors.toList());
    }

    // Método privado que convierte una entidad RolEntity a un DTO RolDTO
    private RolDTO convertirDTO(RolEntity entity)
    {
        RolDTO dto = new RolDTO();
        dto.setIdrol(entity.getIDRol()); // Asigna el ID del rol
        dto.setRol(entity.getRol());     // Asigna el nombre del rol
        return dto; // Retorna el DTO listo para ser enviado al cliente
    }
}
