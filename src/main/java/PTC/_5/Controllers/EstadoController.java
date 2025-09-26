package PTC._5.Controllers;

import PTC._5.Models.DTO.BaniosDTO;
import PTC._5.Models.DTO.EstadoDTO;
import PTC._5.Services.BaniosService;
import PTC._5.Services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Estado")
@CrossOrigin
public class EstadoController
{
    @Autowired
    private EstadoService Est;
    //localhost:8080/Estado/Mostrar
    @GetMapping("/Mostrar")
    public List<EstadoDTO> obtnerTipos()
    {
        return Est.obtenerTodos();
    }
}
