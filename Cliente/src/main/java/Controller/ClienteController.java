package Controller;

import Model.Cliente;
import Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController //Respuesta de datos, permite trabajar y enviar datos en la web
@RequestMapping("/api/v1/clientes") //Ruta base para todos los endpoints de prestamos
public class ClienteController {

    @Autowired //Inyecta automaticamente el servicio de cliente
    private ClienteRepository clienteRepository; //Variable para usar los metodos del Cliente

    @PostMapping //Este metodo responde a POST /api/v1/cliente
    public Cliente guardarCliente(@RequestBody Cliente cliente) {  //Mtdo para guardar Cliente
     return clienteRepository.save(cliente);
    }



}
