
package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {

}
