package PTC._5.Controllers;

import PTC._5.Models.DTO.TipoDTO;
import PTC._5.Services.TipoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Tipo")
@CrossOrigin
public class TipoController
{
    @Autowired
    private TipoServices Tipo;

    // localhost:8080/Tipo/Mostrar
    @GetMapping("/Mostrar")
    public List<TipoDTO> obtnerTipos()
    {
        return Tipo.obtenerTodos();
    }
}
