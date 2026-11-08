package br.edu.christus.backend;

import com.jayway.jsonpath.JsonPath;
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
class EditoraControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveExecutarCrudCompletoDeEditora() throws Exception {
        String cadastro = """
                {
                  "nome": "Editora Exemplo",
                  "cnpj": "12.345.678/0001-90",
                  "paisOrigem": "Brasil",
                  "anoFundacao": 2000,
                  "site": "https://editora.example",
                  "descricao": "Editora usada no teste"
                }
                """;

        String resposta = mockMvc.perform(post("/api/v1/editoras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cadastro))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Editora Exemplo"))
                .andExpect(jsonPath("$.cnpj").value("12.345.678/0001-90"))
                .andReturn().getResponse().getContentAsString();

        Long editoraId = ((Number) JsonPath.read(resposta, "$.id")).longValue();

        mockMvc.perform(get("/api/v1/editoras/{id}", editoraId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paisOrigem").value("Brasil"));

        String atualizacao = """
                {
                  "nome": "Editora Atualizada",
                  "cnpj": "12.345.678/0001-90",
                  "paisOrigem": "Portugal",
                  "anoFundacao": 2001,
                  "site": "https://editora.example",
                  "descricao": "Dados atualizados"
                }
                """;

        mockMvc.perform(put("/api/v1/editoras/{id}", editoraId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Editora Atualizada"))
                .andExpect(jsonPath("$.paisOrigem").value("Portugal"));

        mockMvc.perform(delete("/api/v1/editoras/{id}", editoraId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/editoras/{id}", editoraId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Editora não encontrada"));
    }

    @Test
    void deveRejeitarEditoraSemNome() throws Exception {
        String dadosInvalidos = """
                {
                  "nome": " ",
                  "paisOrigem": "Brasil"
                }
                """;

        mockMvc.perform(post("/api/v1/editoras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosInvalidos))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O nome da editora é obrigatório"));
    }

    @Test
    void deveRejeitarCnpjDuplicado() throws Exception {
        String editora = """
                {
                  "nome": "Primeira Editora",
                  "cnpj": "11.111.111/0001-11",
                  "paisOrigem": "Brasil"
                }
                """;

        mockMvc.perform(post("/api/v1/editoras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editora))
                .andExpect(status().isCreated());

        String duplicada = """
                {
                  "nome": "Segunda Editora",
                  "cnpj": "11.111.111/0001-11",
                  "paisOrigem": "Brasil"
                }
                """;

        mockMvc.perform(post("/api/v1/editoras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicada))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Já existe uma editora com esse CNPJ"));
    }

    @Test
    void deveInformarJsonInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/editoras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("O corpo da requisição está ausente ou possui dados inválidos"));
    }
}
