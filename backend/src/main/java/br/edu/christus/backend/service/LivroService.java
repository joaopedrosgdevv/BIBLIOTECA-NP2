
package br.edu.christus.backend.service;

import java.util.List;
import br.edu.christus.backend.domain.dto.LivroDTO;
import br.edu.christus.backend.domain.dto.LivroLowDTO;
import br.edu.christus.backend.domain.model.Categoria;
import br.edu.christus.backend.domain.model.Livro;
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


    public LivroLowDTO create(LivroDTO dados) {

        if (dados.getCategoriaId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A categoria é obrigatória"
            );
        }

        Categoria categoria = categoriaRepository
                .findById(dados.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada"
                ));

        if (repository.existsByIsbn(dados.getIsbn())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um livro com esse ISBN"
            );
        }

        Livro livro = new Livro();
        livro.setTitulo(dados.getTitulo());
        livro.setIsbn(dados.getIsbn());
        livro.setAnoPublicacao(dados.getAnoPublicacao());
        livro.setQuantidadeDisponivel(dados.getQuantidadeDisponivel());
        livro.setCategoria(categoria);

        Livro livroSalvo = repository.save(livro);

        LivroLowDTO resposta = new LivroLowDTO();
        resposta.setId(livroSalvo.getId());
        resposta.setTitulo(livroSalvo.getTitulo());
        resposta.setIsbn(livroSalvo.getIsbn());
        resposta.setCategoriaId(categoria.getId());

        return resposta;
    }


    public List<Livro> findAll() {
        return repository.findAll();
    }

    public Livro findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Livro não encontrado"
                ));
    }



    public Livro update(Long id, LivroDTO dados) {

        Livro livro = findById(id);

        if (dados.getCategoriaId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A categoria é obrigatória"
            );
        }

        if (repository.existsByIsbnAndIdNot(dados.getIsbn(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe outro livro com esse ISBN"
            );
        }

        Categoria categoria = categoriaRepository
                .findById(dados.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada"
                ));

        livro.setTitulo(dados.getTitulo());
        livro.setIsbn(dados.getIsbn());
        livro.setAnoPublicacao(dados.getAnoPublicacao());
        livro.setQuantidadeDisponivel(dados.getQuantidadeDisponivel());
        livro.setCategoria(categoria);

        return repository.save(livro);
    }


    public void delete(Long id) {
        Livro livro = findById(id);
        repository.delete(livro);
    }



}
