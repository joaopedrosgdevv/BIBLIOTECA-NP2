
package br.edu.christus.backend.service;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.edu.christus.backend.domain.dto.LivroDTO;
import br.edu.christus.backend.domain.dto.LivroLowDTO;
import br.edu.christus.backend.domain.model.Autor;
import br.edu.christus.backend.domain.model.Categoria;
import br.edu.christus.backend.domain.model.Editora;
import br.edu.christus.backend.domain.model.Livro;
import br.edu.christus.backend.repository.AutorRepository;
import br.edu.christus.backend.repository.CategoriaRepository;
import br.edu.christus.backend.repository.LivroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private EditoraService editoraService;

    // CADASTRAR LIVRO
    public LivroLowDTO create(LivroDTO dados) {

        validarLivro(dados);

        Categoria categoria = buscarCategoria(dados.getCategoriaId());

        Set<Autor> autores = buscarAutores(dados.getAutoresIds());

        if (repository.existsByIsbn(dados.getIsbn())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um livro com esse ISBN"
            );
        }

        Editora editora = null;
        if (dados.getEditoraId() != null) {
            editora = editoraService.findById(dados.getEditoraId());
        }

        Livro livro = new Livro();

        livro.setTitulo(dados.getTitulo());
        livro.setIsbn(dados.getIsbn());
        livro.setAnoPublicacao(dados.getAnoPublicacao());
        livro.setQuantidadeDisponivel(dados.getQuantidadeDisponivel());
        livro.setCategoria(categoria);
        livro.setAutores(autores);
        livro.setEditora(editora);

        Livro livroSalvo = repository.save(livro);

        LivroLowDTO resposta = new LivroLowDTO();

        resposta.setId(livroSalvo.getId());
        resposta.setTitulo(livroSalvo.getTitulo());
        resposta.setIsbn(livroSalvo.getIsbn());
        resposta.setCategoriaId(categoria.getId());

        return resposta;
    }

    // LISTAR TODOS OS LIVROS
    public List<Livro> findAll() {
        return repository.findAll();
    }

    // BUSCAR LIVRO PELO ID
    public Livro findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Livro não encontrado"
                ));
    }

    // ATUALIZAR LIVRO
    public Livro update(Long id, LivroDTO dados) {

        Livro livro = findById(id);

        validarLivro(dados);

        if (repository.existsByIsbnAndIdNot(dados.getIsbn(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe outro livro com esse ISBN"
            );
        }

        Categoria categoria = buscarCategoria(dados.getCategoriaId());

        Set<Autor> autores = buscarAutores(dados.getAutoresIds());

        Editora editora = null;
        if (dados.getEditoraId() != null) {
            editora = editoraService.findById(dados.getEditoraId());
        }

        livro.setTitulo(dados.getTitulo());
        livro.setIsbn(dados.getIsbn());
        livro.setAnoPublicacao(dados.getAnoPublicacao());
        livro.setQuantidadeDisponivel(dados.getQuantidadeDisponivel());
        livro.setCategoria(categoria);
        livro.setAutores(autores);
        livro.setEditora(editora);

        return repository.save(livro);
    }

    // EXCLUIR LIVRO
    public void delete(Long id) {

        Livro livro = findById(id);

        repository.delete(livro);
    }

    // BUSCAR E VALIDAR CATEGORIA
    private Categoria buscarCategoria(Long categoriaId) {

        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada"
                ));
    }

    // BUSCAR E VALIDAR AUTORES
    private Set<Autor> buscarAutores(Set<Long> autoresIds) {

        Set<Autor> autores = new HashSet<>();

        for (Long autorId : autoresIds) {

            if (autorId == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "O ID do autor não pode ser nulo"
                );
            }

            Autor autor = autorRepository.findById(autorId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Autor não encontrado: " + autorId
                    ));

            autores.add(autor);
        }

        return autores;
    }

    // VALIDAR DADOS DO LIVRO
    private void validarLivro(LivroDTO dados) {

        if (dados == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Dados do livro são obrigatórios"
            );
        }

        if (dados.getTitulo() == null ||
                dados.getTitulo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Título do livro é obrigatório"
            );
        }

        if (dados.getIsbn() == null ||
                dados.getIsbn().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ISBN é obrigatório"
            );
        }

        if (dados.getAnoPublicacao() == null ||
                dados.getAnoPublicacao() < 1 ||
                dados.getAnoPublicacao() > Year.now().getValue()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ano de publicação inválido"
            );
        }

        if (dados.getQuantidadeDisponivel() == null ||
                dados.getQuantidadeDisponivel() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Quantidade disponível inválida"
            );
        }

        if (dados.getCategoriaId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A categoria é obrigatória"
            );
        }

        if (dados.getAutoresIds() == null ||
                dados.getAutoresIds().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O livro deve possuir pelo menos um autor"
            );
        }
    }
}
