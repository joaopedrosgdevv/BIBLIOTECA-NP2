package br.edu.christus.backend.service;

import java.util.List;
import br.edu.christus.backend.domain.model.Categoria;
import br.edu.christus.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public Categoria create(Categoria categoria) {
        return repository.save(categoria);
    }

    public List<Categoria> findAll() {
        return repository.findAll();
    }

    public Categoria findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada"
                ));
    }

    public Categoria update(Long id, Categoria dados) {
        Categoria categoria = findById(id);

        categoria.setNome(dados.getNome());
        categoria.setDescricao(dados.getDescricao());
        categoria.setCodigo(dados.getCodigo());
        categoria.setAreaConhecimento(dados.getAreaConhecimento());
        categoria.setAtivo(dados.getAtivo());

        return repository.save(categoria);
    }

    public void delete(Long id) {
        Categoria categoria = findById(id);
        repository.delete(categoria);
    }
}