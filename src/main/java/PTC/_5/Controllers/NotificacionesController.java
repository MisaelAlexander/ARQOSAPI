package PTC._5.Controllers;

import PTC._5.Models.DTO.NotificacionesDTO;
import PTC._5.Services.NotificacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Notificacion")
@CrossOrigin
public class NotificacionesController
{
    @Autowired
    private NotificacionesService servicio;

    //localhost:8080/Notificacion/Mostrar
    @GetMapping("/Mostrar")
    public List<NotificacionesDTO> obtenerTodos() {
        return servicio.obtenerTodo();
    }

    //localhost:8080/Notificacion/ObtenerPorUsuario/{id}
    @GetMapping("/ObtenerPorUsuario/{id}")
    public List<NotificacionesDTO> obtenerPorUsuario(@PathVariable Long id) {
        return servicio.obtenerPorIdUsuario(id);
    }

    //localhost:8080/Notificacion/Guardar
    @PostMapping("/Guardar")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificacionesDTO guardar(@RequestBody NotificacionesDTO dto) {
        return servicio.guardarNotificacion(dto);
    }

    //localhost:8080/Notificacion/Eliminar/{id}
    @DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<Void> Eliminar(@PathVariable Long id)
    {
        servicio.eliminarNotificacion(id);
        return ResponseEntity.ok().build();
    }
}
