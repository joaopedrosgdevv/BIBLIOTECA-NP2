package br.edu.christus.backend.controller;

import java.util.List;
import br.edu.christus.backend.domain.dto.EditoraDTO;
import br.edu.christus.backend.domain.dto.EditoraLowDTO;
import br.edu.christus.backend.domain.model.Editora;
import br.edu.christus.backend.service.EditoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/editoras")
public class EditoraController {

    @Autowired
    private EditoraService service;

    @Operation(summary = "Cadastra uma editora", tags = "Editora")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Editora cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Já existe uma editora com esse CNPJ")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EditoraLowDTO create(@RequestBody EditoraDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Lista todas as editoras", tags = "Editora")
    @ApiResponse(responseCode = "200", description = "Editoras retornadas com sucesso")
    @GetMapping
    public List<Editora> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Consulta uma editora pelo ID", tags = "Editora")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Editora encontrada"),
            @ApiResponse(responseCode = "404", description = "Editora não encontrada")
    })
    @GetMapping("/{id}")
    public Editora findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Edita os dados de uma editora", tags = "Editora")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Editora atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Editora não encontrada"),
            @ApiResponse(responseCode = "409", description = "Já existe outra editora com esse CNPJ")
    })
    @PutMapping("/{id}")
    public Editora update(@PathVariable Long id, @RequestBody EditoraDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Exclui uma editora", tags = "Editora")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Editora excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Editora não encontrada"),
            @ApiResponse(responseCode = "409", description = "Existem livros vinculados a essa editora")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
