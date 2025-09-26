package PTC._5.Controllers;

import PTC._5.Entities.RolEntity;
import PTC._5.Models.ApiResponse.ApiResponse;
import PTC._5.Models.DTO.RolDTO;
import PTC._5.Services.RolServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Rol")
@CrossOrigin
public class RolController
{
    @Autowired
    private RolServices rol;
    // localhost:8080/Rol/Mostrar
    @GetMapping("/Mostrar")
    public ResponseEntity<ApiResponse<List<RolDTO>>> obtenerTodos()
    {
        try {
            List<RolDTO> data = rol.obtenerTodos();
            ApiResponse<List<RolDTO>> response = new ApiResponse<>(
                    true,
                    "Roles obtenidos exitosamente",
                    data
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<List<RolDTO>> response = new ApiResponse<>(
                    false,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (Exception e) {
            ApiResponse<List<RolDTO>> response = new ApiResponse<>(
                    false,
                    "Error inesperado al obtener roles",
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
