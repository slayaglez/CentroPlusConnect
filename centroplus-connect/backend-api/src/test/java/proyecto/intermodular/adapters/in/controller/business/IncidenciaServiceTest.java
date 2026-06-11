package proyecto.intermodular.adapters.in.controller.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.centroplus.api.adapters.out.persistence.interfaces.IIncidenciaPersistenceAdapter;
import com.centroplus.api.business.IncidenciaService;
import com.centroplus.api.domain.model.Incidencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidenciaServiceTest {

  @Mock
  IIncidenciaPersistenceAdapter repo;

  @InjectMocks
  IncidenciaService service;

  @Test
  void create_setsIdNull_andSaves() {
    Incidencia input = new Incidencia(99L, 1L, "Asunto", "Desc", LocalDate.now(), "ABIERTA");

    when(repo.save(any(Incidencia.class))).thenAnswer(inv -> inv.getArgument(0));

    service.create(input);

    ArgumentCaptor<Incidencia> captor = ArgumentCaptor.forClass(Incidencia.class);
    verify(repo).save(captor.capture());

    assertNull(captor.getValue().getId(), "IncidenciaService debe poner id a null al crear");
  }

  @Test
  void findAll_delegatesToRepo() {
    when(repo.findAll()).thenReturn(List.of(new Incidencia(1L, 1L, "Asunto", "Desc", LocalDate.now(), "ABIERTA")));
    List<Incidencia> all = service.findAll();
    assertEquals(1, all.size());
    verify(repo).findAll();
  }

  @Test
  void findById_returnsOptional() {
    when(repo.findById(1L)).thenReturn(Optional.of(new Incidencia(1L, 1L, "Asunto", "Desc", LocalDate.now(), "ABIERTA")));
    assertTrue(service.findById(1L).isPresent());
    verify(repo).findById(1L);
  }

  @Test
  void update_mergesFields_keepingExistingWhenPatchIsNull() {
    Incidencia existing = new Incidencia(1L, 1L, "AsuntoViejo", "DescVieja", LocalDate.of(2024, 1, 1), "ABIERTA");
    when(repo.findById(1L)).thenReturn(Optional.of(existing));
    when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    Incidencia patch = new Incidencia(null, null, "AsuntoNuevo", null, null, null);

    Incidencia updated = service.update(1L, patch).orElseThrow();

    assertEquals("AsuntoNuevo", updated.getAsunto());
    assertEquals("DescVieja", updated.getDescripcion(), "Si patch.descripcion es null, se mantiene la anterior");
    assertEquals("ABIERTA", updated.getEstado(), "Si patch.estado es null, se mantiene el anterior");
    verify(repo).save(any(Incidencia.class));
  }

  @Test
  void update_returnsEmpty_whenNotExists() {
    when(repo.findById(99L)).thenReturn(Optional.empty());
    assertTrue(service.update(99L, new Incidencia()).isEmpty());
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
  void cambiarEstado_actualizaEstado_whenExists() {
    Incidencia incidencia = new Incidencia(1L, 1L, "Asunto", "Desc", LocalDate.now(), "ABIERTA");
    when(repo.findById(1L)).thenReturn(Optional.of(incidencia));
    when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    assertTrue(service.cambiarEstado(1L, "CERRADA"));

    ArgumentCaptor<Incidencia> captor = ArgumentCaptor.forClass(Incidencia.class);
    verify(repo).save(captor.capture());
    assertEquals("CERRADA", captor.getValue().getEstado());
  }

  @Test
  void cambiarEstado_returnsFalse_whenNotExists() {
    when(repo.findById(99L)).thenReturn(Optional.empty());
    assertFalse(service.cambiarEstado(99L, "CERRADA"));
    verify(repo, never()).save(any());
  }

  @Test
  void findByUsuario_soloDevuelveIncidenciasDelUsuario() {
    Incidencia deUsuario1 = new Incidencia(1L, 1L, "Asunto1", "Desc", LocalDate.now(), "ABIERTA");
    Incidencia deUsuario2 = new Incidencia(2L, 2L, "Asunto2", "Desc", LocalDate.now(), "ABIERTA");
    when(repo.findAll()).thenReturn(List.of(deUsuario1, deUsuario2));

    List<Incidencia> result = service.findByUsuario(1L);

    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getId());
  }
}
