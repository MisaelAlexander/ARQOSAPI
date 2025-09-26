    package PTC._5.Controllers;

    import PTC._5.Exceptions.DescripcionException.DatosNoEncontradosException;
    import PTC._5.Models.ApiResponse.ApiResponse;
    import PTC._5.Models.DTO.DescripcionDTO;
    import PTC._5.Services.DescripcionService;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.server.ResponseStatusException;

    import java.util.List;

    // Declara que esta clase es un controlador REST
    @RestController
     // Define la ruta base para este controlador: http://localhost:8080/Descripcion
    @RequestMapping("/Descripcion")
    @CrossOrigin
    public class DescripcionController {
        // Inyección automática del servicio de descripción
        @Autowired
        DescripcionService acceso;

        /**
         * Endpoint GET para obtener todas las descripciones registradas.
         * URL: http://localhost:8080/Descripcion/Mostrar
         */
        @GetMapping("/Mostrar")
        public ResponseEntity<ApiResponse<Page<DescripcionDTO>>> obtenerTodos(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {
            try {
                Page<DescripcionDTO> data = acceso.obtenerTodo(page, size);

                ApiResponse<Page<DescripcionDTO>> response = new ApiResponse<>(
                        true,
                        "Descripciones obtenidas exitosamente",
                        data
                );
                return ResponseEntity.ok(response);

            } catch (DatosNoEncontradosException e) {
                ApiResponse<Page<DescripcionDTO>> response = new ApiResponse<>(
                        false,
                        e.getMessage(),
                        null
                );
                // 204 No Content es más adecuado para respuesta vacía sin cuerpo,
                // aquí usamos 404 para que el mensaje se vea bien, puedes cambiarlo si quieres
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

            } catch (RuntimeException e) {
                ApiResponse<Page<DescripcionDTO>> response = new ApiResponse<>(
                        false,
                        e.getMessage(),
                        null
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

            } catch (Exception e) {
                ApiResponse<Page<DescripcionDTO>> response = new ApiResponse<>(
                        false,
                        "Error inesperado al obtener descripciones",
                        null
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        /**
         * Endpoint POST para guardar una nueva descripción.
         * Objeto DTO con los datos a guardar.
         * URL: http://localhost:8080/Descripcion/Guardar
         */
        @PostMapping("/Guardar")
        public ResponseEntity<ApiResponse<DescripcionDTO>> Guardar(@Valid @RequestBody DescripcionDTO dto) {
            try {
                DescripcionDTO data = acceso.guardarDescripcion(dto);
                ApiResponse<DescripcionDTO> response = new ApiResponse<>(
                        true,
                        "Descripción guardada exitosamente",
                        data
                );
                return ResponseEntity.status(HttpStatus.CREATED).body(response);

            }
            catch (RuntimeException e) {
                // Captura errores de validación de unicidad
                return ResponseEntity
                        .badRequest()
                        .body(new ApiResponse<>(false, e.getMessage(), null));
            }
            catch (Exception e) {
                ApiResponse<DescripcionDTO> response = new ApiResponse<>(
                        false,
                        "Error inesperado al guardar descripción",
                        null
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        }

        /**
         * Endpoint PUT para actualizar una descripción existente según su ID.
         * ID de la descripción a actualizar.
         * DTO con los nuevos datos.
         * URL: http://localhost:8080/Descripcion/Actualizar/{id}
         */
        @PutMapping("/Actualizar/{id}")
        public ResponseEntity<ApiResponse<DescripcionDTO>> Actualizar(@PathVariable Long id, @Valid @RequestBody DescripcionDTO dto) {
            try {
                DescripcionDTO data = acceso.actualizarDescripcion(id, dto);
                ApiResponse<DescripcionDTO> response = new ApiResponse<>(
                        true,
                        "Descripción actualizada exitosamente",
                        data
                );
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                ApiResponse<DescripcionDTO> response = new ApiResponse<>(
                        false,
                        "Error inesperado al actualizar descripción",
                        null
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        /**
         * Endpoint DELETE para eliminar una descripción según su ID.
         * ID de la descripción a eliminar.
         * URL: http://localhost:8080/Descripcion/Eliminar/{id}
         * Devuelve una respuesta 204 No Content si se elimina correctamente.
         */
        @DeleteMapping("/Eliminar/{id}")
        public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id)
        {
            try {
                acceso.eliminarDescripcion(id);
                ApiResponse<Void> response = new ApiResponse<>(
                        true,
                        "Descripción eliminada exitosamente",
                        null
                );
                return ResponseEntity.ok(response);
            } catch (ResponseStatusException e) {
                ApiResponse<Void> response = new ApiResponse<>(
                        false,
                        e.getReason(),
                        null
                );
                return ResponseEntity.status(e.getStatusCode()).body(response);
            } catch (Exception e) {
                ApiResponse<Void> response = new ApiResponse<>(
                        false,
                        "Error inesperado al eliminar descripción",
                        null
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);//Devuelve 204
            }
        }
        /*Validaciones*/
        /*Con estas verificamso que lsoc ampos de correo telefono y dui no se dupliquen*/
        @GetMapping("/check-dui")
        public boolean checkDui(@RequestParam String dui) {
            return acceso.existeDui(dui);
        }

        @GetMapping("/check-correo")
        public boolean checkCorreo(@RequestParam String correo) {
            return acceso.existeCorreo(correo);
        }

        @GetMapping("/check-telefono")
        public boolean checkTelefono(@RequestParam String telefono) {
            return acceso.existeTelefono(telefono);
        }
    }



