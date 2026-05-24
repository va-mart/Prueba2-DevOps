package Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity //Esto es lo que hace lo que se guarde ne la base de datos, responde como entidad
@Table(name= "clientes") //Persistencia

@Data //Data para hacer que no se repita objetos en el codigo, y genere getter and setter
@AllArgsConstructor //  Es constructor con argumentos ordenado
@NoArgsConstructor // Es constructor sin argumentos, es requerido por JPA

public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Es un valor autoincremental que se genera solo.
    private long id; // Guardar el id del cliente

    @Column(name= "nombre")private String nombre; // Guardar el nombre del cliente
    @Column(name= "rut")private String rut; // Guardar el rut del cliente
    @Column(name= "telefono")private String telefono; // Guardar el telefono del cliente
    @Column(name= "email")private String email; // Guardar el email del cliente
    @Column(name= "idMascota")private String idMascota; // Guardar el id de la Mascota del cliente
    @Column(name= "direccion")private String direccion; // Guardar la direccion del cliente
    @Column(name= "fechaDeIngreso") private LocalDate fechaDeIngreso; // Guardar el ingreso de la mascota del cliente
    @Column(name= "fechaDeSalida") private LocalDate fechaDeSalida; // Guardar la salida de la mascota del cliente

}//fin clase Cliente
