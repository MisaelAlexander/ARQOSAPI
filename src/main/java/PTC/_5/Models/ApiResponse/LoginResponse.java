package PTC._5.Models.ApiResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Opcional: excluye campos nulos
public class LoginResponse
{
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    // Constructor vacío (necesario para Jackson)
    public LoginResponse() {
    }

    // Constructor con parámetros
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
