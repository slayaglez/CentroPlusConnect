package proyecto.intermodular.adapters.out.persistence;

import org.junit.jupiter.api.Test;

import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.adapters.out.persistence.UsuarioPersistenceAdapter;
import com.centroplus.api.adapters.out.persistence.jpa.UsuarioJpaEntity;
import com.centroplus.api.adapters.out.persistence.jpa.UsuarioJpaRepository;
import com.centroplus.api.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioPersistenceAdapterTest {

  @Test
  void findAllMapsEntitiesToDomainTest() {
    UsuarioJpaRepository jpaRepo = mock(UsuarioJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);

    UsuarioPersistenceAdapter adapter = new UsuarioPersistenceAdapter(jpaRepo, mapper);

    UsuarioJpaEntity entity = new UsuarioJpaEntity();
    entity.setId(1L);
    entity.setNombre("Ana García");

    Usuario domain = new Usuario(1L, "Ana García", "12345678A", "ana@email.com", "600000001", "SOCIO");

    when(jpaRepo.findAll()).thenReturn(List.of(entity));
    when(mapper.toDomain(entity)).thenReturn(domain);

    List<Usuario> result = adapter.findAll();

    assertEquals(1, result.size());
    assertEquals("Ana García", result.get(0).getNombre());
    verify(jpaRepo).findAll();
    verify(mapper).toDomain(entity);
  }

  @Test
  void findByIdMapsOptionalTest() {
    UsuarioJpaRepository jpaRepo = mock(UsuarioJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    UsuarioPersistenceAdapter adapter = new UsuarioPersistenceAdapter(jpaRepo, mapper);

    UsuarioJpaEntity entity = new UsuarioJpaEntity();
    entity.setId(1L);

    when(jpaRepo.findById(1L)).thenReturn(Optional.of(entity));
    when(mapper.toDomain(entity)).thenReturn(new Usuario(1L, "Ana García", "12345678A", "ana@email.com", "600000001", "SOCIO"));

    assertTrue(adapter.findById(1L).isPresent());
    verify(jpaRepo).findById(1L);
  }

  @Test
  void findById_returnsEmpty_whenNotFound() {
    UsuarioJpaRepository jpaRepo = mock(UsuarioJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    UsuarioPersistenceAdapter adapter = new UsuarioPersistenceAdapter(jpaRepo, mapper);

    when(jpaRepo.findById(99L)).thenReturn(Optional.empty());

    assertTrue(adapter.findById(99L).isEmpty());
  }

  @Test
  void existsById_delegatesToJpaRepo() {
    UsuarioJpaRepository jpaRepo = mock(UsuarioJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    UsuarioPersistenceAdapter adapter = new UsuarioPersistenceAdapter(jpaRepo, mapper);

    when(jpaRepo.existsById(1L)).thenReturn(true);

    assertTrue(adapter.existsById(1L));
    verify(jpaRepo).existsById(1L);
  }
}
