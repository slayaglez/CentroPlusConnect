package proyecto.intermodular.adapters.in.controller;

import org.junit.jupiter.api.Test;

import com.centroplus.api.adapters.in.api.UsuarioRequest;
import com.centroplus.api.adapters.in.api.UsuarioResponse;
import com.centroplus.api.adapters.in.controller.UsuarioController;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.business.interfaces.IUsuarioService;
import com.centroplus.api.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

  @Test
  void getAll_returnsMappedResponses() {
    IUsuarioService service = mock(IUsuarioService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    UsuarioController controller = new UsuarioController(service, mapper);

    Usuario usuario = new Usuario(1L, "Ana García", "12345678A", "ana@email.com", "600000001", "SOCIO");
    UsuarioResponse response = new UsuarioResponse();
    response.setId(1L);
    response.setNombre("Ana García");

    when(service.findAll()).thenReturn(List.of(usuario));
    when(mapper.toResponse(usuario)).thenReturn(response);

    List<UsuarioResponse> res = controller.getAll();

    assertEquals(1, res.size());
    assertEquals(1L, res.get(0).getId());
    verify(service).findAll();
    verify(mapper).toResponse(usuario);
  }

  @Test
  void getById_returns404_whenNotFound() {
    IUsuarioService service = mock(IUsuarioService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    UsuarioController controller = new UsuarioController(service, mapper);

    when(service.findById(10L)).thenReturn(Optional.empty());

    var resp = controller.getById(10L);

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void getById_returns200_whenFound() {
    IUsuarioService service = mock(IUsuarioService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    UsuarioController controller = new UsuarioController(service, mapper);

    Usuario usuario = new Usuario(1L, "Ana García", "12345678A", "ana@email.com", "600000001", "SOCIO");
    UsuarioResponse response = new UsuarioResponse();
    response.setId(1L);

    when(service.findById(1L)).thenReturn(Optional.of(usuario));
    when(mapper.toResponse(usuario)).thenReturn(response);

    var resp = controller.getById(1L);

    assertEquals(200, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
  }

  @Test
  void create_returns201_andBody() {
    IUsuarioService service = mock(IUsuarioService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    UsuarioController controller = new UsuarioController(service, mapper);

    UsuarioRequest req = new UsuarioRequest();
    req.setNombre("Carlos");
    req.setDni("87654321B");
    req.setEmail("carlos@email.com");
    req.setTelefono("600000002");
    req.setTipoUsuario("SOCIO");

    Usuario domain = new Usuario(null, "Carlos", "87654321B", "carlos@email.com", "600000002", "SOCIO");
    Usuario saved = new Usuario(1L, "Carlos", "87654321B", "carlos@email.com", "600000002", "SOCIO");
    UsuarioResponse response = new UsuarioResponse();
    response.setId(1L);
    response.setNombre("Carlos");

    when(mapper.toDomain(req)).thenReturn(domain);
    when(service.create(domain)).thenReturn(saved);
    when(mapper.toResponse(saved)).thenReturn(response);

    var resp = controller.create(req);

    assertEquals(201, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
    assertEquals(1L, resp.getBody().getId());
  }

  @Test
  void delete_returns204_whenExists() {
    IUsuarioService service = mock(IUsuarioService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    UsuarioController controller = new UsuarioController(service, mapper);

    when(service.deleteById(1L)).thenReturn(true);

    var resp = controller.delete(1L);

    assertEquals(204, resp.getStatusCode().value());
  }

  @Test
  void delete_returns404_whenNotExists() {
    IUsuarioService service = mock(IUsuarioService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    UsuarioController controller = new UsuarioController(service, mapper);

    when(service.deleteById(99L)).thenReturn(false);

    var resp = controller.delete(99L);

    assertEquals(404, resp.getStatusCode().value());
  }
}
