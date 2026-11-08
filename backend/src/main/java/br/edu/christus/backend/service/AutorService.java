package br.edu.christus.backend.service;

import java.time.Year;
import java.util.List;

import br.edu.christus.backend.domain.dto.AutorDTO;
import br.edu.christus.backend.domain.dto.AutorLowDTO;
import br.edu.christus.backend.domain.model.Autor;
import br.edu.christus.backend.repository.AutorRepository;
import br.edu.christus.backend.repository.LivroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AutorService {

    @Autowired
    private AutorRepository repository;

    @Autowired
    private LivroRepository livroRepository;

    // CADASTRAR AUTOR
    public AutorLowDTO create(AutorDTO dados) {

        validarAutor(dados);

        // IMPEDE CADASTRAR O MESMO AUTOR NOVAMENTE
        if (repository.existsByNomeIgnoreCaseAndAnoNascimento(
                dados.getNome().trim(),
                dados.getAnoNascimento())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um autor cadastrado com esse nome e ano de nascimento"
            );
        }

        Autor autor = new Autor();

        autor.setNome(dados.getNome().trim());
        autor.setNacionalidade(dados.getNacionalidade().trim());
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

        // IMPEDE ATUALIZAR PARA UM AUTOR JÁ CADASTRADO
        if (repository.existsByNomeIgnoreCaseAndAnoNascimentoAndIdNot(
                dados.getNome().trim(),
                dados.getAnoNascimento(),
                id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe outro autor com esse nome e ano de nascimento"
            );
        }

        autor.setNome(dados.getNome().trim());
        autor.setNacionalidade(dados.getNacionalidade().trim());
        autor.setAnoNascimento(dados.getAnoNascimento());
        autor.setBiografia(dados.getBiografia());
        autor.setAtivo(dados.getAtivo());

        return repository.save(autor);
    }

    // EXCLUIR AUTOR
    public void delete(Long id) {

        Autor autor = findById(id);

        // IMPEDE EXCLUIR AUTORES ASSOCIADOS A LIVROS
        if (livroRepository.existsByAutores_Id(id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir um autor associado a livros"
            );
        }

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

        if (dados.getNome().trim().length() > 150) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome do autor não pode ultrapassar 150 caracteres"
            );
        }

        if (dados.getNacionalidade() == null ||
                dados.getNacionalidade().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nacionalidade é obrigatória"
            );
        }

        if (dados.getNacionalidade().trim().length() > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nacionalidade não pode ultrapassar 100 caracteres"
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

        if (dados.getBiografia() != null &&
                dados.getBiografia().length() > 1000) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Biografia não pode ultrapassar 1000 caracteres"
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