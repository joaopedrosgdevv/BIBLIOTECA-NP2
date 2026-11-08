
package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {

}
