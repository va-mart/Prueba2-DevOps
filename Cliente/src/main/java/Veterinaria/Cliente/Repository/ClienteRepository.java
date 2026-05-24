package Veterinaria.Cliente.Repository;

import Veterinaria.Cliente.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por RUT
    Optional<Cliente> findByRut(String rut);

    // Buscar cliente por email
    Optional<Cliente> findByEmail(String email);

    // Verificar si existe un RUT
    boolean existsByRut(String rut);

    // Verificar si existe un email
    boolean existsByEmail(String email);
}
