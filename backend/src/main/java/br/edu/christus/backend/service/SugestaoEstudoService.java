package br.edu.christus.backend.service;

import java.util.List;
import java.util.Random;
import br.edu.christus.backend.domain.dto.SugestaoEstudoDTO;
import br.edu.christus.backend.domain.model.MaterialDidatico;
import br.edu.christus.backend.repository.CategoriaRepository;
import br.edu.christus.backend.repository.MaterialDidaticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

// FUNCIONALIDADE EXTRA: sugestão de material didático para estudo.
// Fica separada do CRUD de materiais para deixar claro que é um recurso adicional.
@Service
public class SugestaoEstudoService {

    @Autowired
    private MaterialDidaticoRepository materialRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Gerador de números aleatórios usado no sorteio.
    private Random random = new Random();

    // Sorteia um material. Se a categoria for informada, sorteia apenas entre os materiais dela.
    public SugestaoEstudoDTO sugerir(Long categoriaId) {

        List<MaterialDidatico> materiais;

        if (categoriaId == null) {
            // Sem filtro: considera todos os materiais cadastrados.
            materiais = materialRepository.findAll();
        } else {
            // Com filtro: a categoria precisa existir.
            if (!categoriaRepository.existsById(categoriaId)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada"
                );
            }
            materiais = materialRepository.findByCategoriaId(categoriaId);
        }

        // Sem materiais, não há o que sortear: retorna 404.
        if (materiais.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhum material disponível para sugestão de estudo"
            );
        }

        // Sorteia uma posição da lista, de 0 até (quantidade - 1).
        int posicaoSorteada = random.nextInt(materiais.size());
        MaterialDidatico sorteado = materiais.get(posicaoSorteada);

        return montarResposta(sorteado);
    }

    // Copia os dados do material sorteado para o DTO de resposta.
    private SugestaoEstudoDTO montarResposta(MaterialDidatico material) {
        SugestaoEstudoDTO resposta = new SugestaoEstudoDTO();
        resposta.setMensagem("Sugestão de estudo: " + material.getTitulo());
        resposta.setMaterialId(material.getId());
        resposta.setTitulo(material.getTitulo());
        resposta.setDescricao(material.getDescricao());
        resposta.setTipoArquivo(material.getTipoArquivo());
        resposta.setUrlArquivo(material.getUrlArquivo());
        resposta.setDataPublicacao(material.getDataPublicacao());
        resposta.setCategoria(material.getCategoria().getNome());
        return resposta;
    }
}
