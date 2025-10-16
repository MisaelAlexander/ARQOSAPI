package PTC._5.Services;

import PTC._5.Config.Argon2.Argon2Password;
import PTC._5.Entities.DescripcionEntity;
import PTC._5.Entities.InmuebleEntity;
import PTC._5.Entities.UsuarioEntity;
import PTC._5.Exceptions.UsuarioException.DatosNoEncontradosExceptionDes;
import PTC._5.Models.DTO.DescripcionDTO;
import PTC._5.Models.DTO.UsuarioDTO;
import PTC._5.Repositores.DescripcionRepository;
import PTC._5.Repositores.InmuebleRepository;
import PTC._5.Repositores.UsuarioRepository;
import PTC._5.Services.CorreoPin.PinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Marca esta clase como un servicio de Spring (lógica de negocio)
public class UsuarioServices
{
    @Autowired
    private DescripcionRepository descRepo;
    @Autowired
    private UsuarioRepository userRepo; // Repositorio para manejar datos de usuarios
    @Autowired
    private InmuebleRepository inmuebleRepository;
    @Autowired
    PinService PinServi;

    @Autowired
    DescripcionService ServiDesc; // Servicio para manejar descripciones (nombre, dirección, etc.)

    @Autowired
    Argon2Password argon2;


    @Autowired
    Argon2Password objHash;


    public UsuarioDTO desactivarUsuarioYInmuebles(Long id) {
        // 1. Buscar el usuario si
        UsuarioEntity usuario = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));

        // 2. Desactivar el usuario
        usuario.setEstado(false);
        UsuarioEntity usuarioActualizado = userRepo.save(usuario);

        // 3. Buscar y desactivar todos los inmuebles activos del usuario
        List<InmuebleEntity> inmueblesActivos = inmuebleRepository.findByUsuario_IDUsuarioAndEstadoTrue(id);

        if (!inmueblesActivos.isEmpty()) {
            for (InmuebleEntity inmueble : inmueblesActivos) {
                inmueble.setEstado(false); // Cambiar estado a false
            }
            // Guardar todos los inmuebles actualizados
            inmuebleRepository.saveAll(inmueblesActivos);

            System.out.println("Se desactivaron " + inmueblesActivos.size() + " inmuebles del usuario ID: " + id);
        }

        return convertirDTO(usuarioActualizado);
    }
    // =====================================
    // GET - Obtener todos los usuarios
    // =====================================
    public List<UsuarioDTO> obtenerTodos() {
        List<UsuarioEntity> lista = userRepo.findAll();

        if (lista.isEmpty()) {
            throw new DatosNoEncontradosExceptionDes("No hay usuarios registrados");
        }

        return lista.stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    // =====================================
    // POST - Guardar nuevo usuario
    // =====================================
    public UsuarioDTO guardarUsuario(UsuarioDTO dto)
    {
        ValidarCampos(dto);
        // Crea una nueva entidad de usuario y le asigna los valores del DTO
        UsuarioEntity UserEntity = new UsuarioEntity();
        UserEntity.setUsuario(dto.getUsuario());
        UserEntity.setContrasena(argon2.EncryptPassword(dto.getContrasenia()));
        UserEntity.setEstado(dto.getEstado());
        UserEntity.setFechaDeCreacion(dto.getFechadecreacion());

        // Si el usuario tiene una descripción relacionada (nombre, dirección, etc.)
        if (dto.getIddescripcion() != null) {
            DescripcionEntity descentity = ServiDesc.buscariddedesc(dto.getIddescripcion());
            UserEntity.setDescripcion(descentity);
        }

        // Guarda el usuario en la base de datos
        UsuarioEntity guardado = userRepo.save(UserEntity);

        // Retorna el usuario guardado como DTO
        return convertirDTO(guardado);
    }

    // =====================================
    // PUT - Actualizar usuario existente
    // =====================================
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
        // Buscar usuario
        UsuarioEntity existe = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));

        // Actualizar datos permitidos del usuario
        if (dto.getUsuario() != null && !dto.getUsuario().equals(existe.getUsuario())) {
            existe.setUsuario(dto.getUsuario());
        }

        if (dto.getEstado() != null) existe.setEstado(dto.getEstado());
        if (dto.getFechadecreacion() != null) existe.setFechaDeCreacion(dto.getFechadecreacion());
        //if (dto.getContrasenia() != null) existe.setContrasena((dto.getContrasenia()));


        // Actualizar descripción
        if (dto.getIddescripcion() != null) {
            // Crear DTO de descripción con los campos editables
            //Esto campos son ttambien lo que llamos desde la vista
            DescripcionDTO descDto = new DescripcionDTO();
            descDto.setIddescripcion(dto.getIddescripcion());
            descDto.setNombre(dto.getNombre());
            descDto.setApellido(dto.getApellido());
            descDto.setDui(dto.getDui());
            descDto.setDireccion(dto.getDireccion());
            descDto.setCorreo(dto.getCorreo());
            descDto.setTelefono(dto.getTelefono());
            descDto.setFotoperfil(dto.getFotoPerfil());
            descDto.setFechadenacimiento(dto.getFechadeNacimiento());
            descDto.setEstado(dto.getEstadoD());

            // Rol se conserva automáticamente desde la entidad existente
            DescripcionEntity descExistente = existe.getDescripcion();
            descDto.setIdrol(descExistente.getRol().getIDRol());

            // Ubicación editable
            descDto.setIdubicacion(dto.getIDUbicacion() != null ? dto.getIDUbicacion() :
                    (descExistente.getUbicacion() != null ? descExistente.getUbicacion().getIDUbicacion() : null));

            // Actualizar usando el service
            DescripcionDTO descActualizada = ServiDesc.actualizarDescripcion(descDto.getIddescripcion(), descDto);

            // Asignar la descripción actualizada al usuario
            existe.setDescripcion(ServiDesc.buscariddedesc(descActualizada.getIddescripcion()));
        }

        // ============================
        // Guardar usuario actualizado
        // ============================
        UsuarioEntity actualizado = userRepo.save(existe);

        return convertirDTO(actualizado);
    }

    // =====================================
    // Buscar por ID
    // =====================================
    public UsuarioDTO buscarporID(Long id)
    {
        UsuarioEntity entity = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró usuario"));
        return convertirDTO(entity);
    }

    // =====================================
    // Contar todos los usuarios
    // =====================================
    public long contarUsuarios()
    {
        return userRepo.count(); // Retorna la cantidad total de usuarios
    }

    // =====================================
    // Contar usuarios creados en una fecha
    // =====================================
    public long contarFecha(LocalDate date)
    {
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        return userRepo.countByfechaDeCreacion(sqlDate); // Cuenta usuarios por fecha exacta
    }

    // =====================================
    // Conversión de entidad a DTO
    // =====================================
    private UsuarioDTO convertirDTO(UsuarioEntity entity)
    {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdusuario(entity.getIDUsuario());
        dto.setUsuario(entity.getUsuario());
        dto.setContrasenia(entity.getContrasena());
        dto.setEstado(entity.getEstado());
        dto.setFechadecreacion(entity.getFechaDeCreacion());

        // Si el usuario tiene descripción (relación con TBDescripcion)
        if (entity.getDescripcion() != null)
        {
            dto.setIddescripcion(entity.getDescripcion().getIDDescripcion());
            dto.setNombre(entity.getDescripcion().getNombre());
            dto.setApellido(entity.getDescripcion().getApellido());
            dto.setDui(entity.getDescripcion().getDui());
            dto.setDireccion(entity.getDescripcion().getDireccion());
            dto.setCorreo(entity.getDescripcion().getCorreo());
            dto.setTelefono(entity.getDescripcion().getTelefono());
            dto.setFotoPerfil(entity.getDescripcion().getFotoPerfil());
            dto.setFechadeNacimiento(entity.getDescripcion().getFechaNacimiento());
            dto.setEstadoD(entity.getDescripcion().getEstado());

            // Si la descripción tiene ubicación
            if (entity.getDescripcion().getUbicacion() != null)
            {
                dto.setIDUbicacion(entity.getDescripcion().getUbicacion().getIDUbicacion());
                dto.setUbicacion(entity.getDescripcion().getUbicacion().getUbicacion());
            }

            // Si la descripción tiene rol
            if (entity.getDescripcion().getRol() != null)
            {
                dto.setIDRol(entity.getDescripcion().getRol().getIDRol());
                dto.setRol(entity.getDescripcion().getRol().getRol());
            }
        }


        return dto; // Devuelve el DTO ya preparado para el cliente
    }

    /*Buscar por Usuario*/
    public UsuarioDTO buscarPorUsuario(String usuario) {
        UsuarioEntity entity = userRepo.findByusuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con nombre: " + usuario));
        return convertirDTO(entity);
    }
    /*Buscar por correo*/
    public UsuarioDTO buscarPorCorreo(String correo) {
        UsuarioEntity entity = userRepo.findBydescripcion_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));
        return convertirDTO(entity);
    }

    /*Enviar PIN para Recuperacion*/
    // Enviar PIN para recuperación
    /*Buscamos apra enviar el pin*/
    public boolean enviarPinRecuperacion(String usuarioOCorreo) {
        Optional<UsuarioEntity> usuarioOpt;
        //Si el texto lleva @
        if (usuarioOCorreo.contains("@"))
        {
            //buscar aen correo
            usuarioOpt = userRepo.findBydescripcion_Correo(usuarioOCorreo);
        }
        else
        //Si no lleva @ busca por nombre
        {
            usuarioOpt = userRepo.findByusuario(usuarioOCorreo);
        }
        if (usuarioOpt.isEmpty()) return false;

        UsuarioEntity user = usuarioOpt.get();
        PinServi.generarYEnviarPin(user.getDescripcion().getCorreo()); // Enviar PIN al correo
        return true;
    }

    // Restablecer contraseña
    public UsuarioDTO actualizarContrasenaPorCorreo(String usuario, String nuevaContrasena) {
        UsuarioEntity user = userRepo.findByusuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setContrasena(argon2.EncryptPassword(nuevaContrasena));
        UsuarioEntity usuarioActualizado = userRepo.save(user);

        return convertirDTO(usuarioActualizado); // Ahora devuelve el DTO para el historial :D
    }
    /*Validaciones*/
    private void ValidarCampos(UsuarioDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El Usuario no puede ser nulo");
        }
        if (dto.getUsuario() == null) {
            throw new IllegalArgumentException("El nombre usuario no puede ser nulo");
        }
        if (dto.getContrasenia() == null) {
            throw new IllegalArgumentException("La contrasenia no puede ser nula");
        }
        if (dto.getFechadecreacion() == null) {
            throw new IllegalArgumentException("La fecha de creacion no es nula");
        }
        if (dto.getEstado() == null) {
            throw new IllegalArgumentException("Es obligatoria una direccion");
        }
    }
    public boolean existeUsuario(String user) {
        return userRepo.existsByusuario(user);
    }


    // Login seguro
    public boolean login(String usuario, String contrasena)
    {
        Argon2Password objHash = new Argon2Password();

        // Buscar usuario
        Optional<UsuarioEntity> list = userRepo.findByusuario(usuario).stream().findFirst();
        if (list.isPresent())
        {
            UsuarioEntity usuariolog= list.get();
            String nombre = usuariolog.getDescripcion().getNombre();
            System.out.println("Usuario encontrado con ID: " + usuariolog.getIDUsuario() +
                    "Usuario" + usuariolog.getUsuario() +
                    "IDUbicacion" + usuariolog.getDescripcion().getUbicacion().getIDUbicacion()+
                    "Rol"+ usuariolog.getDescripcion().getRol().getIDRol());
            return objHash.VerifyPassword(usuariolog.getContrasena(), contrasena);
        }
        return false;
    }

    public Optional<UsuarioEntity> obtenerUsuario(String Usuario){
        Optional<UsuarioEntity> userOpt = userRepo.findByusuario(Usuario);
        return (userOpt != null) ? userOpt : null;
    }
}
