package PTC._5.Controllers;

import PTC._5.Models.DTO.DescripcionDTO;
import PTC._5.Models.DTO.FotoDTO;
import PTC._5.Models.DTO.InmuebleDTO;
import PTC._5.Services.FotoServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/Foto")
@CrossOrigin("*")
public class FotoController
{
@Autowired
    FotoServices acceso;
    //localhost:8080/Foto/Mostrar
    @GetMapping("/Mostrar")
    public List<FotoDTO> obtenerDatos()
    {
        return acceso.obtenerTodo();
    }

    //localhost:8080/Foto/Mostrar/{id}
    @GetMapping("/Mostrar/{id}")
    public List<FotoDTO> obtenerInmuebleId(@PathVariable Long id)
    {
        return acceso.buscarPorIdInmueble(id);
    }
    //localhost:8080/Foto/Guardar
    @PostMapping("/Guardar")
    public FotoDTO Guardar(@Valid @RequestBody  FotoDTO dto) {
        return acceso.guardarFoto(dto);
    }

    //localhost:8080/Foto/Eliminar/{id}
    @DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id)
    {
        acceso.eliminarFoto(id);
        return ResponseEntity.noContent().build();
    }

}
