package br.edu.christus.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import br.edu.christus.backend.domain.dto.MaterialDidaticoDTO;
import br.edu.christus.backend.domain.dto.MaterialDidaticoLowDTO;
import br.edu.christus.backend.domain.model.Categoria;
import br.edu.christus.backend.domain.model.MaterialDidatico;
import br.edu.christus.backend.repository.CategoriaRepository;
import br.edu.christus.backend.repository.MaterialDidaticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MaterialDidaticoService {

    @Autowired
    private MaterialDidaticoRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // CADASTRO: valida os dados, busca a categoria, associa ao material e salva.
    public MaterialDidaticoLowDTO create(MaterialDidaticoDTO dados) {

        validarDados(dados);

        Categoria categoria = buscarCategoria(dados.getCategoriaId());

        MaterialDidatico material = new MaterialDidatico();
        copiarDados(dados, material);
        material.setCategoria(categoria);

        MaterialDidatico materialSalvo = repository.save(material);

        MaterialDidaticoLowDTO resposta = new MaterialDidaticoLowDTO();
        resposta.setId(materialSalvo.getId());
        resposta.setTitulo(materialSalvo.getTitulo());
        resposta.setTipoArquivo(materialSalvo.getTipoArquivo());
        resposta.setUrlArquivo(materialSalvo.getUrlArquivo());
        resposta.setCategoriaId(categoria.getId());

        return resposta;
    }

    // LISTAGEM: retorna todos os materiais cadastrados.
    public List<MaterialDidatico> findAll() {
        return repository.findAll();
    }

    // CONSULTA: busca pelo ID e retorna 404 se o material não existir.
    public MaterialDidatico findById(Long id) {
        Optional<MaterialDidatico> resultado = repository.findById(id);

        if (resultado.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Material didático não encontrado"
            );
        }

        return resultado.get();
    }

    // EDIÇÃO: valida os novos dados e a categoria antes de atualizar o material.
    public MaterialDidatico update(Long id, MaterialDidaticoDTO dados) {

        MaterialDidatico material = findById(id);

        validarDados(dados);

        Categoria categoria = buscarCategoria(dados.getCategoriaId());

        copiarDados(dados, material);
        material.setCategoria(categoria);

        return repository.save(material);
    }

    // EXCLUSÃO: verifica se o material existe antes de apagar.
    public void delete(Long id) {
        MaterialDidatico material = findById(id);
        repository.delete(material);
    }

    // FUNCIONALIDADE EXTRA: valida o título informado e consulta os materiais.
    public List<MaterialDidatico> buscarPorTitulo(String titulo) {

        if (!temTexto(titulo)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O título para busca é obrigatório"
            );
        }

        return repository.findByTituloContainingIgnoreCase(titulo);
    }

    // Busca a categoria informada e retorna 404 se ela não existir.
    private Categoria buscarCategoria(Long categoriaId) {
        Optional<Categoria> resultado = categoriaRepository.findById(categoriaId);

        if (resultado.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Categoria não encontrada"
            );
        }

        return resultado.get();
    }

    // Valida campos obrigatórios, URL e data de publicação.
    private void validarDados(MaterialDidaticoDTO dados) {

        if (!temTexto(dados.getTitulo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O título do material é obrigatório"
            );
        }

        if (!temTexto(dados.getTipoArquivo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O tipo do arquivo é obrigatório"
            );
        }

        if (!temTexto(dados.getUrlArquivo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A URL do arquivo é obrigatória"
            );
        }

        if (!urlValida(dados.getUrlArquivo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A URL do arquivo deve começar com http:// ou https://"
            );
        }

        if (dados.getDataPublicacao() != null && dados.getDataPublicacao().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data de publicação não pode estar no futuro"
            );
        }

        if (dados.getCategoriaId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A categoria é obrigatória"
            );
        }
    }

    // Transfere os campos simples do DTO para a entidade.
    private void copiarDados(MaterialDidaticoDTO dados, MaterialDidatico material) {
        material.setTitulo(dados.getTitulo());
        material.setDescricao(dados.getDescricao());
        material.setTipoArquivo(dados.getTipoArquivo());
        material.setUrlArquivo(dados.getUrlArquivo());
        material.setDataPublicacao(dados.getDataPublicacao());
    }

    private boolean temTexto(String texto) {
        return texto != null && !texto.isBlank();
    }

    private boolean urlValida(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}
