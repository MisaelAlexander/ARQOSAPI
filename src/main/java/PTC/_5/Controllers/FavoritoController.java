package PTC._5.Controllers;

import PTC._5.Models.ApiResponse.ApiPageResponse;
import PTC._5.Models.ApiResponse.ApiResponse;
import PTC._5.Models.DTO.FavoritosDTO;
import PTC._5.Services.FavoritoServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Favorito")
@CrossOrigin
public class FavoritoController
{
    @Autowired
    private FavoritoServices acceso;


    //  localhost:8080/Favorito/Usuario/5
    //  localhost:8080/Favorito/Usuario/5?page=0&size=10
    @GetMapping("/Usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<ApiPageResponse<FavoritosDTO>>> obtenerFavoritosPorUsuario(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FavoritosDTO> pageData = acceso.obtenerFavoritosPorUsuario(idUsuario, PageRequest.of(page, size));
        ApiPageResponse<FavoritosDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Favoritos obtenidos correctamente", pageResponse)
        );
    }


    //  localhost:8080/Favorito/Guardar
    @PostMapping("/Guardar")
    public FavoritosDTO guardarFavorito(@Valid @RequestBody FavoritosDTO dto) {
        return acceso.guardarFavorito(dto);
    }

    //  localhost:8080/Favorito/Eliminar/1
    @DeleteMapping("/Eliminar/{idFavorito}")
    public ResponseEntity<Void> eliminarFavorito(@PathVariable Long idFavorito) {
        acceso.eliminarFavorito(idFavorito);
        return ResponseEntity.noContent().build();
    }
}
