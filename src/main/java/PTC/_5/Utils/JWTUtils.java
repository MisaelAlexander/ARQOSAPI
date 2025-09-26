package PTC._5.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
@Component
public class JWTUtils
{
    @Value("${security.jwt.secret}")
    private String jwtSecreto;                  // 32 caracteres por seguridad
    @Value("${security.jwt.issuer}")
    private String issuer;                      // Firma del token
    @Value("${security.jwt.expiration}")
    private long expiracionMs;                  // Tiempo de expiración

    private final Logger log = LoggerFactory.getLogger(JWTUtils.class);

    /**
     * Metodo para crea JWT
     * @param idusuario
     * @param rol
     * @param idubicacion
     * @return
     */
    /*Datos capturar*/
    public String create(String idusuario, String usuario, String idubicacion, String rol, String estado)
    {
        //Decodificar Base 64 y crea una clave segura tipo HMAC-SHA
        SecretKey signingKey =  Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecreto));

        //Obtienen la fecha actual y la fecha actual y calcula la fecha de expiracion
        //Capturamso fecha actual
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expiracionMs);

        //Construye el token con sus componentes
        return Jwts.builder()
                .setId(idusuario) //ID UNICO(JWT ID)(Usaremos el del usuario)
                .setIssuedAt(now) // Fecha de emision
                .setSubject(usuario) //Sujeto(usuario(el nombre del usuario))
                .claim("id", idusuario) //Datos a cargar
                .claim("idubicacion", idubicacion) //Datos a cargar
                .claim("rol", rol) //Datos a cargar
                .claim("estado", estado)
                .setIssuer(issuer) // Emisor del token
                .setExpiration(expiracionMs  >= 0 ? expiration : null) // Expiración (si es >= 0)
                .signWith(signingKey, SignatureAlgorithm.HS256)         // Firma con algoritmo HS256
                .compact();                                             // Convierte a String compacto
    }

    public String extractidusurio(String token)
    {
        Claims claims = parseToken(token); //1. parsea y valida el token
        return  claims.get("id", String.class);// 2.devuelve el token
    }

    //Extrae el token
    public String extractidubicacion(String token)
    {
        Claims claims = parseToken(token); //1. parsea y valida el token
        return  claims.get("idubicacion", String.class);// 2.devuelve el token
    }

    //Extrae el rol
    public String extractrol(String token)
    {
        Claims claims = parseToken(token); //1. parsea y valida el token
        return  claims.get("rol", String.class);// 2.devuelve el token
    }

    //Extrae el estado
    public String extractestado(String token)
    {
        Claims claims = parseToken(token); //1. parsea y valida el token
        return  claims.get("estado", String.class);// 2.devuelve el token
    }
    /**
     * Parsea y valida el token
     * @param jwt
     * @return
     * @throws ExpiredJwtException
     * @throws MalformedJwtException
     */
    public Claims parseToken(String jwt) throws ExpiredJwtException, MalformedJwtException {
        return parseClaims(jwt); //Si tiene error lo devolvemos
    }

    /**
     * Validación del token
     * @param token
     * @return
     */
    public boolean validate(String token){
        try{
            parseClaims(token);//con el try intenta parsear el token
            return true; //si no hay problemas es valido
        }catch (JwtException | IllegalArgumentException e){
            log.warn("Token inválido: {}", e.getMessage()); //Si no es valido manda advertencia
            return false;// si hay problemas no es valido
        }
    }


    //######################## METODOS COMPLEMENTARIOS ########################

    /**
     * Metodo privado para parsear los claims de un JWT
     * @param jwt Token a parsear
     * @return Claims del token
     */
    private Claims parseClaims(String jwt) {
        //Configura el parse con la clave de firma y parsea el token
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecreto)))  // Configurar Clave
                .build()  //2.Construir parser
                .parseClaimsJws(jwt) // 3.Parsear token
                .getBody(); //4.Obtener payload
    }
}
