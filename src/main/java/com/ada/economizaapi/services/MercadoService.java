package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.repositories.MercadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MercadoService extends ServicoAbstrato<Mercado, Long, MercadoRepository> {

    private final LocalizacaoService localizacaoService;

    public MercadoService(MercadoRepository mercadoRepository, LocalizacaoService localizacaoService) {
        super(mercadoRepository);
        this.localizacaoService = localizacaoService;
    }

    @Override
    @Transactional
    public Mercado save(Mercado mercado) {
        if (mercado.getLocalizacao() != null && mercado.getLocalizacao().getId() == null) {
            localizacaoService.save(mercado.getLocalizacao());
        }
        return super.save(mercado);
    }

    @Override
    @Transactional
    public Mercado update(Long id, Mercado mercado) {
        if (mercado.getLocalizacao() != null && mercado.getLocalizacao().getId() == null) {
            localizacaoService.save(mercado.getLocalizacao());
        }
        return super.update(id, mercado);
    }
}
