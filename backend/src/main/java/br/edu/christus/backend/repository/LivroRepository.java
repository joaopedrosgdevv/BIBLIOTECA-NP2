
package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.Livro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    // Verificar se o ISBN já existe
    boolean existsByIsbn(String isbn);

    // Verificar ISBN duplicado na atualização
    boolean existsByIsbnAndIdNot(String isbn, Long id);

    // Verificar se um autor está associado a algum livro
    boolean existsByAutores_Id(Long autorId);

    // Verificar se uma editora está associada a algum livro
    boolean existsByEditoraId(Long editoraId);

    // Listar livros carregando os relacionamentos
    @Override
    @EntityGraph(attributePaths = {"categoria", "autores", "editora"})
    List<Livro> findAll();

    // Buscar livro por ID carregando os relacionamentos
    @Override
    @EntityGraph(attributePaths = {"categoria", "autores", "editora"})
    Optional<Livro> findById(Long id);
}
