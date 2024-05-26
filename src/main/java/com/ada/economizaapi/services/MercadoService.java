package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.exceptions.EntidadeNaoExisteException;
import com.ada.economizaapi.repositories.LocalizacaoRepository;
import com.ada.economizaapi.repositories.MercadoRepository;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class MercadoService extends ServicoAbstrato<Mercado, Long, MercadoRepository> {

    private final LocalizacaoRepository localizacaoRepository;

    public MercadoService(MercadoRepository mercadoRepository, LocalizacaoRepository localizacaoRepository) {
        super(mercadoRepository);
        this.localizacaoRepository = localizacaoRepository;
    }

    @Override
    public Mercado save(Mercado mercado) {
        if (mercado.getLocalizacao() != null && mercado.getLocalizacao().getId() == null) {
            localizacaoRepository.save(mercado.getLocalizacao());
        }
        return repository.save(mercado);
    }

    @Override
    public Mercado update(Long id, Mercado mercado) {
        if (!repository.existsById(id)) {
            throw new EntidadeNaoExisteException();
        }
        if (mercado.getLocalizacao() != null && mercado.getLocalizacao().getId() == null) {
            localizacaoRepository.save(mercado.getLocalizacao());
        }

        Mercado mercadoExistente = this.findById(id)
                .orElseThrow(EntidadeNaoExisteException::new);

        copyProperties(mercado, mercadoExistente, "id", "localizacao");

        if (mercado.getLocalizacao() != null && mercadoExistente.getLocalizacao() != null) {
            copyProperties(mercado.getLocalizacao(), mercadoExistente.getLocalizacao(), "id");
        }

        return repository.save(mercadoExistente);
    }
}
