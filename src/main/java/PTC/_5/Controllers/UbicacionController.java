package PTC._5.Controllers;

import PTC._5.Models.ApiResponse.ApiResponse;
import PTC._5.Models.DTO.UbicacionDTO;
import PTC._5.Services.UbicacionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Ubicacion")
@CrossOrigin
public class UbicacionController
{
    @Autowired
    private UbicacionServices ubi;
    // localhost:8080/Ubicacion/Mostrar
    @GetMapping("/Mostrar")
    public ResponseEntity<ApiResponse<List<UbicacionDTO>>>  obtenerTodos()
    {
        try
        {
            List<UbicacionDTO> data = ubi.obtenertodos();
            ApiResponse<List<UbicacionDTO>> response = new ApiResponse<>(
                    true,
                    "Ubicaciones obtenidas exitosamente",
                    data
            );
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e)
        {
            ApiResponse<List<UbicacionDTO>> response = new ApiResponse<>(
                    false,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        catch (Exception e)
        {
            ApiResponse<List<UbicacionDTO>> response = new ApiResponse<>(
                    false,
                    "Error inesperado al obtener ubicaciones",
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
