package br.edu.christus.backend;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.edu.christus.backend.domain.model.Categoria;
import br.edu.christus.backend.domain.model.MaterialDidatico;
import br.edu.christus.backend.repository.CategoriaRepository;
import br.edu.christus.backend.repository.MaterialDidaticoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MaterialDidaticoBuscaTests {

    private static final String URL_BUSCA = "/api/v1/materiais-didaticos/buscar";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MaterialDidaticoRepository materialRepository;

    @BeforeEach
    void cadastrarDadosDeTeste() {
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria de Teste da Busca");
        categoria.setCodigo("TESTE-BUSCA");
        categoria.setAtivo(true);
        categoriaRepository.save(categoria);

        cadastrarMaterial("Apostila Spring Boot", categoria);
        cadastrarMaterial("Guia de Java", categoria);
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
    void buscaComResultado() throws Exception {
        mockMvc.perform(get(URL_BUSCA).param("titulo", "Spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].titulo", hasItem("Apostila Spring Boot")))
                .andExpect(jsonPath("$[*].titulo", not(hasItem("Guia de Java"))));
    }

    @Test
    void buscaIgnorandoMaiusculasEMinusculas() throws Exception {
        mockMvc.perform(get(URL_BUSCA).param("titulo", "sPrInG bOoT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].titulo", hasItem("Apostila Spring Boot")));
    }

    @Test
    void buscaSemResultadoRetornaListaVazia() throws Exception {
        mockMvc.perform(get(URL_BUSCA).param("titulo", "titulo-que-nao-existe-xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void buscaComTituloVazioRetornaErro400() throws Exception {
        mockMvc.perform(get(URL_BUSCA).param("titulo", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O título para busca é obrigatório"));
    }
}
