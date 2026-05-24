package Veterinaria.Cliente.Service;

import Veterinaria.Cliente.DTO.ClienteRequest;
import Veterinaria.Cliente.Exception.ClienteNoEncontradoException;
import Veterinaria.Cliente.Model.Cliente;
import Veterinaria.Cliente.Repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    // Retorna todos los clientes registrados
    public List<Cliente> listarTodos() {
        log.info("Listando todos los clientes");
        List<Cliente> clientes = clienteRepository.findAll();
        log.info("Total de clientes encontrados: {}", clientes.size());
        return clientes;
    }

    // Busca un cliente por su ID
    public Cliente buscarPorId(Long id) {
        log.info("Buscando cliente con id: {}", id);
        return clienteRepository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró cliente con id: {}", id);
            return new ClienteNoEncontradoException("Cliente no encontrado con id: " + id);
        });
    }

    // Busca un cliente por su RUT
    public Cliente buscarPorRut(String rut) {
        log.info("Buscando cliente con RUT: {}", rut);
        return clienteRepository.findByRut(rut).orElseThrow(() -> {
            log.warn("No se encontró cliente con RUT: {}", rut);
            return new ClienteNoEncontradoException("Cliente no encontrado con RUT: " + rut);
        });
    }

    // Guarda un nuevo cliente mapeando desde el DTO
    public Cliente guardar(ClienteRequest request) {
        log.info("Guardando nuevo cliente con RUT: {}", request.getRut());

        // Validar que el RUT no esté duplicado
        if (clienteRepository.existsByRut(request.getRut())) {
            log.warn("Ya existe un cliente con RUT: {}", request.getRut());
            throw new IllegalArgumentException("Ya existe un cliente con el RUT: " + request.getRut());
        }

        // Validar que el email no esté duplicado
        if (clienteRepository.existsByEmail(request.getEmail())) {
            log.warn("Ya existe un cliente con email: {}", request.getEmail());
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + request.getEmail());
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setRut(request.getRut());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setDireccion(request.getDireccion());
        cliente.setFechaRegistro(request.getFechaRegistro());

        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente guardado correctamente con id: {}", guardado.getId());
        return guardado;
    }

    // Actualiza los datos de un cliente existente
    public Cliente actualizar(Long id, ClienteRequest request) {
        log.info("Actualizando cliente con id: {}", id);

        Cliente existente = buscarPorId(id);

        existente.setNombre(request.getNombre());
        existente.setTelefono(request.getTelefono());
        existente.setEmail(request.getEmail());
        existente.setDireccion(request.getDireccion());
        existente.setFechaRegistro(request.getFechaRegistro());

        Cliente actualizado = clienteRepository.save(existente);
        log.info("Cliente actualizado correctamente con id: {}", id);
        return actualizado;
    }

    // Elimina un cliente por su ID
    public void eliminar(Long id) {
        log.info("Eliminando cliente con id: {}", id);

        if (!clienteRepository.existsById(id)) {
            log.warn("No fue posible eliminar: no existe el cliente con id: {}", id);
            throw new ClienteNoEncontradoException("Cliente no encontrado con id: " + id);
        }

        clienteRepository.deleteById(id);
        log.info("Cliente eliminado correctamente con id: {}", id);
    }
}
