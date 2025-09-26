package PTC._5.Services;

import PTC._5.Entities.BaniosEntity;
import PTC._5.Exceptions.BaniosException.NoData;
import PTC._5.Models.DTO.BaniosDTO;
import PTC._5.Repositores.BaniosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Anotación que indica que esta clase es un servicio de Spring
public class BaniosService
{
    // Inyección del repositorio de baños para acceder a la base de datos
    @Autowired
    BaniosRepository banrepo;

    /**
     * Método para obtener todos los registros de baños en formato DTO
     * Lista de BaniosDTO con todos los registros encontrados
     */
    public List<BaniosDTO> obtenerTodos()
    {
        // Se obtiene la lista de entidades desde la base de datos
        List<BaniosEntity> list = banrepo.findAll();

        if(list.isEmpty())
        {
            throw new NoData("No se encontro cantidad de baños");
        }
        // Se convierte la lista de entidades a DTO usando programación funcional (Streams)
        return list.stream()
                .map(this::convertirDTO) // Se aplica la conversión a cada elemento
                .collect(Collectors.toList()); // Se recopilan los resultados en una lista
    }

    /**
     * Método auxiliar para convertir una entidad BaniosEntity a su equivalente BaniosDTO
     *  entity Entidad de baño
     * DTO equivalente
     */
    public BaniosDTO convertirDTO(BaniosEntity entity)
    {
        // Se crea un nuevo objeto DTO y se le asignan los valores de la entidad
        BaniosDTO dto = new BaniosDTO();
        dto.setIdbanio(entity.getIDBanios()); // ID del baño
        dto.setBanio(entity.getBanios());     // Cantidad o descripción del baño

        // Se retorna el DTO construido
        return dto;
    }
}
