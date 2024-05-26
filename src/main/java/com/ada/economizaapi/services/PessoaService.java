package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Pessoa;
import com.ada.economizaapi.exceptions.EntidadeNaoExisteException;
import com.ada.economizaapi.repositories.PessoaRepository;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class PessoaService extends ServicoAbstrato<Pessoa, Long, PessoaRepository> {

    private final LocalizacaoService localizacaoService;

    public PessoaService(PessoaRepository pessoaRepository, LocalizacaoService localizacaoService) {
        super(pessoaRepository);
        this.localizacaoService = localizacaoService;
    }

    @Override
    public Pessoa save(Pessoa pessoa) {
        if (pessoa.getLocalizacao() != null && pessoa.getLocalizacao().getId() == null) {
            localizacaoService.save(pessoa.getLocalizacao());
        }
        return super.save(pessoa);
    }

    @Override
    public Pessoa update(Long id, Pessoa pessoa) {
        if (!repository.existsById(id)) {
            throw new EntidadeNaoExisteException();
        }
        if (pessoa.getLocalizacao() != null && pessoa.getLocalizacao().getId() == null) {
            localizacaoService.save(pessoa.getLocalizacao());
        }

        Pessoa pessoaExistente = this.findById(id)
                .orElseThrow(EntidadeNaoExisteException::new);

        copyProperties(pessoa, pessoaExistente, "id", "localizacao");

        if (pessoa.getLocalizacao() != null && pessoaExistente.getLocalizacao() != null) {
            copyProperties(pessoa.getLocalizacao(), pessoaExistente.getLocalizacao(), "id");
        }

        return repository.save(pessoaExistente);
    }
}
