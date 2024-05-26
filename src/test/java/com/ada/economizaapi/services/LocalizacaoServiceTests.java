package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Localizacao;
import com.ada.economizaapi.repositories.LocalizacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LocalizacaoServiceTests {

    @Mock
    private LocalizacaoRepository localizacaoRepository;
    @InjectMocks
    private LocalizacaoService localizacaoService;

    private Localizacao localizacaoCasa;
    private Localizacao localizacaoMercado;


    @BeforeEach
    public void beforeEach() {
        localizacaoCasa = new Localizacao(null, "-48.202220601703104,-17.45826586438533");
        localizacaoMercado = new Localizacao(null, "-48.201765767168645,-17.46098566181234");
    }

    @Test
    public void deveRetornarDistanciaCorreta() {
        double distance = localizacaoService.retornarDistanciaKm(localizacaoCasa, localizacaoMercado);

        assertEquals(distance, 0.4385);
    }

}
