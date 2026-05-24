package DTO;

import jakarta.validation.constraints.*; //Para que esto funcione hay que agregar dependencias en el pom y recargar el maven
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequest {

    private Long id;   // Solo se usa al responder; al crear viene null.

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(
            regexp = "^[0-9]{7,8}-[0-9Kk]$", //Condicion para el rut
            message = "El RUT debe tener formato 12345678-9") //el msje
    private String rut;

    @NotBlank(message= "El telefono es obligatorio")
    @Pattern(
            regexp = "^[+0-9]{8,15}$",//Condicion para el telefono
            message = "Teléfono inválido (8 a 15 dígitos, opcionalmente con +)"
    ) private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")//Condicion para el mail
    private String email;

    @NotBlank(message = "La id de la Mascota es obligatoria")
    @Pattern(
            regexp = "^[A-Z]{3}-[0-9]{3}$",//Condicion para el idDeMascota
            message = "La id de la Mascota debe tener formato AAA-999"
    )
    private String idMascota;

    @NotBlank(message = "La direccion del cliente es obligatoria")
    @Size(min= 20, max= 100, message= "La direccion tiene que tener entre 20 a 100 caracteres") //Condicion para direccion
    private String direccion;

    @NotBlank(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaDeIngreso;

    @NotBlank(message = "La fecha de salida es obligatoria")
    private LocalDate fechaDeSalida;

}
