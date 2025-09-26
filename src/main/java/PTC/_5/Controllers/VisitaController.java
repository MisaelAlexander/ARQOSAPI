package PTC._5.Controllers;

import PTC._5.Models.ApiResponse.ApiPageResponse;
import PTC._5.Models.ApiResponse.ApiResponse;
import PTC._5.Models.DTO.VisitaDTO;
import PTC._5.Services.VisitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Visita")
@CrossOrigin
public class VisitaController
{
    @Autowired
    VisitaService acceso;

    // localhost:8080/Visita/Mostrar
    @GetMapping("/Mostrar")
    public ResponseEntity<ApiResponse<ApiPageResponse<VisitaDTO>>> mostrarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        acceso.actualizarVisitasVencidas();
        Page<VisitaDTO> pageData = acceso.obtenerVisitas(PageRequest.of(page, size));
        ApiPageResponse<VisitaDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Visitas obtenidas correctamente", pageResponse)
        );
    }
    //localhost:8080/Visita/Guardar
    @PostMapping("/Guardar")
    public VisitaDTO Guardar(@Valid @RequestBody VisitaDTO dto) {
        return acceso.guardarVisita(dto);
    }

    //localhost:8080/Actualizar/Guardar
    @PutMapping("/Actualizar/{id}")
    public VisitaDTO Actualizar(@PathVariable Long id, @Valid @RequestBody VisitaDTO dto) {
        return acceso.actualizarVisita(id, dto);
    }

    // localhost:8080/Visita/Cliente/{idUsuario}
    @GetMapping("/Cliente/{idUsuario}")
    public ResponseEntity<ApiResponse<ApiPageResponse<VisitaDTO>>> obtenerVisitasPorCliente(
               @PathVariable Long idUsuario,
               @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size) {
    acceso.actualizarVisitasVencidas();
    Page<VisitaDTO> pageData = acceso.obtenerVisitasPorCliente(idUsuario, PageRequest.of(page, size));
    ApiPageResponse<VisitaDTO> pageResponse = new ApiPageResponse<>(pageData);
    
    return ResponseEntity.ok(
    new ApiResponse<>(true, "Visitas por cliente obtenidas correctamente", pageResponse)
    );
    }

    // localhost:8080/Visita/Vendedor/{idUsuario}
    @GetMapping("/Vendedor/{idUsuario}")
    public ResponseEntity<ApiResponse<ApiPageResponse<VisitaDTO>>> obtenerVisitasPorVendedor(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        acceso.actualizarVisitasVencidas();
        Page<VisitaDTO> pageData = acceso.obtenerVisitasPorVendedor(idUsuario, PageRequest.of(page, size));
        ApiPageResponse<VisitaDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Visitas por vendedor obtenidas correctamente", pageResponse)
        );
    }

    // localhost:8080/Visita/EstadoCliente?idEstado=2&idCliente=5
    @GetMapping("/EstadoCliente")
    public ResponseEntity<ApiResponse<ApiPageResponse<VisitaDTO>>> obtenerVisitasPorEstadoYCliente(
            @RequestParam Long idEstado,
            @RequestParam Long idCliente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        acceso.actualizarVisitasVencidas();
        Page<VisitaDTO> pageData = acceso.obtenerVisitasPorEstadoYCliente(idEstado, idCliente, PageRequest.of(page, size));
        ApiPageResponse<VisitaDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Visitas por estado y cliente obtenidas correctamente", pageResponse)
        );
    }

    // localhost:8080/Visita/EstadoVendedor?idEstado=2&idVendedor=3
    @GetMapping("/EstadoVendedor")
    public ResponseEntity<ApiResponse<ApiPageResponse<VisitaDTO>>> obtenerVisitasPorEstadoYVendedor(
            @RequestParam Long idEstado,
            @RequestParam Long idVendedor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        acceso.actualizarVisitasVencidas();
        Page<VisitaDTO> pageData = acceso.obtenerVisitasPorEstadoYVendedor(idEstado, idVendedor, PageRequest.of(page, size));
        ApiPageResponse<VisitaDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Visitas por estado y vendedor obtenidas correctamente", pageResponse)
        );
    }

    // localhost:8080/Visita/BuscarPorTitulo?titulo=casa&idUsuario=1&page=0&size=10
    @GetMapping("/BuscarPorTitulo")
    public ResponseEntity<ApiResponse<ApiPageResponse<VisitaDTO>>> buscarPorTitulo(
            @RequestParam String titulo,
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<VisitaDTO> pageData = acceso.obtenerVisitasPorTituloInmuebleYCliente(
                titulo,
                idUsuario,
                PageRequest.of(page, size)
        );

        ApiPageResponse<VisitaDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Visitas filtradas por título obtenidas correctamente", pageResponse)
        );
    }



    // localhost:8080/Visita/Inmueble/10
    @GetMapping("/Inmueble/{idInmueble}")
    public ResponseEntity<ApiResponse<List<VisitaDTO>>> obtenerVisitasPorInmueble(
            @PathVariable Long idInmueble) {

        List<VisitaDTO> visitas = acceso.obtenerVisitasPorInmueble(idInmueble);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Visitas por inmueble obtenidas correctamente", visitas)
        );
    }
}
