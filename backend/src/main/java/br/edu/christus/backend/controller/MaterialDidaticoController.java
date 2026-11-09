package br.edu.christus.backend.controller;

import java.util.List;
import br.edu.christus.backend.domain.dto.MaterialDidaticoDTO;
import br.edu.christus.backend.domain.dto.MaterialDidaticoLowDTO;
import br.edu.christus.backend.domain.model.MaterialDidatico;
import br.edu.christus.backend.service.MaterialDidaticoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Disponibiliza as operações do CRUD de Materiais Didáticos.
@RestController
@RequestMapping("/api/v1/materiais-didaticos")
public class MaterialDidaticoController {

    @Autowired
    private MaterialDidaticoService service;

    @Operation(summary = "Cadastra um material didático", tags = "Material Didático")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Material cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialDidaticoLowDTO create(@RequestBody MaterialDidaticoDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Lista todos os materiais didáticos", tags = "Material Didático")
    @ApiResponse(responseCode = "200", description = "Materiais retornados com sucesso")
    @GetMapping
    public List<MaterialDidatico> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Busca materiais didáticos pelo título", tags = "Material Didático")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Materiais encontrados (lista vazia se nenhum)"),
            @ApiResponse(responseCode = "400", description = "O título para busca é obrigatório")
    })
    // Recebe o título pela URL e encaminha a pesquisa ao Service.
    @GetMapping("/buscar")
    public List<MaterialDidatico> buscarPorTitulo(@RequestParam(required = false) String titulo) {
        return service.buscarPorTitulo(titulo);
    }

    @Operation(summary = "Consulta um material didático pelo ID", tags = "Material Didático")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material encontrado"),
            @ApiResponse(responseCode = "404", description = "Material didático não encontrado")
    })
    @GetMapping("/{id}")
    public MaterialDidatico findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Edita os dados de um material didático", tags = "Material Didático")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Material ou categoria não encontrados")
    })
    @PutMapping("/{id}")
    public MaterialDidatico update(@PathVariable Long id, @RequestBody MaterialDidaticoDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Exclui um material didático", tags = "Material Didático")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Material excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Material didático não encontrado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
