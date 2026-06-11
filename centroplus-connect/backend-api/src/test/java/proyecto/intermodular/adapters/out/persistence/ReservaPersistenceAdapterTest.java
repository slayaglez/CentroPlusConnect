package proyecto.intermodular.adapters.out.persistence;

import org.junit.jupiter.api.Test;

import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.adapters.out.persistence.ReservaPersistenceAdapter;
import com.centroplus.api.adapters.out.persistence.jpa.ReservaJpaEntity;
import com.centroplus.api.adapters.out.persistence.jpa.ReservaJpaRepository;
import com.centroplus.api.domain.model.Reserva;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaPersistenceAdapterTest {

  @Test
  void findAllMapsEntitiesToDomainTest() {
    ReservaJpaRepository jpaRepo = mock(ReservaJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);

    ReservaPersistenceAdapter adapter = new ReservaPersistenceAdapter(jpaRepo, mapper);

    ReservaJpaEntity entity = new ReservaJpaEntity();
    entity.setId(1L);
    entity.setEstado("ACTIVA");

    Reserva domain = new Reserva(1L, 2L, 3L, LocalDate.now(), "ACTIVA");

    when(jpaRepo.findAll()).thenReturn(List.of(entity));
    when(mapper.toDomain(entity)).thenReturn(domain);

    List<Reserva> result = adapter.findAll();

    assertEquals(1, result.size());
    assertEquals("ACTIVA", result.get(0).getEstado());
    verify(jpaRepo).findAll();
    verify(mapper).toDomain(entity);
  }

  @Test
  void findByIdMapsOptionalTest() {
    ReservaJpaRepository jpaRepo = mock(ReservaJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaPersistenceAdapter adapter = new ReservaPersistenceAdapter(jpaRepo, mapper);

    ReservaJpaEntity entity = new ReservaJpaEntity();
    entity.setId(1L);

    when(jpaRepo.findById(1L)).thenReturn(Optional.of(entity));
    when(mapper.toDomain(entity)).thenReturn(new Reserva(1L, 2L, 3L, LocalDate.now(), "ACTIVA"));

    assertTrue(adapter.findById(1L).isPresent());
    verify(jpaRepo).findById(1L);
  }

  @Test
  void findById_returnsEmpty_whenNotFound() {
    ReservaJpaRepository jpaRepo = mock(ReservaJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaPersistenceAdapter adapter = new ReservaPersistenceAdapter(jpaRepo, mapper);

    when(jpaRepo.findById(99L)).thenReturn(Optional.empty());

    assertTrue(adapter.findById(99L).isEmpty());
  }

  @Test
  void existsById_delegatesToJpaRepo() {
    ReservaJpaRepository jpaRepo = mock(ReservaJpaRepository.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaPersistenceAdapter adapter = new ReservaPersistenceAdapter(jpaRepo, mapper);

    when(jpaRepo.existsById(1L)).thenReturn(true);

    assertTrue(adapter.existsById(1L));
    verify(jpaRepo).existsById(1L);
  }
}
