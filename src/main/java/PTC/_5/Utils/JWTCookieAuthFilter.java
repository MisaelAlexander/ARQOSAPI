package PTC._5.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
public class JWTCookieAuthFilter extends OncePerRequestFilter
{
    private static final Logger log = LoggerFactory.getLogger(JWTCookieAuthFilter.class); //Escribe los logs con el nombre de la clase(en lo que entendi es el mensaje que da)
    private static final String AUTH_COOKIE_NAME = "authToken"; //Asignamos un valor por defecto como nombre
    private final JWTUtils jwtUtils; //Inyectamos el valor para poder validar y crear tokens al ingresar al JWTUtils

    /*Estamos autorizando una inyeccion de JWTUtils en cada filtro*/
    @Autowired
    public JWTCookieAuthFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try
        {
            String token = extractTokenFromCookies(request);
            if (token == null || token.isBlank())
            {
                // Para endpoints no públicos, requerimos token
                if (!isPublicEndpoint(request)) {
                    sendError(response, "Token no encontrado", HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                filterChain.doFilter(request, response);
                return;
            }
            Claims claims = jwtUtils.parseToken(token);
            //Extraer los datos extraibles del token
            String rol = jwtUtils.extractrol(token);
            String idubicacion = jwtUtils.extractidubicacion(token);
            String idusuario = jwtUtils.extractidusurio(token);
            String estado = jwtUtils.extractestado(token);

            //CrearAuthorities basado en rol
            Collection<? extends GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+ rol));

            // CREAR AUTENTICACIÓN CON AUTHORITIES CORRECTOS
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), // username
                            null, // credentials
                            authorities // ← ROLES REALES
                    );
            // ESTABLECER AUTENTICACIÓN EN CONTEXTO
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: {}", e.getMessage());
            sendError(response, "Token expirado", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            log.warn("Token malformado: {}", e.getMessage());
            sendError(response, "Token inválido", HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            log.error("Error de autenticación", e);
            sendError(response, "Error de autenticación", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }



    //Estraer las cookies
    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> AUTH_COOKIE_NAME.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    //Mandar el error
    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(String.format(
                "{\"error\": \"%s\", \"status\": %d}", message, status));
    }

    /*Endpoint publicos*/
    // MEJORADA: Lógica para endpoints públicos
    private boolean isPublicEndpoint(HttpServletRequest request)
    {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Endpoints públicos
        //Endponits que estan aqui
        //---Pin controller
        //1Enviar
        //2Validar
        //---Descripcion Controller
        //4DELETE
        //5POST
        //---Usuario Controller
        //6Guardar
        //7Verificar si es real
        //8Login
        //9Buscar por correo
        //10Buscar por usuario
        //11 Verificar codigo
        //12Restablecer Cotrasenia
        //---Rol Controller
        // 13Mostrar
        //--- Ubicacion Controller
        //14Mostrar
        //--- Uso de Cloudinary
        //15Agregar
        return (path.equals("/Pin/enviar") && "POST".equals(method)) ||
                (path.equals("/Pin/validar") && "POST".equals(method)) ||
                (path.equals("/Descripcion/Guardar") && "POST".equals(method)) ||
                (path.equals("/Descripcion/Eliminar") && "DELETE".equals(method)) ||
                (path.equals("/Descripcion/check-dui") && "GET".equals(method)) ||
                (path.equals("/Descripcion/check-correo") && "GET".equals(method)) ||
                (path.equals("/Descripcion/check-telefono") && "GET".equals(method)) ||
                (path.equals("/Usuario/Guardar") && "POST".equals(method)) ||
                (path.equals("/Usuario/check-usuario") && "GET".equals(method)) ||
                (path.equals("/Usuario/login") && "POST".equals(method)) ||
                (path.equals("/Usuario/BuscarPorUsuario") && "GET".equals(method)) ||
                (path.equals("/Usuario/BuscarPorCorreo") && "GET".equals(method)) ||
                (path.equals("/Usuario/Recuperar") && "POST".equals(method)) ||
                (path.equals("/Usuario/RestablecerContrasena") && "PUT".equals(method)) ||
                (path.equals("/Rol/Mostrar") && "GET".equals(method)) ||
                (path.equals("/Ubicacion/Mostrar") && "GET".equals(method)) ||
                (path.equals("/Imagen/Guardar") && "POST".equals(method))||
                (path.equals("/Imagen/GuardarCarpeta") && "POST".equals(method))||
                (path.equals("/Historial/Guardar") && "POST".equals(method));
    }
}
