package PTC._5.Controllers;

import PTC._5.Exceptions.ComentariosException.ComentarioInmuebleNotFound;
import PTC._5.Exceptions.ComentariosException.ComentarioUsuarioNotFound;
import PTC._5.Models.ApiResponse.ApiPageResponse;
import PTC._5.Models.ApiResponse.ApiResponse;
import PTC._5.Models.DTO.ComentariosDTO;
import PTC._5.Models.DTO.DescripcionDTO;
import PTC._5.Services.ComentariosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


// Anotación que indica que esta clase es un controlador REST
@RestController
// Ruta base del controlador
@RequestMapping("/ComentariosInmuebles")
@CrossOrigin
public class ComentariosController
{
    // Inyección del servicio que contiene la lógica para manejar los comentarios
    @Autowired
    ComentariosService acceso;

    /**
     * Endpoint GET para obtener todos los comentarios.
     * URL: http://localhost:8080/ComentariosInmuebles/Mostrar
     */
    @GetMapping("/Mostrar")
    public ResponseEntity<Page<ComentariosDTO>> obtenerDatos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5")  int size
    )
    {
            if (size <= 0 || size > 40)
            {
                ResponseEntity.badRequest().body(Map.of(
                        "status", "El tamaño debe de ser de 1 a 40"
                ));
                return ResponseEntity.ok(null);
            }
            Page<ComentariosDTO> comments = acceso.obtenerTodos(page, size);
            if (comments == null)
            {
                ResponseEntity.badRequest().body(Map.of(
                        "status","Error al obtener datos"
                ));
            }

            return ResponseEntity.ok(comments);


    }

    /**
     * Endpoint GET para obtener todos los comentarios de un inmueble específico.
     *  ID del inmueble
     * URL: http://localhost:8080/ComentariosInmuebles/Mostrar/Inmueble/{id}
     */
    @GetMapping("/Mostrar/Inmueble/{id}")
    public ResponseEntity<ApiResponse<ApiPageResponse<ComentariosDTO>>> obtenerPorIdInmueble(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size)
    {
        try
        {
            Page<ComentariosDTO> comments = acceso.obtenerPorIdInmueble(id,page,size);
            // Si no hay comentarios
            if (comments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false,
                                "No se encontraron comentarios para el inmueble con ID " + id,
                                null));
            }
            ApiPageResponse<ComentariosDTO> pageResponse = new ApiPageResponse<>(comments);
            ApiResponse<ApiPageResponse<ComentariosDTO>> response =
                    new ApiResponse<>(true,"Comentarios",pageResponse);
            return ResponseEntity.ok(response);
        }
        catch (ComentarioInmuebleNotFound e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Endpoint GET para obtener todos los comentarios hechos por un usuario específico.
     * ID del usuario
     * URL: http://localhost:8080/ComentariosInmuebles/BuscarPorUsuario/{id}
     */
    @GetMapping("/BuscarPorUsuario/{id}")
    public ResponseEntity<ApiResponse<ApiPageResponse<ComentariosDTO>>>  buscarPorIdUsuario(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size)
    {
        try
        {
            Page<ComentariosDTO> comments = acceso.obtenerPorIdUsuario(id, page, size);
            // Validar si hay resultados
            if (comments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false,
                                "No se encontraron comentarios para el usuario con ID " + id,
                                null));
            }
            //Respuesta Estandarizada
            ApiPageResponse<ComentariosDTO> pageResponse = new ApiPageResponse<>(comments);
            ApiResponse<ApiPageResponse<ComentariosDTO>> response =
                    new ApiResponse<>(true,"Comentarios encontrados",pageResponse);
            return ResponseEntity.ok(response);
        }
        catch (ComentarioUsuarioNotFound e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Endpoint POST para guardar un nuevo comentario.
     * Requiere un JSON válido con los datos del comentario.
     * URL: http://localhost:8080/ComentariosInmuebles/Guardar
     */
    @PostMapping("/Guardar")
    public ResponseEntity<ApiResponse<ComentariosDTO>> Guardar(@Valid @RequestBody ComentariosDTO dto)
    {
        try
        {
            ComentariosDTO guardado = acceso.guardar(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            true,
                            "Comentario guardado exitosamente",
                            guardado));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false,
                            "Fallo al capturar algun id",
                            null));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false,
                            "Error inesperado al guardar",
                            null));
        }
    }

    /**
     * Endpoint PUT para actualizar un comentario existente.
     *  ID del comentario a actualizar
     *  Datos nuevos del comentario
     * URL: http://localhost:8080/ComentariosInmuebles/Actualizar/{id}
     */
    @PutMapping("/Actualizar/{id}")
    public ResponseEntity<ApiResponse<ComentariosDTO>> Actualizar(@PathVariable Long id, @Valid @RequestBody ComentariosDTO dto)
    {
        try
        {
            ComentariosDTO actualizado = acceso.actualizarComentario(id, dto);
            ApiResponse<ComentariosDTO> response = new ApiResponse<>(
                    true,
                    "Comentario actualizado correctamente",
                    actualizado
            );
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false,
                            "Error inesperado al actualizar",
                            null));
        }
    }

    /**
     * Endpoint DELETE para eliminar un comentario por su ID.
     * ID del comentario a eliminar
     * URL: http://localhost:8080/ComentariosInmuebles/Eliminar/{id}
     */
    @DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id)
    {
        acceso.eliminar(id);
        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Comentario eliminado correctamente",
                null
        );
        // Retorna una respuesta HTTP 204 (sin contenido)
        return ResponseEntity.ok(response);
    }
}
