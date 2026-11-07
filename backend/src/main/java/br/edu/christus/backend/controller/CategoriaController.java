package br.edu.christus.backend.controller;

import java.util.List;
import br.edu.christus.backend.domain.dto.CategoriaLowDTO;
import br.edu.christus.backend.domain.dto.CategoriaDTO;
import br.edu.christus.backend.domain.model.Categoria;
import br.edu.christus.backend.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public List<Categoria> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Categoria findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Categoria update(
            @PathVariable Long id,
            @RequestBody Categoria dados) {

        return service.update(id, dados);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping
    public CategoriaLowDTO create(@RequestBody CategoriaDTO dto) {
        return service.create(dto);
    }


}