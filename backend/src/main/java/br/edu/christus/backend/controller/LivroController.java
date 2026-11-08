
package br.edu.christus.backend.controller;

import java.util.List;
import br.edu.christus.backend.domain.dto.LivroDTO;
import br.edu.christus.backend.domain.dto.LivroLowDTO;
import br.edu.christus.backend.domain.model.Livro;
import br.edu.christus.backend.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/livros")
public class LivroController {

    @Autowired
    private LivroService service;


    @PostMapping
    public LivroLowDTO create(@RequestBody LivroDTO dto) {
        return service.create(dto);
    }


    @GetMapping
    public List<Livro> findAll() {
        return service.findAll();
    }


    @GetMapping("/{id}")
    public Livro findById(@PathVariable Long id) {
        return service.findById(id);
    }


    @PutMapping("/{id}")
    public Livro update(@PathVariable Long id, @RequestBody LivroDTO dto) {
        return service.update(id, dto);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
