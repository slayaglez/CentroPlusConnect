package proyecto.intermodular.adapters.in.controller.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.centroplus.api.adapters.out.persistence.interfaces.IUsuarioPersistenceAdapter;
import com.centroplus.api.business.UsuarioService;
import com.centroplus.api.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

  @Mock
  IUsuarioPersistenceAdapter repo;

  @InjectMocks
  UsuarioService service;

  @Test
  void create_setsIdNull_andSaves() {
    Usuario input = new Usuario(99L, "Ana", "12345678A", "ana@email.com", "600000001", "SOCIO");

    when(repo.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

    service.create(input);

    ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
    verify(repo).save(captor.capture());

    assertNull(captor.getValue().getId(), "UsuarioService debe poner id a null al crear");
    assertEquals("Ana", captor.getValue().getNombre());
  }

  @Test
  void findAll_delegatesToRepo() {
    when(repo.findAll()).thenReturn(List.of(new Usuario(1L, "Ana", "12345678A", "ana@email.com", "600000001", "SOCIO")));
    List<Usuario> all = service.findAll();
    assertEquals(1, all.size());
    verify(repo).findAll();
  }

  @Test
  void findById_returnsOptional() {
    when(repo.findById(1L)).thenReturn(Optional.of(new Usuario(1L, "Ana", "12345678A", "ana@email.com", "600000001", "SOCIO")));
    assertTrue(service.findById(1L).isPresent());
    verify(repo).findById(1L);
  }

  @Test
  void update_mergesFields_keepingExistingWhenPatchIsNull() {
    Usuario existing = new Usuario(1L, "Ana", "12345678A", "ana@email.com", "600000001", "SOCIO");
    when(repo.findById(1L)).thenReturn(Optional.of(existing));
    when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    Usuario patch = new Usuario(null, "Ana López", null, null, null, null);

    Usuario updated = service.update(1L, patch).orElseThrow();

    assertEquals("Ana López", updated.getNombre());
    assertEquals("12345678A", updated.getDni(), "Si patch.dni es null, se mantiene el anterior");
    assertEquals("ana@email.com", updated.getEmail(), "Si patch.email es null, se mantiene el anterior");
    assertEquals("SOCIO", updated.getTipoUsuario(), "Si patch.tipoUsuario es null, se mantiene el anterior");
    verify(repo).save(any(Usuario.class));
  }

  @Test
  void update_returnsEmpty_whenNotExists() {
    when(repo.findById(99L)).thenReturn(Optional.empty());
    assertTrue(service.update(99L, new Usuario()).isEmpty());
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
}
