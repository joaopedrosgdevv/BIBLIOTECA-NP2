package br.edu.christus.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import br.edu.christus.backend.domain.dto.EditoraDTO;
import br.edu.christus.backend.domain.dto.EditoraLowDTO;
import br.edu.christus.backend.domain.model.Editora;
import br.edu.christus.backend.repository.EditoraRepository;
import br.edu.christus.backend.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EditoraService {

    @Autowired
    private EditoraRepository repository;

    @Autowired
    private LivroRepository livroRepository;

    // CADASTRO: valida os dados, verifica CNPJ duplicado e salva a editora.
    public EditoraLowDTO create(EditoraDTO dados) {

        validarDados(dados);

        if (temTexto(dados.getCnpj()) && repository.existsByCnpj(dados.getCnpj())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma editora com esse CNPJ"
            );
        }

        Editora editora = new Editora();
        copiarDados(dados, editora);

        Editora editoraSalva = repository.save(editora);

        EditoraLowDTO resposta = new EditoraLowDTO();
        resposta.setId(editoraSalva.getId());
        resposta.setNome(editoraSalva.getNome());
        resposta.setCnpj(editoraSalva.getCnpj());
        resposta.setPaisOrigem(editoraSalva.getPaisOrigem());

        return resposta;
    }

    // LISTAGEM: retorna todas as editoras cadastradas.
    public List<Editora> findAll() {
        return repository.findAll();
    }

    // CONSULTA: busca pelo ID e retorna 404 se a editora não existir.
    public Editora findById(Long id) {
        Optional<Editora> resultado = repository.findById(id);

        if (resultado.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Editora não encontrada"
            );
        }

        return resultado.get();
    }

    // EDIÇÃO: busca a editora, valida os novos dados e salva as alterações.
    public Editora update(Long id, EditoraDTO dados) {

        Editora editora = findById(id);

        validarDados(dados);

        if (temTexto(dados.getCnpj()) && repository.existsByCnpjAndIdNot(dados.getCnpj(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe outra editora com esse CNPJ"
            );
        }

        copiarDados(dados, editora);

        return repository.save(editora);
    }

    // EXCLUSÃO: impede apagar editoras com livros vinculados.
    public void delete(Long id) {

        Editora editora = findById(id);

        if (livroRepository.existsByEditoraId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir a editora, pois existem livros vinculados a ela"
            );
        }

        repository.delete(editora);
    }

    // Reúne as validações utilizadas no cadastro e na edição.
    private void validarDados(EditoraDTO dados) {

        if (!temTexto(dados.getNome())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O nome da editora é obrigatório"
            );
        }

        if (!temTexto(dados.getPaisOrigem())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O país de origem é obrigatório"
            );
        }

        if (dados.getAnoFundacao() != null) {
            int anoAtual = LocalDate.now().getYear();

            if (dados.getAnoFundacao() < 1 || dados.getAnoFundacao() > anoAtual) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "O ano de fundação deve estar entre 1 e o ano atual"
                );
            }
        }

        if (temTexto(dados.getSite()) && !urlValida(dados.getSite())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O site deve começar com http:// ou https://"
            );
        }
    }

    // Transfere os campos recebidos no DTO para a entidade.
    private void copiarDados(EditoraDTO dados, Editora editora) {
        editora.setNome(dados.getNome());
        editora.setPaisOrigem(dados.getPaisOrigem());
        editora.setAnoFundacao(dados.getAnoFundacao());
        editora.setSite(dados.getSite());
        editora.setDescricao(dados.getDescricao());

        if (temTexto(dados.getCnpj())) {
            editora.setCnpj(dados.getCnpj());
        } else {
            editora.setCnpj(null);
        }
    }

    private boolean temTexto(String texto) {
        return texto != null && !texto.isBlank();
    }

    private boolean urlValida(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}
