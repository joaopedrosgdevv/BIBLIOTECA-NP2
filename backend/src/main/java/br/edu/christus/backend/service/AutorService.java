
package br.edu.christus.backend.service;

import java.time.Year;
import java.util.List;

import br.edu.christus.backend.domain.dto.AutorDTO;
import br.edu.christus.backend.domain.dto.AutorLowDTO;
import br.edu.christus.backend.domain.model.Autor;
import br.edu.christus.backend.repository.AutorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AutorService {

    @Autowired
    private AutorRepository repository;

    // CADASTRAR AUTOR
    public AutorLowDTO create(AutorDTO dados) {

        validarAutor(dados);

        Autor autor = new Autor();

        autor.setNome(dados.getNome());
        autor.setNacionalidade(dados.getNacionalidade());
        autor.setAnoNascimento(dados.getAnoNascimento());
        autor.setBiografia(dados.getBiografia());
        autor.setAtivo(dados.getAtivo());

        Autor autorSalvo = repository.save(autor);

        AutorLowDTO resposta = new AutorLowDTO();
        resposta.setId(autorSalvo.getId());
        resposta.setNome(autorSalvo.getNome());
        resposta.setNacionalidade(autorSalvo.getNacionalidade());

        return resposta;
    }

    // LISTAR TODOS OS AUTORES
    public List<Autor> findAll() {
        return repository.findAll();
    }

    // BUSCAR AUTOR POR ID
    public Autor findById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado"
                ));
    }

    // ATUALIZAR AUTOR
    public Autor update(Long id, AutorDTO dados) {

        Autor autor = findById(id);

        validarAutor(dados);

        autor.setNome(dados.getNome());
        autor.setNacionalidade(dados.getNacionalidade());
        autor.setAnoNascimento(dados.getAnoNascimento());
        autor.setBiografia(dados.getBiografia());
        autor.setAtivo(dados.getAtivo());

        return repository.save(autor);
    }

    // EXCLUIR AUTOR
    public void delete(Long id) {

        Autor autor = findById(id);

        repository.delete(autor);
    }

    // VALIDAR DADOS DO AUTOR
    private void validarAutor(AutorDTO dados) {

        if (dados == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Dados do autor são obrigatórios"
            );
        }

        if (dados.getNome() == null ||
                dados.getNome().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome do autor é obrigatório"
            );
        }

        if (dados.getNacionalidade() == null ||
                dados.getNacionalidade().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nacionalidade é obrigatória"
            );
        }

        if (dados.getAnoNascimento() == null ||
                dados.getAnoNascimento() < 1 ||
                dados.getAnoNascimento() > Year.now().getValue()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ano de nascimento inválido"
            );
        }

        if (dados.getAtivo() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status ativo é obrigatório"
            );
        }
    }
}
