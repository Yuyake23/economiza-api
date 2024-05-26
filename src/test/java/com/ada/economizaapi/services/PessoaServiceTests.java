package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Localizacao;
import com.ada.economizaapi.entities.Pessoa;
import com.ada.economizaapi.repositories.PessoaRepository;
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
public class PessoaServiceTests {

    @Mock
    private PessoaRepository pessoaRepository;
    @Mock
    private LocalizacaoService localizacaoService;
    @InjectMocks
    private PessoaService pessoaService;

    private Pessoa pessoa;

    @BeforeEach
    public void beforeEach() {
        this.pessoa = new Pessoa(null, "Bruno", new Localizacao("lat, lon"), 5d);
    }

    @Test
    public void deveSalvarPessoaELocalizacao() {
        when(pessoaRepository.exists(Example.of(pessoa))).thenReturn(false);

        pessoaService.save(pessoa);

        verify(pessoaRepository, times(1)).save(pessoa);
        verify(localizacaoService, times(1)).save(pessoa.getLocalizacao());
    }

    @Test
    public void deveSalvarPessoaENaoLocalizacao1() {
        Pessoa pessoa = new Pessoa(null, "Bruno", null, 5d);

        pessoaService.save(pessoa);

        verify(pessoaRepository, times(1)).save(pessoa);
        verifyNoInteractions(localizacaoService);
    }

    @Test
    public void deveSalvarPessoaENaoLocalizacao2() {
        Pessoa pessoa = new Pessoa(null, "Bruno", new Localizacao(1L, "lat, lon"), 5d);

        pessoaService.save(pessoa);

        verify(pessoaRepository, times(1)).save(pessoa);
        verifyNoInteractions(localizacaoService);
    }

    @Test
    public void deveAtualizarPessoaELocalizacao() {
        pessoa.setId(1L);
        when(pessoaService.findById(1L)).thenReturn(Optional.of(pessoa));

        Localizacao novaLocalizacao = new Localizacao("lon, lat");
        pessoaService.update(1L, new Pessoa(null, "Helo", new Localizacao("lat, lon"), 5d));

        verify(localizacaoService, times(1)).save(novaLocalizacao);
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    public void deveAtualizarPessoaENaoLocalizacao1() {
        pessoa.setId(1L);
        when(pessoaService.findById(1L)).thenReturn(Optional.of(pessoa));

        pessoaService.update(1L, new Pessoa(null, "Helo", null, 5d));

        verifyNoInteractions(localizacaoService);
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    public void deveAtualizarPessoaENaoLocalizacao2() {
        pessoa.setId(1L);
        when(pessoaService.findById(1L)).thenReturn(Optional.of(pessoa));

        pessoaService.update(1L, new Pessoa(null, "Helo", new Localizacao(1L, "lat, lon"), 5d));

        verifyNoInteractions(localizacaoService);
        verify(pessoaRepository, times(1)).save(pessoa);
    }

}
