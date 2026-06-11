package proyecto.intermodular.adapters.out.persistence;

import org.junit.jupiter.api.Test;

import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.adapters.out.persistence.ActividadPersistenceAdapter;
import com.centroplus.api.adapters.out.persistence.jpa.ActividadJpaEntity;
import com.centroplus.api.adapters.out.persistence.jpa.ActividadJpaRepository;
import com.centroplus.api.domain.model.Actividad;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActividadPersistenceAdapterTest {

  @Test
  void findAllMapsEntitiesToDomainTest() {
    ActividadJpaRepository jpaRepo = mock(ActividadJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);

    ActividadPersistenceAdapter adapter = new ActividadPersistenceAdapter(jpaRepo, mapper);

    ActividadJpaEntity entity = new ActividadJpaEntity();
    entity.setId(1L);
    entity.setNombre("Yoga");

    Actividad domain = new Actividad(1L, "Yoga", "DEPORTIVA", 60, 15.0, 20, 5);

    when(jpaRepo.findAll()).thenReturn(List.of(entity));
    when(mapper.toDomain(entity)).thenReturn(domain);

    List<Actividad> result = adapter.findAll();

    assertEquals(1, result.size());
    assertEquals("Yoga", result.get(0).getNombre());
    verify(jpaRepo).findAll();
    verify(mapper).toDomain(entity);
  }

  @Test
  void findByIdMapsOptionalTest() {
    ActividadJpaRepository jpaRepo = mock(ActividadJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadPersistenceAdapter adapter = new ActividadPersistenceAdapter(jpaRepo, mapper);

    ActividadJpaEntity entity = new ActividadJpaEntity();
    entity.setId(1L);

    when(jpaRepo.findById(1L)).thenReturn(Optional.of(entity));
    when(mapper.toDomain(entity)).thenReturn(new Actividad(1L, "Yoga", "DEPORTIVA", 60, 15.0, 20, 5));

    assertTrue(adapter.findById(1L).isPresent());
    verify(jpaRepo).findById(1L);
  }

  @Test
  void findById_returnsEmpty_whenNotFound() {
    ActividadJpaRepository jpaRepo = mock(ActividadJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadPersistenceAdapter adapter = new ActividadPersistenceAdapter(jpaRepo, mapper);

    when(jpaRepo.findById(99L)).thenReturn(Optional.empty());

    assertTrue(adapter.findById(99L).isEmpty());
  }

  @Test
  void existsById_delegatesToJpaRepo() {
    ActividadJpaRepository jpaRepo = mock(ActividadJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadPersistenceAdapter adapter = new ActividadPersistenceAdapter(jpaRepo, mapper);

    when(jpaRepo.existsById(1L)).thenReturn(true);

    assertTrue(adapter.existsById(1L));
    verify(jpaRepo).existsById(1L);
  }
}
