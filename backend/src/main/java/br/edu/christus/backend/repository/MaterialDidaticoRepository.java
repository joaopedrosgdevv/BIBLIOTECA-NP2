package br.edu.christus.backend.repository;

import java.util.List;
import br.edu.christus.backend.domain.model.MaterialDidatico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialDidaticoRepository extends JpaRepository<MaterialDidatico, Long> {

    List<MaterialDidatico> findByTituloContainingIgnoreCase(String titulo);

}
