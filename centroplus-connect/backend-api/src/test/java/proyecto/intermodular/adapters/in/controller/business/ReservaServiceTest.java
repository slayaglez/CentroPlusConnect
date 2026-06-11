package proyecto.intermodular.adapters.in.controller.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.centroplus.api.adapters.out.persistence.interfaces.IReservaPersistenceAdapter;
import com.centroplus.api.business.ReservaService;
import com.centroplus.api.domain.model.Reserva;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

  @Mock
  IReservaPersistenceAdapter repo;

  @InjectMocks
  ReservaService service;

  @Test
  void create_setsIdNull_andSaves() {
    Reserva input = new Reserva(99L, 1L, 2L, LocalDate.now(), "ACTIVA");

    when(repo.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

    service.create(input);

    ArgumentCaptor<Reserva> captor = ArgumentCaptor.forClass(Reserva.class);
    verify(repo).save(captor.capture());

    assertNull(captor.getValue().getId(), "ReservaService debe poner id a null al crear");
  }

  @Test
  void findAll_delegatesToRepo() {
    when(repo.findAll()).thenReturn(List.of(new Reserva(1L, 1L, 2L, LocalDate.now(), "ACTIVA")));
    List<Reserva> all = service.findAll();
    assertEquals(1, all.size());
    verify(repo).findAll();
  }

  @Test
  void findById_returnsOptional() {
    when(repo.findById(1L)).thenReturn(Optional.of(new Reserva(1L, 1L, 2L, LocalDate.now(), "ACTIVA")));
    assertTrue(service.findById(1L).isPresent());
    verify(repo).findById(1L);
  }

  @Test
  void update_mergesFields_keepingExistingWhenPatchIsNull() {
    Reserva existing = new Reserva(1L, 1L, 2L, LocalDate.of(2024, 1, 1), "ACTIVA");
    when(repo.findById(1L)).thenReturn(Optional.of(existing));
    when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    Reserva patch = new Reserva(null, null, 3L, null, null);

    Reserva updated = service.update(1L, patch).orElseThrow();

    assertEquals(3L, updated.getIdActividad());
    assertEquals(1L, updated.getIdUsuario(), "Si patch.idUsuario es null, se mantiene el anterior");
    assertEquals("ACTIVA", updated.getEstado(), "Si patch.estado es null, se mantiene el anterior");
    verify(repo).save(any(Reserva.class));
  }

  @Test
  void update_returnsEmpty_whenNotExists() {
    when(repo.findById(99L)).thenReturn(Optional.empty());
    assertTrue(service.update(99L, new Reserva()).isEmpty());
  }

  @Test
  void deleteById_returnsFalse_whenNotExists() {
    when(repo.existsById(5L)).thenReturn(false);
    assertFalse(service.deleteById(5L));
    verify(repo, never()).deleteById(anyLong());
  }

  @Test
  void deleteById_deletes_whenExists() {
    when(repo.existsById(5L)).thenReturn(true);
    assertTrue(service.deleteById(5L));
    verify(repo).deleteById(5L);
  }

  @Test
  void cancelarReserva_cambiaEstadoACancelada_whenExists() {
    Reserva reserva = new Reserva(1L, 1L, 2L, LocalDate.now(), "ACTIVA");
    when(repo.findById(1L)).thenReturn(Optional.of(reserva));
    when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    assertTrue(service.cancelarReserva(1L));

    ArgumentCaptor<Reserva> captor = ArgumentCaptor.forClass(Reserva.class);
    verify(repo).save(captor.capture());
    assertEquals("CANCELADA", captor.getValue().getEstado());
  }

  @Test
  void cancelarReserva_returnsFalse_whenNotExists() {
    when(repo.findById(99L)).thenReturn(Optional.empty());
    assertFalse(service.cancelarReserva(99L));
    verify(repo, never()).save(any());
  }

  @Test
  void findDisponibles_soloDevuelveReservasActivas() {
    Reserva activa = new Reserva(1L, 1L, 2L, LocalDate.now(), "ACTIVA");
    Reserva cancelada = new Reserva(2L, 1L, 3L, LocalDate.now(), "CANCELADA");
    when(repo.findAll()).thenReturn(List.of(activa, cancelada));

    List<Reserva> disponibles = service.findDisponibles();

    assertEquals(1, disponibles.size());
    assertEquals(1L, disponibles.get(0).getId());
  }
}
