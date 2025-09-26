package PTC._5.Controllers;

import PTC._5.Models.ApiResponse.ApiPageResponse;
import PTC._5.Models.ApiResponse.ApiResponse;
import PTC._5.Models.DTO.InmuebleDTO;
import PTC._5.Services.InmuebleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Inmueble")
@CrossOrigin("*")
public class InmuebleController
{
    @Autowired
    InmuebleService acceso;

    //localhost:8080/Inmueble/Mostrar
    @GetMapping("/Mostrar")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> mostrarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarActivos(PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Inmuebles obtenidos correctamente", pageResponse)
        );
    }

    // localhost:8080/Inmueble/MostrarActivos?page=0&size=5
    @GetMapping("/MostrarActivos")
    public ResponseEntity<ApiResponse<Page<InmuebleDTO>>> obtenerActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<InmuebleDTO> inmuebles = acceso.buscarActivos(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Inmuebles activos obtenidos correctamente", inmuebles)
        );
    }


    // localhost:8080/Inmueble/BuscarPorId/{id}
    @GetMapping("/BuscarPorId/{id}")
    public ResponseEntity<ApiResponse<InmuebleDTO>> obtenerPorId(@PathVariable Long id) {
        InmuebleDTO inmueble = acceso.buscarPorId(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Inmueble obtenido correctamente", inmueble)
        );
    }

    //localhost:8080/Inmueble/Guardar
    @PostMapping("/Guardar")
    public InmuebleDTO Guardar(@Valid @RequestBody  InmuebleDTO dto) {
        return acceso.guardarInmueble(dto);
    }

    //localhost:8080/Inmueble/Actualizar/{id}
    @PutMapping("/Actualizar/{id}")
    public InmuebleDTO Actualizar(@PathVariable Long id, @Valid @RequestBody  InmuebleDTO dto) {
        return acceso.actualizarInmueble(id,dto);
    }

    /*//localhost:8080/Inmueble/Eliminar/{id}
    @DeleteMapping("/Eliminar/{id}")
            public ResponseEntity<Void> Eliminar(@PathVariable Long id)
    {
        acceso.eliminarInmueble(id);
        return ResponseEntity.ok().build();
    }*/

    //localhost:8080/Inmueble/BuscarporUser/{id}
    @GetMapping("/BuscarporUser/{id}")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> buscarPorUsuario(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarporUser(id, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Inmuebles por usuario obtenidos correctamente", pageResponse)
        );
    }

    //localhost:8080/Inmueble/BuscarporUbi/{id}
    @GetMapping("/BuscarporUbi/{id}")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> buscarPorUbicacion(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarporUbi(id, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Inmuebles por ubicación obtenidos correctamente", pageResponse)
        );
    }

    //localhost:8080/Inmueble/BuscarporTip/{id}
    @GetMapping("/BuscarporTip/{id}")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> buscarPorTipoTexto(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarporTip(id, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Inmuebles filtrados por tipo", pageResponse)
        );
    }

    //localhost:8080/Inmueble/BuscarporTitu?titulo=C
    @GetMapping("/BuscarporTitu")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> buscarPorTipoTexto(
            @RequestParam String titulo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarPorTitulo(titulo, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Inmuebles filtrados por tipo", pageResponse)
        );
    }

    //localhost:8080/Inmueble/BuscarporUbi?ubicacion=C
    @GetMapping("/BuscarporUbi")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> obtenerporUbicacion(
            @RequestParam String ubicacion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarPorUbicacion(ubicacion, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Inmuebles filtrados por tipo", pageResponse)
        );
    }

    //localhost:8080/Inmueble/BuscarporTip?tipo=C
    @GetMapping("/BuscarporTip")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> obtenerPorTipo(
            @RequestParam String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarPorUbicacion(tipo, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Inmuebles filtrados por tipo", pageResponse)
        );
    }

      // localhost:8080/Inmueble/BuscarPorUbicacionYTipo?idUbicacion=1&idTipo=2&page=0&size=10
      @GetMapping("/BuscarPorUbicacionYTipo")
      public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> obtenerPorUbicacionYTipo(
              @RequestParam Long idUbicacion,
              @RequestParam Long idTipo,
              @RequestParam(defaultValue = "0") int page,
              @RequestParam(defaultValue = "10") int size) {

          Page<InmuebleDTO> pageData = acceso.buscarporUbiyTip(idUbicacion, idTipo, PageRequest.of(page, size));
          ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

          return ResponseEntity.ok(
                  new ApiResponse<>(true, "Inmuebles filtrados por ubicación y tipo", pageResponse)
          );
      }


      /*VENDEDOR*/
      // localhost:8080/Inmueble/BuscarPorUbiYUser?idUbicacion=1&idUsuario=2&page=0&size=10
      @GetMapping("/BuscarPorUbiYUser")
      public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> buscarPorUbiYUser(
              @RequestParam Long idUbicacion,
              @RequestParam Long idUsuario,
              @RequestParam(defaultValue = "0") int page,
              @RequestParam(defaultValue = "10") int size) {

          Page<InmuebleDTO> pageData = acceso.buscarPorUbiYUser(idUbicacion, idUsuario, PageRequest.of(page, size));
          ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

          return ResponseEntity.ok(new ApiResponse<>(true, "Inmuebles filtrados por ubicación y usuario", pageResponse));
      }

    // localhost:8080/Inmueble/BuscarPorTipYUser?idTipo=1&idUsuario=2&page=0&size=10
    @GetMapping("/BuscarPorTipYUser")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> buscarPorTipYUser(
            @RequestParam Long idTipo,
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarPorTipYUser(idTipo, idUsuario, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(new ApiResponse<>(true, "Inmuebles filtrados por tipo y usuario", pageResponse));
    }

    // localhost:8080/Inmueble/BuscarPorTituloYUser?titulo=C&idUsuario=2&page=0&size=10
    @GetMapping("/BuscarPorTituloYUser")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> buscarPorTituloYUser(
            @RequestParam String titulo,
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarPorTituloYUser(titulo, idUsuario, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(new ApiResponse<>(true, "Inmuebles filtrados por título y usuario", pageResponse));
    }

    // localhost:8080/Inmueble/BuscarPorUbicacionYUser?ubicacion=C&idUsuario=2&page=0&size=10
    @GetMapping("/BuscarPorUbicacionYUser")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> buscarPorUbicacionYUser(
            @RequestParam String ubicacion,
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarPorUbicacionYUser(ubicacion, idUsuario, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(new ApiResponse<>(true, "Inmuebles filtrados por ubicación (texto) y usuario", pageResponse));
    }

    // localhost:8080/Inmueble/BuscarPorTipoTextoYUser?tipo=C&idUsuario=2&page=0&size=10
    @GetMapping("/BuscarPorTipoTextoYUser")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> buscarPorTipoTextoYUser(
            @RequestParam String tipo,
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarPorTipoTextoYUser(tipo, idUsuario, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(new ApiResponse<>(true, "Inmuebles filtrados por tipo (texto) y usuario", pageResponse));
    }

    // localhost:8080/Inmueble/BuscarPorUbiTipYUser?idUbicacion=1&idTipo=2&idUsuario=3&page=0&size=10
    @GetMapping("/BuscarPorUbiTipYUser")
    public ResponseEntity<ApiResponse<ApiPageResponse<InmuebleDTO>>> buscarPorUbiTipYUser(
            @RequestParam Long idUbicacion,
            @RequestParam Long idTipo,
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InmuebleDTO> pageData = acceso.buscarPorUbiTipYUser(idUbicacion, idTipo, idUsuario, PageRequest.of(page, size));
        ApiPageResponse<InmuebleDTO> pageResponse = new ApiPageResponse<>(pageData);

        return ResponseEntity.ok(new ApiResponse<>(true, "Inmuebles filtrados por ubicación, tipo y usuario", pageResponse));
    }

}
