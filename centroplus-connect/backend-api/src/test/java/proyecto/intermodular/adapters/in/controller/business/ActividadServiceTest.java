package proyecto.intermodular.adapters.in.controller.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.centroplus.api.adapters.out.persistence.interfaces.IActividadPersistenceAdapter;
import com.centroplus.api.business.ActividadService;
import com.centroplus.api.domain.model.Actividad;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActividadServiceTest {

  @Mock
  IActividadPersistenceAdapter repo;

  @InjectMocks
  ActividadService service;

  @Test
  void create_setsIdNull_andSaves() {
    Actividad input = new Actividad(99L, "Yoga", "DEPORTIVA", 60, 15.0, 20, 5);

    when(repo.save(any(Actividad.class))).thenAnswer(inv -> inv.getArgument(0));

    service.create(input);

    ArgumentCaptor<Actividad> captor = ArgumentCaptor.forClass(Actividad.class);
    verify(repo).save(captor.capture());

    assertNull(captor.getValue().getId(), "ActividadService debe poner id a null al crear");
  }

  @Test
  void findAll_delegatesToRepo() {
    when(repo.findAll()).thenReturn(List.of(new Actividad(1L, "Pilates", "DEPORTIVA", 45, 12.0, 15, 3)));
    List<Actividad> all = service.findAll();
    assertEquals(1, all.size());
    verify(repo).findAll();
  }

  @Test
  void findById_returnsOptional() {
    when(repo.findById(1L)).thenReturn(Optional.of(new Actividad(1L, "Yoga", "DEPORTIVA", 60, 15.0, 20, 5)));
    assertTrue(service.findById(1L).isPresent());
    verify(repo).findById(1L);
  }

  @Test
  void update_mergesFields_keepingExistingWhenPatchIsNull() {
    Actividad existing = new Actividad(1L, "Yoga", "DEPORTIVA", 60, 15.0, 20, 5);
    when(repo.findById(1L)).thenReturn(Optional.of(existing));
    when(repo.save(any(Actividad.class))).thenAnswer(inv -> inv.getArgument(0));

    Actividad patch = new Actividad(null, "Yoga Pro", null, null, null, null, null);

    Actividad updated = service.update(1L, patch).orElseThrow();

    assertEquals("Yoga Pro", updated.getNombre());
    assertEquals("DEPORTIVA", updated.getTipoActividad(), "Si patch.tipoActividad es null, se mantiene el anterior");
    assertEquals(15.0, updated.getPrecio(), "Si patch.precio es null, se mantiene el anterior");
    verify(repo).save(any(Actividad.class));
  }

  @Test
  void update_returns404_whenNotExists() {
    when(repo.findById(99L)).thenReturn(Optional.empty());
    assertTrue(service.update(99L, new Actividad()).isEmpty());
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
  void reservarPlaza_returnsFalse_whenSinPlazas() {
    Actividad llena = new Actividad(1L, "Spinning", "DEPORTIVA", 45, 10.0, 5, 5);
    when(repo.findById(1L)).thenReturn(Optional.of(llena));

    assertFalse(service.reservarPlaza(1L));
    verify(repo, never()).save(any());
  }

  @Test
  void reservarPlaza_incrementaPlazasOcupadas_whenHayPlazas() {
    Actividad actividad = new Actividad(1L, "Yoga", "DEPORTIVA", 60, 15.0, 10, 3);
    when(repo.findById(1L)).thenReturn(Optional.of(actividad));
    when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    assertTrue(service.reservarPlaza(1L));

    ArgumentCaptor<Actividad> captor = ArgumentCaptor.forClass(Actividad.class);
    verify(repo).save(captor.capture());
    assertEquals(4, captor.getValue().getPlazasOcupadas());
  }

  @Test
  void cancelarPlaza_returnsFalse_whenNoHayOcupadas() {
    Actividad vacia = new Actividad(1L, "Yoga", "DEPORTIVA", 60, 15.0, 10, 0);
    when(repo.findById(1L)).thenReturn(Optional.of(vacia));

    assertFalse(service.cancelarPlaza(1L));
    verify(repo, never()).save(any());
  }

  @Test
  void cancelarPlaza_decrementaPlazasOcupadas_whenHayOcupadas() {
    Actividad actividad = new Actividad(1L, "Yoga", "DEPORTIVA", 60, 15.0, 10, 3);
    when(repo.findById(1L)).thenReturn(Optional.of(actividad));
    when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    assertTrue(service.cancelarPlaza(1L));

    ArgumentCaptor<Actividad> captor = ArgumentCaptor.forClass(Actividad.class);
    verify(repo).save(captor.capture());
    assertEquals(2, captor.getValue().getPlazasOcupadas());
  }

  @Test
  void findCompletas_soloDevuelveActividadesSinPlazas() {
    Actividad llena = new Actividad(1L, "Spinning", "DEPORTIVA", 45, 10.0, 5, 5);
    Actividad disponible = new Actividad(2L, "Yoga", "DEPORTIVA", 60, 15.0, 10, 3);
    when(repo.findAll()).thenReturn(List.of(llena, disponible));

    List<Actividad> completas = service.findCompletas();

    assertEquals(1, completas.size());
    assertEquals(1L, completas.get(0).getId());
  }

  @Test
  void calcularIngresosTotales_sumaPrecioPorPlazasOcupadas() {
    Actividad a1 = new Actividad(1L, "Yoga", "DEPORTIVA", 60, 10.0, 10, 3);   // 30.0
    Actividad a2 = new Actividad(2L, "Pilates", "DEPORTIVA", 45, 15.0, 8, 2); // 30.0
    when(repo.findAll()).thenReturn(List.of(a1, a2));

    double total = service.calcularIngresosTotales();

    assertEquals(60.0, total, 0.001);
  }
}
