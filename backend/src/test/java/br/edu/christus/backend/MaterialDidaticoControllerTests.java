package br.edu.christus.backend;

import br.edu.christus.backend.domain.model.Categoria;
import br.edu.christus.backend.repository.CategoriaRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MaterialDidaticoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Long categoriaId;

    @BeforeEach
    void criarCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNome("Tecnologia");
        categoria.setCodigo("TEC-" + System.nanoTime());
        categoria.setAreaConhecimento("Computação");
        categoria.setAtivo(true);
        categoriaId = categoriaRepository.save(categoria).getId();
    }

    @Test
    void deveExecutarCrudCompletoDeMaterialDidatico() throws Exception {
        String cadastro = """
                {
                  "titulo": "Introdução ao Spring",
                  "descricao": "Material de apoio",
                  "tipoArquivo": "PDF",
                  "urlArquivo": "https://example.com/spring.pdf",
                  "dataPublicacao": "2025-05-10",
                  "categoriaId": %d
                }
                """.formatted(categoriaId);

        String resposta = mockMvc.perform(post("/api/v1/materiais-didaticos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cadastro))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo").value("Introdução ao Spring"))
                .andExpect(jsonPath("$.categoriaId").value(categoriaId))
                .andReturn().getResponse().getContentAsString();

        Long materialId = ((Number) JsonPath.read(resposta, "$.id")).longValue();

        mockMvc.perform(get("/api/v1/materiais-didaticos/{id}", materialId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoArquivo").value("PDF"));

        String atualizacao = """
                {
                  "titulo": "Spring Atualizado",
                  "descricao": "Nova descrição",
                  "tipoArquivo": "VIDEO",
                  "urlArquivo": "https://example.com/spring.mp4",
                  "dataPublicacao": "2025-06-10",
                  "categoriaId": %d
                }
                """.formatted(categoriaId);

        mockMvc.perform(put("/api/v1/materiais-didaticos/{id}", materialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Spring Atualizado"))
                .andExpect(jsonPath("$.tipoArquivo").value("VIDEO"));

        mockMvc.perform(delete("/api/v1/materiais-didaticos/{id}", materialId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/materiais-didaticos/{id}", materialId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Material didático não encontrado"));
    }

    @Test
    void deveRejeitarUrlInvalida() throws Exception {
        String dadosInvalidos = """
                {
                  "titulo": "Material",
                  "tipoArquivo": "PDF",
                  "urlArquivo": "arquivo-sem-http.pdf",
                  "categoriaId": %d
                }
                """.formatted(categoriaId);

        mockMvc.perform(post("/api/v1/materiais-didaticos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosInvalidos))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("A URL do arquivo deve começar com http:// ou https://"));
    }

    @Test
    void deveRejeitarDataFutura() throws Exception {
        String dadosInvalidos = """
                {
                  "titulo": "Material",
                  "tipoArquivo": "PDF",
                  "urlArquivo": "https://example.com/material.pdf",
                  "dataPublicacao": "2999-01-01",
                  "categoriaId": %d
                }
                """.formatted(categoriaId);

        mockMvc.perform(post("/api/v1/materiais-didaticos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosInvalidos))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("A data de publicação não pode estar no futuro"));
    }

    @Test
    void deveRejeitarCategoriaInexistente() throws Exception {
        String dadosInvalidos = """
                {
                  "titulo": "Material",
                  "tipoArquivo": "PDF",
                  "urlArquivo": "https://example.com/material.pdf",
                  "categoriaId": 999999
                }
                """;

        mockMvc.perform(post("/api/v1/materiais-didaticos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosInvalidos))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
    }
}
