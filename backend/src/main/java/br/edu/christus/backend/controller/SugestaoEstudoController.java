package br.edu.christus.backend.controller;

import br.edu.christus.backend.domain.dto.SugestaoEstudoDTO;
import br.edu.christus.backend.service.SugestaoEstudoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// FUNCIONALIDADE EXTRA: sugestão de material didático para estudo.
// Tem rota e grupo próprios no Swagger, separados do CRUD de Material Didático.
@RestController
@RequestMapping("/api/v1/sugestoes-estudo")
@Tag(name = SugestaoEstudoController.TAG)
public class SugestaoEstudoController {

    // Nome do grupo no Swagger (a descrição fica em BackendApplication).
    public static final String TAG = "⭐ Funcionalidade Extra - Sugestão de Estudo";

    @Autowired
    private SugestaoEstudoService service;

    @Operation(
            summary = "Sugere um material didático para estudo",
            description = "Sorteia um material didático cadastrado. "
                    + "Informe o categoriaId para sortear apenas entre os materiais daquela categoria."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material sugerido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada ou nenhum material disponível")
    })
    @GetMapping
    public SugestaoEstudoDTO sugerir(
            @Parameter(description = "ID da categoria para filtrar a sugestão (opcional)", example = "1")
            @RequestParam(required = false) Long categoriaId) {
        return service.sugerir(categoriaId);
    }
}
