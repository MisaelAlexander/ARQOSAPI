package PTC._5.Controllers;

import PTC._5.Entities.UsuarioEntity;
import PTC._5.Exceptions.UsuarioException.DatosNoEncontradosExceptionDes;
import PTC._5.Models.ApiResponse.ApiResponse;
import PTC._5.Models.ApiResponse.LoginResponse;
import PTC._5.Models.DTO.CambioContrasena.RestablecerContrasenaDTO;
import PTC._5.Models.DTO.DescripcionDTO;
import PTC._5.Models.DTO.LoginDTO;
import PTC._5.Models.DTO.UsuarioDTO;
import PTC._5.Services.UsuarioServices;
import PTC._5.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Usuario")
@CrossOrigin
public class UsuarioController
{
    @Autowired
    private UsuarioServices acceso;

    @Autowired
    private JWTUtils jwtUtils;

    // localhost:8080/Usuario/Mostrar
    @GetMapping("/Mostrar")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> mostrar() {
        try {
            List<UsuarioDTO> data = acceso.obtenerTodos();

            ApiResponse<List<UsuarioDTO>> response = new ApiResponse<>(
                    true,
                    "Usuarios obtenidos exitosamente",
                    data
            );
            return ResponseEntity.ok(response);

        } catch (DatosNoEncontradosExceptionDes e) {
            ApiResponse<List<UsuarioDTO>> response = new ApiResponse<>(
                    false,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (RuntimeException e) {
            ApiResponse<List<UsuarioDTO>> response = new ApiResponse<>(
                    false,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception e) {
            ApiResponse<List<UsuarioDTO>> response = new ApiResponse<>(
                    false,
                    "Error inesperado al obtener usuarios",
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    // localhost:8080/Usuario/Guardar
    @PostMapping("/Guardar")
    public ResponseEntity<ApiResponse<UsuarioDTO>> guardar(@Valid @RequestBody UsuarioDTO dto) {
        try {
            UsuarioDTO data = acceso.guardarUsuario(dto);

            ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                    true,
                    "Usuario guardado exitosamente",
                    data
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                    false,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                    false,
                    "Error inesperado al guardar usuario",
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // localhost:8080/Usuario/Actualizar/{id}
    @PutMapping("/Actualizar/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> actualizarSimple(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO dto) {

        try {
            UsuarioDTO actualizado = acceso.actualizarUsuario(id, dto);
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Usuario actualizado exitosamente",
                    actualizado
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(false, e.getMessage(), null)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Error inesperado al actualizar el usuario", null)
            );
        }
    }

    // localhost:8080/Usuario/Buscar/{id}
    @GetMapping("/Buscar/{id}")
    public ApiResponse<UsuarioDTO> BuscarID(@PathVariable Long id) {
        try {
            UsuarioDTO usuario = acceso.buscarporID(id);
            return new ApiResponse<>(true, "Usuario encontrado", usuario);
        } catch (RuntimeException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    // localhost:8080/Usuario/Contar
    @GetMapping("/Contar")
    public ApiResponse<Long> contarUsuarios() {
        long total = acceso.contarUsuarios();

        // Si quieres que lance excepción si no hay usuarios
        if (total == 0) {
            throw new RuntimeException("No hay usuarios registrados");
        }

        return new ApiResponse<>(true, "Cantidad total de usuarios", total);
    }

    // localhost:8080/Usuario/ContarFecha
    @GetMapping("/ContarFecha")
    public ApiResponse<Long> contarUsuariosFecha(
            @RequestParam("fecha")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        long total = acceso.contarFecha(fecha);

        if (total == 0) {
            throw new RuntimeException("No se encontraron usuarios en la fecha " + fecha);
        }

        return new ApiResponse<>(true, "Cantidad de usuarios en la fecha " + fecha, total);
    }


    //Validaciones
    @GetMapping("/check-usuario")
    public boolean checkUsuario(@RequestParam String usuario) {
        return acceso.existeUsuario(usuario);
    }
    /*LOGIN CON COOKIE*/
    //Login ta potente
    @PostMapping("/login")
    public ResponseEntity<LoginResponse>  login(@Valid @RequestBody LoginDTO data, HttpServletResponse response) {
        if (data.getUsuario() == null || data.getUsuario().isBlank() ||
                data.getContrasena() == null || data.getContrasena().isBlank())
        {
            return ResponseEntity.status(401).body( new LoginResponse(false, "Error: Credenciales incompletas"));
        }

        if (acceso.login(data.getUsuario(), data.getContrasena()))
        {
            addTokenCookie(response, data.getUsuario());
            return ResponseEntity.ok(new LoginResponse(true, "Inicio de sesión exitoso"));
        }
        return ResponseEntity.status(401).body( new LoginResponse(false,"Credenciales incorrectas" ) );
    }

    /**
     * Se genera el token y se guarda en la Cookie
     * @param response
     * @param
     */
    private void addTokenCookie(HttpServletResponse response, String usuario) {
        Optional<UsuarioEntity> userOpt = acceso.obtenerUsuario(usuario);

        if (userOpt.isPresent()) {
            UsuarioEntity user = userOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getIDUsuario()),
                    user.getUsuario(),
                    String.valueOf(user.getDescripcion().getUbicacion().getIDUbicacion()),
                    user.getDescripcion().getRol().getRol(),
                    String.valueOf(user.getDescripcion().getEstado())
            );


            String cookieValue = String.format(
                    "authToken=%s; " +
                            "Path=/; " +
                            "HttpOnly; " +
                            "Secure; " +
                            "SameSite=None; " +
                            "MaxAge=840; " +
                            "Domain=arqosapi-9796070a345d.herokuapp.com",
                    token
            );

           /* String cookieValue = String.format(
                    "authToken=%s; " +
                            "Path=/; " +
                            "HttpOnly; " +
                            "Secure; " +       //Esta es uan version de prueba de cookie sin heroku
                            "SameSite=None; " +
                            "MaxAge=86400",
                    token
            );*/

            /*Cookie para local*/
            /*String cookieValue = String.format(
                    "authToken=%s; " +
                            "Path=/; " +
                            "HttpOnly; " +
                            "SameSite=None; " +
                            "MaxAge=86400",
                    token
            );*/
           /*String cookieValue = String.format(
                    "authToken=%s; " +
                            "Path=/; " +
                            "HttpOnly; " +
                            "MaxAge=86400",
                    token
            );*/


            response.addHeader("Set-Cookie", cookieValue);
            //response.addHeader("Access-Control-Allow-Credentials", "true"); <-- ESTO NO DEBEN AGREGARLO
            response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "authenticated", false,
                                "message", "No autenticado"
                        ));
            }

            // Manejar diferentes tipos de Principal
            String username;
            Collection<? extends GrantedAuthority> authorities;

            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                username = userDetails.getUsername();
                authorities = userDetails.getAuthorities();
            } else {
                username = authentication.getName();
                authorities = authentication.getAuthorities();
            }

            Optional<UsuarioEntity> userOpt = acceso.obtenerUsuario(username);

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "authenticated", false,
                                "message", "Usuario no encontrado"
                        ));
            }

            UsuarioEntity user = userOpt.get();

            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "user", Map.of(
                            "id", user.getIDUsuario(),
                            "nombre", user.getDescripcion().getNombre(),
                            "estado", user.getEstado(),
                            "idubicacion", user.getDescripcion().getUbicacion().getIDUbicacion(),
                            "rol", user.getDescripcion().getRol().getRol(),
                            "authorities", authorities.stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList())
                    )
            ));

        } catch (Exception e) {
            //log.error("Error en /me endpoint: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "authenticated", false,
                            "message", "Error obteniendo datos de usuario"
                    ));
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Crear cookie de expiración con SameSite=None
        String cookieValue =
                "authToken=; Path=/; " +
                        "HttpOnly; Secure; " +
                        "SameSite=None; " +
                        "MaxAge=0;" +
                        " Domain=arqosapi-9796070a345d.herokuapp.com";

        response.addHeader("Set-Cookie", cookieValue);
        //response.addHeader("Access-Control-Allow-Credentials", "true"); <-- ESTO NO DEBEN AGREGARLO
        response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");

        // También agregar headers CORS para la respuesta
        String origin = request.getHeader("Origin");
        if (origin != null &&
                (origin.contains("localhost") || origin.contains("herokuapp.com"))) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        return ResponseEntity.ok()
                .body("Logout exitoso");
    }
        /*LOGIN CON COOKIE*/
    //Metodos para la busqeuda por nombre o correo recuperacion
    //Fuentes chatgpt(arrreglo como 2 errores) yo (lo cree y corregi los errores)
    // Buscar por usuario
    @GetMapping("/BuscarPorUsuario")
    public ResponseEntity<ApiResponse<UsuarioDTO>> BuscarPorUsuario(@RequestParam String usuario) {
        try {
            UsuarioDTO usuarioDTO = acceso.buscarPorUsuario(usuario);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuario encontrado", usuarioDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Buscar por correo
    @GetMapping("/BuscarPorCorreo")
    public ResponseEntity<ApiResponse<UsuarioDTO>> BuscarPorCorreo(@RequestParam String correo) {
        try {
            UsuarioDTO usuarioDTO = acceso.buscarPorCorreo(correo);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuario encontrado", usuarioDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Iniciar recuperación de contraseña
    @PostMapping("/Recuperar")
    public ResponseEntity<ApiResponse<String>> recuperarContrasena(@RequestParam String usuarioOCorreo) {
        try {
            boolean enviado = acceso.enviarPinRecuperacion(usuarioOCorreo);
            if (enviado) {
                return ResponseEntity.ok(new ApiResponse<>(true,
                        "Se ha enviado un PIN de recuperación al correo registrado", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No se encontró un usuario con ese valor", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al enviar PIN de recuperación", null));
        }
    }

    @PutMapping("/RestablecerContrasena")
    public ResponseEntity<ApiResponse<UsuarioDTO>> RestablecerContrasena (@Valid@RequestBody RestablecerContrasenaDTO dto)
    {
        try
        {
            // Ahora el servicio devuelve el UsuarioDTO actualizado
            UsuarioDTO usuarioActualizado = acceso.actualizarContrasenaPorCorreo(dto.getUsuario(), dto.getContrasenia());

            ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                    true,
                    "Contraseña cambiada",
                    usuarioActualizado // ← Ahora devolvemos el usuario
            );
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e)
        {
            ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                    false,
                    "Contraseña no cambiada: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        catch (Exception e)
        {
            ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                    false,
                    "Error interno al cambiar contraseña",
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
