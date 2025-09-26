package PTC._5.Utils;

import java.time.LocalDateTime;

//Los datos de aca se mandan de la linea 28
public class PinDato
{
    //Aqui captura los dos elementos enviados
    private String pin; /*Se guarda el pin*/
    private LocalDateTime expirationTime; /*Establece una fecah de experiacion de 5 minutps*/

    /*Permite creacion de objeto pin data*/
    public PinDato(String pin, LocalDateTime expirationTime) {
        this.pin = pin;
        this.expirationTime = expirationTime;
    }

    //Devuele valor guardado
    public String getPin()
    {
        return pin;
    }

    /*Devuelve true si es valido y else si no*/
    public boolean isExpired()
    {
        /*Toma la hora o fecha actual y cuenta que el tiempo despues */
        return LocalDateTime.now().isAfter(expirationTime);
    }
}