package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Localizacao;
import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.repositories.MercadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MercadoServiceTests {

    @Mock
    private MercadoRepository mercadoRepository;
    @Mock
    private LocalizacaoService localizacaoService;
    @InjectMocks
    private MercadoService mercadoService;

    private Mercado mercado;

    @BeforeEach
    public void beforeEach() {
        this.mercado = new Mercado(null, "São Rafael", new Localizacao("lat, lon"));
    }

    @Test
    public void deveSalvarMercadoELocalizacao() {
        when(mercadoRepository.exists(Example.of(mercado))).thenReturn(false);

        mercadoService.save(mercado);

        verify(mercadoRepository, times(1)).save(mercado);
        verify(localizacaoService, times(1)).save(mercado.getLocalizacao());
    }

    @Test
    public void deveSalvarMercadoENaoLocalizacao1() {
        Mercado mercado = new Mercado(null, "São Rafael", null);

        mercadoService.save(mercado);

        verify(mercadoRepository, times(1)).save(mercado);
        verifyNoInteractions(localizacaoService);
    }

    @Test
    public void deveSalvarMercadoENaoLocalizacao2() {
        Mercado mercado = new Mercado(null, "São Rafael", new Localizacao(1L, "lon, lat"));

        mercadoService.save(mercado);

        verify(mercadoRepository, times(1)).save(mercado);
        verifyNoInteractions(localizacaoService);
    }

    @Test
    public void deveAtualizarMercadoELocalizacao() {
        mercado.setId(1L);
        when(mercadoService.findById(1L)).thenReturn(Optional.of(mercado));

        Localizacao novaLocalizacao = new Localizacao("lon, lat");
        mercadoService.update(1L, new Mercado(null, "Bom Preço", novaLocalizacao));

        verify(localizacaoService, times(1)).save(novaLocalizacao);
        verify(mercadoRepository, times(1)).save(mercado);
    }

    @Test
    public void deveAtualizarMercadoENaoLocalizacao1() {
        mercado.setId(1L);
        when(mercadoService.findById(1L)).thenReturn(Optional.of(mercado));

        mercadoService.update(1L, new Mercado(null, "Bom Preço", null));

        verifyNoInteractions(localizacaoService);
        verify(mercadoRepository, times(1)).save(mercado);
    }

    @Test
    public void deveAtualizarMercadoENaoLocalizacao2() {
        mercado.setId(1L);
        when(mercadoService.findById(1L)).thenReturn(Optional.of(mercado));

        mercadoService.update(1L, new Mercado(null, "Bom Preço", new Localizacao(1L, "lon, lat")));

        verifyNoInteractions(localizacaoService);
        verify(mercadoRepository, times(1)).save(mercado);
    }

}
