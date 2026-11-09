package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.Editora;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditoraRepository extends JpaRepository<Editora, Long> {

    // Verifica se o CNPJ já está cadastrado.
    boolean existsByCnpj(String cnpj);

    // Na edição, verifica o CNPJ desconsiderando a própria editora.
    boolean existsByCnpjAndIdNot(String cnpj, Long id);

}
