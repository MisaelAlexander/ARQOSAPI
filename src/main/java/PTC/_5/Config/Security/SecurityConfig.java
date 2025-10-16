package PTC._5.Config.Security;

import PTC._5.Utils.JWTCookieAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    private final JWTCookieAuthFilter jwtCookieAuthFilter;
    public SecurityConfig(JWTCookieAuthFilter jwtCookieAuthFilter) {
        this.jwtCookieAuthFilter = jwtCookieAuthFilter;
    }
    // Configuración de seguridad HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Aqui van todos los endPoints públicos que no requieren de un JWT
        http
                .csrf(csrf -> csrf.disable())  // Nuevo estilo lambda
                .authorizeHttpRequests(auth -> auth
                        //  authorizeHttpRequests
                        //PUBLICOS
                        .requestMatchers(HttpMethod.POST, "/Imagen/Guardar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/Imagen/GuardarCarpeta").permitAll()
                        .requestMatchers(HttpMethod.POST, "/Pin/enviar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/Pin/validar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/Descripcion/Guardar").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/Descripcion/Eliminar/**").permitAll() // ← AGREGAR ESTO
                        .requestMatchers(HttpMethod.GET, "/Descripcion/check-dui").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Descripcion/check-correo").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Descripcion/check-telefono").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Rol/Mostrar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Ubicacion/Mostrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/Usuario/Guardar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Usuario/Contar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Usuario/ContarFecha").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Usuario/check-usuario").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // ← Permite preflight requests
                        .requestMatchers(HttpMethod.POST,"/Usuario/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/Usuario/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Usuario/BuscarPorCorreo").permitAll()
                        .requestMatchers(HttpMethod.GET, "/Usuario/BuscarPorUsuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/Usuario/Recuperar").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/Usuario/RestablecerContrasena").permitAll()
                        .requestMatchers(HttpMethod.POST, "/Historial/Guardar").permitAll()
                        //VENDEDOR
                        .requestMatchers(HttpMethod.GET, "/Banio/Mostrar").hasRole("Vendedor")
                        //.requestMatchers(HttpMethod.GET, "/Estado/Mostrar").hasRole("Vendedor")
                        .requestMatchers(HttpMethod.POST, "/Foto/Guardar").hasRole("Vendedor")
                        .requestMatchers(HttpMethod.DELETE, "/Foto/Eliminar/{id}").hasRole("Vendedor")
                        .requestMatchers(HttpMethod.GET, "/Habitacion/Mostrar").hasRole("Vendedor")
                        .requestMatchers(HttpMethod.POST, "/Inmueble/Guardar").hasRole("Vendedor")
                        .requestMatchers(HttpMethod.PUT, "/Inmueble/Actualizar/{id}").hasRole("Vendedor")
                        //.requestMatchers(HttpMethod.GET, "/Tipo/Mostrar").hasRole("Vendedor")
                        .requestMatchers(HttpMethod.GET, "/Visita/Actualizar/{id}").hasRole("Vendedor")
                        //.requestMatchers(HttpMethod.GET, "/Visita/Inmueble/{idInmueble}").hasRole("Vendedor")
                        //.requestMatchers(HttpMethod.GET, "/Visita/BuscarPorTituloV").hasRole("Vendedor")
                        //USUARIO
                        .requestMatchers(HttpMethod.GET, "/Favorito/Usuario/{idUsuario}").hasRole("Usuario")
                        .requestMatchers(HttpMethod.POST, "/Favorito/Guardar").hasRole("Usuario")
                        .requestMatchers(HttpMethod.DELETE, "/Favorito/Eliminar/{idFavorito}").hasRole("Usuario")
                        .requestMatchers(HttpMethod.POST, "/Visita/Guardar").hasRole("Usuario")
                        .requestMatchers(HttpMethod.GET, "/TipoVisita/Mostrar").hasRole("Usuario")
                        //.requestMatchers(HttpMethod.GET, "/Visita/BuscarPorTituloU").hasRole("Usuario")
                       // .requestMatchers("/api/auth/me").authenticated()
                       // .requestMatchers("/api/test/admin-only").hasRole("Administrador")
                        //.requestMatchers("/api/test/cliente-only").hasRole("Cliente")
                        //TODOS AUTENTICADOS QUE USAN LOS USUARIOS Y LOS VENDEDORES
                        .requestMatchers("/Usuario/me").authenticated()
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
