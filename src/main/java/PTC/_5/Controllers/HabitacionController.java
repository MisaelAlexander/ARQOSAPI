package PTC._5.Controllers;

import PTC._5.Models.DTO.HabitacionesDTO;
import PTC._5.Models.DTO.TipoDTO;
import PTC._5.Services.HabitacionesService;
import PTC._5.Services.TipoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Habitacion")
@CrossOrigin    
public class HabitacionController
{
    @Autowired
    private HabitacionesService Hab;
    // localhost:8080/Habitacion/Mostrar
    @GetMapping("/Mostrar")
    public List<HabitacionesDTO> obtnerTipos()
    {
        return Hab.obtenerTodos();
    }
}
