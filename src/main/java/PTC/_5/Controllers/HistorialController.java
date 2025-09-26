package PTC._5.Controllers;

import PTC._5.Models.ApiResponse.ApiPageResponse;
import PTC._5.Models.ApiResponse.ApiResponse;
import PTC._5.Models.DTO.HistorialDTO;
import PTC._5.Services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Historial")
@CrossOrigin
public class HistorialController
{
    @Autowired
    private HistorialService acceso;
    //localhost:8080/Historial/Mostrar
    @GetMapping("/Mostrar")
    public ResponseEntity<ApiResponse<ApiPageResponse<HistorialDTO>>> obtenerTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    )
    {
        Page<HistorialDTO> historiales = acceso.obtenerTodo(page, size);
        if (historiales.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false,"no se encontraron historiales", null));
        }
        ApiPageResponse<HistorialDTO> pageResponse = new ApiPageResponse<>(historiales);
        ApiResponse<ApiPageResponse<HistorialDTO>> response =
                new ApiResponse<>(true, "Historiales encontrados", pageResponse);

        return ResponseEntity.ok(response);
    }

            //localhost:8080/Historial/Usuario/{id}
        @GetMapping("/Usuario/{id}")
        public ResponseEntity<ApiResponse<ApiPageResponse<HistorialDTO>>> obtenerPorIdUsuario(
                @PathVariable Long id,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size)
        {
            Page<HistorialDTO> historiales = acceso.obtenerPorIdUsuario(id, page, size);

            if (historiales.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false,
                                "No se encontraron historiales para el usuario con ID " + id,
                                null));
            }

            ApiPageResponse<HistorialDTO> pageResponse = new ApiPageResponse<>(historiales);
            ApiResponse<ApiPageResponse<HistorialDTO>> response =
                    new ApiResponse<>(true, "Historiales encontrados", pageResponse);

            return ResponseEntity.ok(response);
        }
        //localhost:8080/Historial/Guardar
        @PostMapping("/Guardar")
        public ResponseEntity<ApiResponse<HistorialDTO>> guardarHistorial(@RequestBody HistorialDTO dto) {
            try {
                HistorialDTO guardado = acceso.guardarHistorial(dto);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse<>(true, "Historial guardado exitosamente", guardado));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, e.getMessage(), null));
            }
        }
}
