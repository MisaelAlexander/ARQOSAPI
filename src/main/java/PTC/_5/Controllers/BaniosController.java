package PTC._5.Controllers;

import PTC._5.Exceptions.BaniosException.NoData;
import PTC._5.Models.ApiResponse.ApiResponse;
import PTC._5.Models.DTO.BaniosDTO;
import PTC._5.Models.DTO.HabitacionesDTO;
import PTC._5.Services.BaniosService;
import PTC._5.Services.HabitacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


// Anotación que indica que esta clase es un controlador REST
@RestController
// Ruta base del controlador: http://localhost:8080/Banio
@RequestMapping("/Banio")

/*Permite ingreso de otros dominios o puertos como html
* si usaramos live server seria de poner en ves de origin = "*" seria de poner
* http://localhost:5500 en las comillas solo si este viene de live server
* ps: Recordar que esto del * es apra que venga de cualqueir perto no solo de 5500
* o 8080 ya que de este modo es menos seguro pero por falta de conocimientos lo dejare asi(por el momento)
*
* EN RESUMEN: Esto de @CrossOrigin permite solicitudes de cualquier dominio o puerto
* evitando su bloqueo*/
@CrossOrigin
public class BaniosController
{

    // Inyección del servicio de baños para acceder a la lógica de negocio
    @Autowired
    private BaniosService Ban;

    /**
     * Endpoint GET para obtener todos los tipos de baños.
     * URL completa: http://localhost:8080/Banio/Mostrar
     * Lista de BaniosDTO con los tipos de baños disponibles.
     */
    @GetMapping("/Mostrar")
    public ResponseEntity<ApiResponse<List<BaniosDTO>>>  obtnerTipos()
    {
        /*Indicamos bloque de intento*/
        try
        {
            /*Obtenemos objeto de lista*/
        List<BaniosDTO> data = Ban.obtenerTodos();
        /*Estandarizamso respuesta con ApiResponse*/
        ApiResponse<List<BaniosDTO>> response = new ApiResponse<>(
                true,
                "Datos de baños obtenidos",
                data
        );
        return ResponseEntity.ok(response);
        }
        catch (NoData e)
        {
            ApiResponse<List<BaniosDTO>> response = new ApiResponse<>(
                    false,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(404).body(response);
        }
        catch (Exception e)
        {
            ApiResponse<List<BaniosDTO>> response = new ApiResponse<>(
                    false,
                    "Error inesperado en la carga",
                    null
            );
            return ResponseEntity.status(500).body(response);
        }
    }
}
