package Veterinaria.Cliente.Controller;

import Veterinaria.Cliente.DTO.ClienteRequest;
import Veterinaria.Cliente.Model.Cliente;
import Veterinaria.Cliente.Service.ClienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private ClienteService clienteService;

    // GET /api/v1/clientes - Lista todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        log.info("GET /api/v1/clientes - Listando todos los clientes");
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    // GET /api/v1/clientes/{id} - Busca un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/clientes/{} - Buscando cliente", id);
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    // GET /api/v1/clientes/rut/{rut} - Busca un cliente por RUT
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Cliente> buscarPorRut(@PathVariable String rut) {
        log.info("GET /api/v1/clientes/rut/{} - Buscando cliente por RUT", rut);
        Cliente cliente = clienteService.buscarPorRut(rut);
        return ResponseEntity.ok(cliente);
    }

    // POST /api/v1/clientes - Crea un nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> guardar(@Valid @RequestBody ClienteRequest request) {
        log.info("POST /api/v1/clientes - Creando nuevo cliente con RUT: {}", request.getRut());
        Cliente guardado = clienteService.guardar(request);
        return new ResponseEntity<>(guardado, HttpStatus.CREATED);
    }

    // PUT /api/v1/clientes/{id} - Actualiza un cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody ClienteRequest request) {
        log.info("PUT /api/v1/clientes/{} - Actualizando cliente", id);
        Cliente actualizado = clienteService.actualizar(id, request);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/v1/clientes/{id} - Elimina un cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/clientes/{} - Eliminando cliente", id);
        clienteService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Cliente eliminado correctamente"));
    }
}
