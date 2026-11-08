package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    // VERIFICAR AUTOR DUPLICADO NO CADASTRO
    boolean existsByNomeIgnoreCaseAndAnoNascimento(
            String nome,
            Integer anoNascimento
    );

    // VERIFICAR AUTOR DUPLICADO NA ATUALIZAÇÃO
    boolean existsByNomeIgnoreCaseAndAnoNascimentoAndIdNot(
            String nome,
            Integer anoNascimento,
            Long id
    );
}