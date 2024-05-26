package com.ada.economizaapi.services;

import com.ada.economizaapi.exceptions.EntidadeJaExisteException;
import com.ada.economizaapi.exceptions.EntidadeNaoExisteException;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServicoAbstratoTests {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @jakarta.persistence.Entity
    private static class Entity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String field;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entity entity = (Entity) o;
            return Objects.equals(id, entity.id);
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    private interface EntityRepository extends JpaRepository<Entity, Long> {}

    @Mock
    private EntityRepository repository;
    private ServicoAbstrato<Entity, Long, EntityRepository> servicoAbstrato;

    private Entity entity;

    @BeforeEach
    public void beforeEach() {
        this.servicoAbstrato = new ServicoAbstrato<>(repository) {};
        this.entity = new Entity(null, "test");
    }

    @Test
    public void deveExistir() {
        when(repository.exists(Example.of(entity))).thenReturn(true);

        assertTrue(servicoAbstrato.exists(entity));

        verify(repository, times(1)).exists(Example.of(entity));
    }

    @Test
    public void naoDeveExistir() {
        when(repository.exists(any())).thenReturn(false);

        assertFalse(servicoAbstrato.exists(entity));

        verify(repository, times(1)).exists(Example.of(entity));
    }

    @Test
    public void deveExistirPorId() {
        entity.setId(1L);
        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(servicoAbstrato.existsById(1L));

        verify(repository, times(1)).existsById(1L);
    }

    @Test
    public void naoDeveExistirPorId() {
        when(repository.existsById(any())).thenReturn(false);

        assertFalse(servicoAbstrato.existsById(1L));

        verify(repository, times(1)).existsById(1L);
    }

    @Test
    public void deveSalvarComSucesso() {
        Entity savedEntity = new Entity(1L, "test");
        when(repository.exists(any())).thenReturn(false);
        when(repository.save(entity)).thenReturn(savedEntity);

        Entity returnedEntity = servicoAbstrato.save(entity);

        assertNotNull(returnedEntity.getId());
        assertEquals(entity.getField(), returnedEntity.getField());

        verify(repository, times(1)).exists(Example.of(entity));
        verify(repository, times(1)).save(entity);
    }

    @Test
    public void deveLancarExcecaoAotentarSalvar() {
        when(repository.exists(any())).thenReturn(true);

        assertThrows(EntidadeJaExisteException.class, () -> servicoAbstrato.save(entity));

        verify(repository, times(1)).exists(Example.of(entity));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deveBuscarTodos() {
        List<Entity> entities = servicoAbstrato.findAll();

        assertNotNull(entities);
        verify(repository, times(1)).findAll();
    }

    @Test
    public void deveAtualizarComSucesso1() {
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        Entity updatedEntity = new Entity(null, "testado");

        Entity returnedEntity = servicoAbstrato.update(1L, updatedEntity);

        assertEquals(updatedEntity.getField(), returnedEntity.getField());
        verify(repository, times(1)).save(entity);
    }

    @Test
    public void deveAtualizarComSucesso2() {
        entity.setId(1L);
        when(repository.exists(Example.of(entity))).thenReturn(true);
        Entity updatedEntity = new Entity(1L, "testado");
        when(repository.save(updatedEntity)).thenReturn(updatedEntity);

        Entity returnedEntity = servicoAbstrato.update(updatedEntity);

        assertEquals(updatedEntity.getField(), returnedEntity.getField());
        verify(repository, times(1)).save(entity);
    }

    @Test
    public void deveLancarExcecaoAoAtualizar1() {
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Entity updatedEntity = new Entity(null, "testado");

        assertThrows(EntidadeNaoExisteException.class, () -> servicoAbstrato.update(1L, updatedEntity));

        verify(repository, times(0)).save(entity);
    }

    @Test
    public void deveLancarExcecaoAoAtualizar2() {
        entity.setId(1L);
        when(repository.exists(Example.of(entity))).thenReturn(false);
        Entity updatedEntity = new Entity(1L, "testado");

        assertThrows(EntidadeNaoExisteException.class, () -> servicoAbstrato.update(updatedEntity));

        verify(repository, times(0)).save(entity);
    }

    @Test
    public void deveDeletar() {
        servicoAbstrato.delete(entity);

        verify(repository, times(1)).delete(entity);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deveDeletarPorId() {
        servicoAbstrato.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deveSalvarTodos() {
        Entity entity2 = new Entity(null, "test2");
        List<Entity> entities = List.of(entity, entity2);
        when(repository.save(entity)).thenReturn(entity);
        when(repository.save(entity2)).thenReturn(entity2);

        List<Entity> returnedEntities = servicoAbstrato.saveAll(entities);

        assertEquals(returnedEntities, entities);
        verify(repository, times(2)).save(any());
        verify(repository, times(2)).exists(any());
    }

}
