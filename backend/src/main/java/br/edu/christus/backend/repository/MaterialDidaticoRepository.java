package br.edu.christus.backend.repository;

import java.util.List;
import br.edu.christus.backend.domain.model.MaterialDidatico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialDidaticoRepository extends JpaRepository<MaterialDidatico, Long> {

    // Busca por parte do título, ignorando maiúsculas e minúsculas.
    List<MaterialDidatico> findByTituloContainingIgnoreCase(String titulo);

    // FUNCIONALIDADE EXTRA (sugestão de estudo): materiais de uma categoria.
    List<MaterialDidatico> findByCategoriaId(Long categoriaId);

}
