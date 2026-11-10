package br.edu.christus.backend;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.edu.christus.backend.domain.model.Categoria;
import br.edu.christus.backend.domain.model.MaterialDidatico;
import br.edu.christus.backend.repository.CategoriaRepository;
import br.edu.christus.backend.repository.MaterialDidaticoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

// Testes da FUNCIONALIDADE EXTRA: sugestão de material didático para estudo.
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MaterialDidaticoSugestaoTests {

    private static final String URL_SUGESTAO = "/api/v1/sugestoes-estudo";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MaterialDidaticoRepository materialRepository;

    private Categoria cadastrarCategoria(String nome, String codigo) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setCodigo(codigo);
        categoria.setAtivo(true);
        return categoriaRepository.save(categoria);
    }

    private void cadastrarMaterial(String titulo, Categoria categoria) {
        MaterialDidatico material = new MaterialDidatico();
        material.setTitulo(titulo);
        material.setTipoArquivo("PDF");
        material.setUrlArquivo("https://exemplo.com/material.pdf");
        material.setCategoria(categoria);
        materialRepository.save(material);
    }

    @Test
    void sugestaoRetornaUmMaterialCadastrado() throws Exception {
        materialRepository.deleteAll();

        Categoria categoria = cadastrarCategoria("Categoria de Teste da Sugestão", "TESTE-SUGESTAO");
        cadastrarMaterial("Apostila Spring Boot", categoria);
        cadastrarMaterial("Guia de Java", categoria);

        mockMvc.perform(get(URL_SUGESTAO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", anyOf(is("Apostila Spring Boot"), is("Guia de Java"))))
                .andExpect(jsonPath("$.mensagem", anyOf(
                        is("Sugestão de estudo: Apostila Spring Boot"),
                        is("Sugestão de estudo: Guia de Java"))))
                .andExpect(jsonPath("$.categoria").value("Categoria de Teste da Sugestão"));
    }

    @Test
    void sugestaoPorCategoriaSorteiaSomenteMateriaisDaquelaCategoria() throws Exception {
        materialRepository.deleteAll();

        Categoria programacao = cadastrarCategoria("Programação", "TESTE-SUG-PROG");
        Categoria historia = cadastrarCategoria("História", "TESTE-SUG-HIST");
        cadastrarMaterial("Apostila Spring Boot", programacao);
        cadastrarMaterial("Brasil Colônia", historia);

        // Repete algumas vezes para garantir que o sorteio nunca sai da categoria.
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get(URL_SUGESTAO).param("categoriaId", historia.getId().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.titulo").value("Brasil Colônia"))
                    .andExpect(jsonPath("$.categoria").value("História"));
        }
    }

    @Test
    void sugestaoComCategoriaInexistenteRetornaErro404() throws Exception {
        mockMvc.perform(get(URL_SUGESTAO).param("categoriaId", "999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
    }

    @Test
    void sugestaoComCategoriaSemMateriaisRetornaErro404() throws Exception {
        Categoria vazia = cadastrarCategoria("Categoria Vazia", "TESTE-SUG-VAZIA");

        mockMvc.perform(get(URL_SUGESTAO).param("categoriaId", vazia.getId().toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Nenhum material disponível para sugestão de estudo"));
    }

    @Test
    void sugestaoSemMateriaisRetornaErro404() throws Exception {
        materialRepository.deleteAll();

        mockMvc.perform(get(URL_SUGESTAO))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Nenhum material disponível para sugestão de estudo"));
    }

    @Test
    void rotaAntigaDentroDeMaterialDidaticoNaoExisteMais() throws Exception {
        // A sugestão saiu do CRUD de materiais: "sugestao" agora cai na rota /{id} e é um ID inválido.
        mockMvc.perform(get("/api/v1/materiais-didaticos/sugestao"))
                .andExpect(status().isBadRequest());
    }
}
