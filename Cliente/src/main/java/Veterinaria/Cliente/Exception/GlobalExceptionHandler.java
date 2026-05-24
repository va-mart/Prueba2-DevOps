package Veterinaria.Cliente.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Maneja cliente no encontrado
    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarClienteNoEncontrado(ClienteNoEncontradoException ex,
                                                                     HttpServletRequest request) {
        log.warn("Cliente no encontrado. Ruta: {} - Mensaje: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse respuesta = new ErrorResponse();
        respuesta.setFecha(LocalDateTime.now());
        respuesta.setEstado(HttpStatus.NOT_FOUND.value());
        respuesta.setError("Not Found");
        respuesta.setMensaje(ex.getMessage());
        respuesta.setRuta(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    // Maneja errores de validación Bean Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex,
                                                            HttpServletRequest request) {
        log.warn("Error de validación en ruta: {}", request.getRequestURI());

        Map<String, String> errores = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errores.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse respuesta = new ErrorResponse();
        respuesta.setFecha(LocalDateTime.now());
        respuesta.setEstado(HttpStatus.BAD_REQUEST.value());
        respuesta.setError("Validation Error");
        respuesta.setMensaje("Hay errores de validación en la solicitud");
        respuesta.setRuta(request.getRequestURI());
        respuesta.setErrores(errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // Maneja errores generales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarExcepcionGeneral(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado en ruta: {} - {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse respuesta = new ErrorResponse();
        respuesta.setFecha(LocalDateTime.now());
        respuesta.setEstado(HttpStatus.INTERNAL_SERVER_ERROR.value());
        respuesta.setError("Internal Server Error");
        respuesta.setMensaje("Ocurrió un error inesperado en el servidor");
        respuesta.setRuta(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}
