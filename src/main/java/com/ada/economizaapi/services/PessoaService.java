package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Pessoa;
import com.ada.economizaapi.repositories.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PessoaService extends ServicoAbstrato<Pessoa, Long, PessoaRepository> {

    private final LocalizacaoService localizacaoService;

    public PessoaService(PessoaRepository pessoaRepository, LocalizacaoService localizacaoService) {
        super(pessoaRepository);
        this.localizacaoService = localizacaoService;
    }

    @Override
    @Transactional
    public Pessoa save(Pessoa pessoa) {
        if (pessoa.getLocalizacao() != null && pessoa.getLocalizacao().getId() == null) {
            localizacaoService.save(pessoa.getLocalizacao());
        }
        return super.save(pessoa);
    }

    @Override
    @Transactional
    public Pessoa update(Long id, Pessoa pessoa) {
        if (pessoa.getLocalizacao() != null && pessoa.getLocalizacao().getId() == null) {
            localizacaoService.save(pessoa.getLocalizacao());
        }
        return super.update(id, pessoa);
    }
}
