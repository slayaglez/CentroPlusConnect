package proyecto.intermodular.adapters.out.persistence;

import org.junit.jupiter.api.Test;

import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.adapters.out.persistence.IncidenciaPersistenceAdapter;
import com.centroplus.api.adapters.out.persistence.jpa.IncidenciaJpaEntity;
import com.centroplus.api.adapters.out.persistence.jpa.IncidenciaJpaRepository;
import com.centroplus.api.domain.model.Incidencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncidenciaPersistenceAdapterTest {

  @Test
  void findAllMapsEntitiesToDomainTest() {
    IncidenciaJpaRepository jpaRepo = mock(IncidenciaJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);

    IncidenciaPersistenceAdapter adapter = new IncidenciaPersistenceAdapter(jpaRepo, mapper);

    IncidenciaJpaEntity entity = new IncidenciaJpaEntity();
    entity.setId(1L);
    entity.setAsunto("Gotera");

    Incidencia domain = new Incidencia(1L, 2L, "Gotera", "Desc", LocalDate.now(), "ABIERTA");

    when(jpaRepo.findAll()).thenReturn(List.of(entity));
    when(mapper.toDomain(entity)).thenReturn(domain);

    List<Incidencia> result = adapter.findAll();

    assertEquals(1, result.size());
    assertEquals("Gotera", result.get(0).getAsunto());
    verify(jpaRepo).findAll();
    verify(mapper).toDomain(entity);
  }

  @Test
  void findByIdMapsOptionalTest() {
    IncidenciaJpaRepository jpaRepo = mock(IncidenciaJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaPersistenceAdapter adapter = new IncidenciaPersistenceAdapter(jpaRepo, mapper);

    IncidenciaJpaEntity entity = new IncidenciaJpaEntity();
    entity.setId(1L);

    when(jpaRepo.findById(1L)).thenReturn(Optional.of(entity));
    when(mapper.toDomain(entity)).thenReturn(new Incidencia(1L, 2L, "Gotera", "Desc", LocalDate.now(), "ABIERTA"));

    assertTrue(adapter.findById(1L).isPresent());
    verify(jpaRepo).findById(1L);
  }

  @Test
  void findById_returnsEmpty_whenNotFound() {
    IncidenciaJpaRepository jpaRepo = mock(IncidenciaJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaPersistenceAdapter adapter = new IncidenciaPersistenceAdapter(jpaRepo, mapper);

    when(jpaRepo.findById(99L)).thenReturn(Optional.empty());

    assertTrue(adapter.findById(99L).isEmpty());
  }

  @Test
  void existsById_delegatesToJpaRepo() {
    IncidenciaJpaRepository jpaRepo = mock(IncidenciaJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaPersistenceAdapter adapter = new IncidenciaPersistenceAdapter(jpaRepo, mapper);

    when(jpaRepo.existsById(1L)).thenReturn(true);

    assertTrue(adapter.existsById(1L));
    verify(jpaRepo).existsById(1L);
  }
}
