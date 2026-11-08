package br.edu.christus.backend.service;

import java.util.List;
import br.edu.christus.backend.domain.dto.CategoriaLowDTO;
import br.edu.christus.backend.domain.dto.CategoriaDTO;
import br.edu.christus.backend.util.MapperUtil;
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

    public CategoriaLowDTO create(CategoriaDTO dto) {

        if (repository.existsByCodigo(dto.getCodigo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma categoria com esse código"
            );
        }

        Categoria categoria = MapperUtil.parseObject(dto, Categoria.class);

        Categoria categoriaSalva = repository.save(categoria);

        return MapperUtil.parseObject(categoriaSalva, CategoriaLowDTO.class);
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

        if (repository.existsByCodigoAndIdNot(dados.getCodigo(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe outra categoria com esse código"
            );
        }

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