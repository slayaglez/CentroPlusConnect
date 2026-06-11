package proyecto.intermodular.adapters.in.controller;

import org.junit.jupiter.api.Test;

import com.centroplus.api.adapters.in.api.IncidenciaRequest;
import com.centroplus.api.adapters.in.api.IncidenciaResponse;
import com.centroplus.api.adapters.in.controller.IncidenciaController;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.business.interfaces.IIncidenciaService;
import com.centroplus.api.domain.model.Incidencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncidenciaControllerTest {

  @Test
  void getAll_returnsMappedResponses() {
    IIncidenciaService service = mock(IIncidenciaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaController controller = new IncidenciaController(service, mapper);

    Incidencia incidencia = new Incidencia(1L, 2L, "Fuga de agua", "Descripción", LocalDate.now(), "ABIERTA");
    IncidenciaResponse response = new IncidenciaResponse();
    response.setId(1L);

    when(service.findAll()).thenReturn(List.of(incidencia));
    when(mapper.toResponse(incidencia)).thenReturn(response);

    List<IncidenciaResponse> res = controller.getAll();

    assertEquals(1, res.size());
    assertEquals(1L, res.get(0).getId());
    verify(service).findAll();
    verify(mapper).toResponse(incidencia);
  }

  @Test
  void getById_returns404_whenNotFound() {
    IIncidenciaService service = mock(IIncidenciaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaController controller = new IncidenciaController(service, mapper);

    when(service.findById(10L)).thenReturn(Optional.empty());

    var resp = controller.getById(10L);

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void getById_returns200_whenFound() {
    IIncidenciaService service = mock(IIncidenciaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaController controller = new IncidenciaController(service, mapper);

    Incidencia incidencia = new Incidencia(1L, 2L, "Asunto", "Desc", LocalDate.now(), "ABIERTA");
    IncidenciaResponse response = new IncidenciaResponse();
    response.setId(1L);

    when(service.findById(1L)).thenReturn(Optional.of(incidencia));
    when(mapper.toResponse(incidencia)).thenReturn(response);

    var resp = controller.getById(1L);

    assertEquals(200, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
  }

  @Test
  void create_returns201_andBody() {
    IIncidenciaService service = mock(IIncidenciaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaController controller = new IncidenciaController(service, mapper);

    IncidenciaRequest req = new IncidenciaRequest();
    req.setIdUsuario(1L);
    req.setAsunto("Gotera");
    req.setDescripcion("Hay una gotera en el techo");
    req.setFecha(LocalDate.now());
    req.setEstado("ABIERTA");

    Incidencia domain = new Incidencia(null, 1L, "Gotera", "Hay una gotera en el techo", LocalDate.now(), "ABIERTA");
    Incidencia saved = new Incidencia(1L, 1L, "Gotera", "Hay una gotera en el techo", LocalDate.now(), "ABIERTA");
    IncidenciaResponse response = new IncidenciaResponse();
    response.setId(1L);

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
    IIncidenciaService service = mock(IIncidenciaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaController controller = new IncidenciaController(service, mapper);

    when(service.deleteById(1L)).thenReturn(true);

    var resp = controller.delete(1L);

    assertEquals(204, resp.getStatusCode().value());
  }

  @Test
  void delete_returns404_whenNotExists() {
    IIncidenciaService service = mock(IIncidenciaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaController controller = new IncidenciaController(service, mapper);

    when(service.deleteById(99L)).thenReturn(false);

    var resp = controller.delete(99L);

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void cambiarEstado_returns200_whenSuccess() {
    IIncidenciaService service = mock(IIncidenciaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaController controller = new IncidenciaController(service, mapper);

    when(service.cambiarEstado(1L, "CERRADA")).thenReturn(true);

    var resp = controller.cambiarEstado(1L, Map.of("estado", "CERRADA"));

    assertEquals(200, resp.getStatusCode().value());
  }

  @Test
  void cambiarEstado_returns404_whenNotFound() {
    IIncidenciaService service = mock(IIncidenciaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaController controller = new IncidenciaController(service, mapper);

    when(service.cambiarEstado(99L, "CERRADA")).thenReturn(false);

    var resp = controller.cambiarEstado(99L, Map.of("estado", "CERRADA"));

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void cambiarEstado_returns400_whenEstadoMissing() {
    IIncidenciaService service = mock(IIncidenciaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaController controller = new IncidenciaController(service, mapper);

    var resp = controller.cambiarEstado(1L, Map.of());

    assertEquals(400, resp.getStatusCode().value());
    verify(service, never()).cambiarEstado(anyLong(), anyString());
  }

  @Test
  void getByUsuario_returnsMappedList() {
    IIncidenciaService service = mock(IIncidenciaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    IncidenciaController controller = new IncidenciaController(service, mapper);

    Incidencia incidencia = new Incidencia(1L, 2L, "Asunto", "Desc", LocalDate.now(), "ABIERTA");
    IncidenciaResponse response = new IncidenciaResponse();
    response.setId(1L);

    when(service.findByUsuario(2L)).thenReturn(List.of(incidencia));
    when(mapper.toResponse(incidencia)).thenReturn(response);

    List<IncidenciaResponse> res = controller.getByUsuario(2L);

    assertEquals(1, res.size());
    verify(service).findByUsuario(2L);
  }
}
