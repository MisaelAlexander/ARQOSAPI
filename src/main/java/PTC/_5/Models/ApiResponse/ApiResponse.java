package PTC._5.Models.ApiResponse;

import lombok.Getter;
import lombok.Setter;

/*Estandrizamos respeustas de la api*/
public class ApiResponse<T>
{
    /*Captura*/
    @Getter @Setter
    private boolean status;

    @Getter@Setter
    private String message;

    @Getter@Setter
    private T data;

    public ApiResponse(boolean status,String message, T data)
    {
this.status = status;
this. message = message;
this.data = data;
    }
}
