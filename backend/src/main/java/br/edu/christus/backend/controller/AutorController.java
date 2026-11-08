
package br.edu.christus.backend.controller;

import br.edu.christus.backend.domain.dto.AutorDTO;
import br.edu.christus.backend.domain.dto.AutorLowDTO;
import br.edu.christus.backend.domain.model.Autor;
import br.edu.christus.backend.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService service;

    // CADASTRAR AUTOR
    @PostMapping
    public ResponseEntity<AutorLowDTO> create(@RequestBody AutorDTO dados) {
        AutorLowDTO autorSalvo = service.create(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(autorSalvo);
    }

    // LISTAR TODOS OS AUTORES
    @GetMapping
    public ResponseEntity<List<Autor>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // BUSCAR AUTOR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Autor> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // ATUALIZAR AUTOR
    @PutMapping("/{id}")
    public ResponseEntity<Autor> update(
            @PathVariable Long id,
            @RequestBody AutorDTO dados) {

        return ResponseEntity.ok(service.update(id, dados));
    }

    // EXCLUIR AUTOR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
