package PTC._5.Controllers;

import PTC._5.Models.DTO.EstadoDTO;
import PTC._5.Models.DTO.TipoVisitaDTO;
import PTC._5.Services.EstadoService;
import PTC._5.Services.TipoVisitaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/TipoVisita")
@CrossOrigin
public class TipoVisitaController
{
    @Autowired
    private TipoVisitaServices tv;
    //localhost:8080/TipoVisita/Mostrar
    @GetMapping("/Mostrar")
    public List<TipoVisitaDTO> obtnerTipos()
    {
        return tv.obtenerTodos();
    }
}
