package br.edu.christus.backend.controller;

import java.util.List;
import br.edu.christus.backend.domain.model.Categoria;
import br.edu.christus.backend.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @PostMapping
    public Categoria create(@RequestBody Categoria categoria) {
        return service.create(categoria);
    }

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


}