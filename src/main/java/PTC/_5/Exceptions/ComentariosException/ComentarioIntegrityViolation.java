package PTC._5.Exceptions.ComentariosException;

import org.springframework.dao.DataIntegrityViolationException;

public class ComentarioIntegrityViolation extends RuntimeException {
    public ComentarioIntegrityViolation(String message, DataIntegrityViolationException e) {
        super(message);
    }
}
