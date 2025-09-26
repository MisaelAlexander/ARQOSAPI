package PTC._5.Controllers;

import PTC._5.Exceptions.ComentariosUsuarioException.ComentarioVendedorNotFound;
import PTC._5.Models.ApiResponse.ApiPageResponse;
import PTC._5.Models.ApiResponse.ApiResponse;
import PTC._5.Models.DTO.ComentariosUsuarioDTO;
import PTC._5.Services.ComentariosUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Define esta clase como un controlador REST
@RestController
// Establece la ruta base para todas las peticiones
@RequestMapping("/ComentariosUsuario")
@CrossOrigin
public class ComentariosUsuariosController
{
    // Inyección del servicio que contiene la lógica del negocio
    @Autowired
    private ComentariosUsuarioService acceso;

    /**
     * Endpoint GET para obtener todos los comentarios entre usuarios (clientes y vendedores).
     * URL: http://localhost:8080/ComentariosUsuario/Mostrar
     */
    @GetMapping("/Mostrar")
    public ResponseEntity<Page<ComentariosUsuarioDTO>> obtenerTodos(
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
        Page<ComentariosUsuarioDTO> comments = acceso.obtenerTodos(page, size);
        if (comments == null)
        {
            ResponseEntity.badRequest().body(Map.of(
                    "status","Error al obtener datos"
            ));
        }
        return ResponseEntity.ok(comments);
    }

    /**
     * Endpoint GET para obtener los comentarios dirigidos a un vendedor específico.
     *  ID del vendedor
     * URL: http://localhost:8080/ComentariosUsuario/Vendedor/{id}
     */
    @GetMapping("/Vendedor/{id}")
    public ResponseEntity<ApiResponse<ApiPageResponse<ComentariosUsuarioDTO>>> obtenerPorIdVendedor(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size)
    {
        try
        {
            Page<ComentariosUsuarioDTO> comments = acceso.obtenerPorIdVendedor(id, page, size);
            //Si no hay comentarios
            if (comments.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(
                                false,
                                "No se encontraron comentarios para el vendedor con ID " + id,
                                null
                        ));
            }
            //Respuesta
            ApiPageResponse<ComentariosUsuarioDTO> pageResponse = new ApiPageResponse<>(comments);
            ApiResponse<ApiPageResponse<ComentariosUsuarioDTO>> response =
                    new ApiResponse<>(true,"Comentarios encontrados",pageResponse);
            return ResponseEntity.ok(response);
        }
        catch (ComentarioVendedorNotFound e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Endpoint GET para obtener los comentarios realizados por un cliente específico.
     *  ID del cliente
     * URL: http://localhost:8080/ComentariosUsuario/Cliente/{id}
     */
    @GetMapping("/Cliente/{id}")
    public ResponseEntity<ApiResponse<ApiPageResponse<ComentariosUsuarioDTO>>> obtenerPorIdCliente(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size)
    {
        try
        {
            Page<ComentariosUsuarioDTO> comments = acceso.obtenerPorIdCliente(id, page, size);
            //Si no hay comentarios
            if (comments.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(
                                false,
                                "No se encontraron comentarios para el vendedor con ID " + id,
                                null
                        ));
            }
            //Respuesta
            ApiPageResponse<ComentariosUsuarioDTO> pageResponse = new ApiPageResponse<>(comments);
            ApiResponse<ApiPageResponse<ComentariosUsuarioDTO>> response =
                    new ApiResponse<>(true,"Comentarios encontrados",pageResponse);
            return ResponseEntity.ok(response);
        }
        catch (ComentarioVendedorNotFound e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Endpoint POST para guardar un nuevo comentario entre usuarios.
     *  Objeto con los datos del comentario a guardar.
     * URL: http://localhost:8080/ComentariosUsuario/Guardar
     */
    @PostMapping("/Guardar")
    public ResponseEntity<ApiResponse<ComentariosUsuarioDTO>> guardarComentario(@Valid @RequestBody ComentariosUsuarioDTO dto) {
        try
        {
            ComentariosUsuarioDTO guardado = acceso.guardarComentario(dto);
            ApiResponse<ComentariosUsuarioDTO> response = new ApiResponse<>(
                    true,
                    "Comentario de usuario guardado con éxito",
                    guardado
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (RuntimeException e)
        {
            ApiResponse<ComentariosUsuarioDTO> response = new ApiResponse<>(
                    false,
                    "Error al guardar el comentario de usuario: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint PUT para actualizar un comentario existente entre usuarios.
     *  ID del comentario a actualizar.
     *  Objeto con los datos nuevos.
     * URL: http://localhost:8080/ComentariosUsuario/Actualizar/{id}
     */
    @PutMapping("/Actualizar/{id}")
    public ResponseEntity<ApiResponse<ComentariosUsuarioDTO>> actualizarComentario(
            @PathVariable Long id,
            @Valid @RequestBody ComentariosUsuarioDTO dto
    )
    {
        try {
            ComentariosUsuarioDTO actualizado = acceso.actualizarComentario(id, dto);
            ApiResponse<ComentariosUsuarioDTO> response = new ApiResponse<>(
                    true,
                    "Comentario de usuario actualizado con éxito",
                    actualizado
            );
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e)
        {
            ApiResponse<ComentariosUsuarioDTO> response = new ApiResponse<>(
                    false,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Endpoint DELETE para eliminar un comentario entre usuarios por su ID.
     *  ID del comentario a eliminar.
     * URL: http://localhost:8080/ComentariosUsuario/Eliminar/{id}
     */
    @DeleteMapping("/Eliminar/{id}")
public ResponseEntity<ApiResponse<Void>> eliminarComentario(@PathVariable Long id) {
        try {
            acceso.eliminarComentario(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    true,
                    "Comentario de usuario eliminado con éxito",
                    null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        catch (RuntimeException e)
        {
            ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
}

}
