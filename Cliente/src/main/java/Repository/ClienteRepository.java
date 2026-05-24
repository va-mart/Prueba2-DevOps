package Repository;

import Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Indica que esta clase pertenece a Repository,
// esta hace que se comunique con la base de datos
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}//fin de cliente Repository
