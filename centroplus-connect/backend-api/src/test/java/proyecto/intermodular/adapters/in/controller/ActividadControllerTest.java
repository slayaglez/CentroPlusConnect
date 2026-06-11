package proyecto.intermodular.adapters.in.controller;

import org.junit.jupiter.api.Test;

import com.centroplus.api.adapters.in.api.ActividadRequest;
import com.centroplus.api.adapters.in.api.ActividadResponse;
import com.centroplus.api.adapters.in.controller.ActividadController;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.business.interfaces.IActividadService;
import com.centroplus.api.domain.model.Actividad;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActividadControllerTest {

  @Test
  void getAll_returnsMappedResponses() {
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    Actividad actividad = new Actividad(1L, "Yoga", "DEPORTIVA", 60, 15.0, 20, 5);
    ActividadResponse response = new ActividadResponse();
    response.setId(1L);
    response.setNombre("Yoga");

    when(service.findAll()).thenReturn(List.of(actividad));
    when(mapper.toResponse(actividad)).thenReturn(response);

    List<ActividadResponse> res = controller.getAll();

    assertEquals(1, res.size());
    assertEquals(1L, res.get(0).getId());
    verify(service).findAll();
    verify(mapper).toResponse(actividad);
  }

  @Test
  void getById_returns404_whenNotFound() {
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    when(service.findById(10L)).thenReturn(Optional.empty());

    var resp = controller.getById(10L);

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void getById_returns200_whenFound() {
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    Actividad actividad = new Actividad(1L, "Pilates", "DEPORTIVA", 45, 12.0, 15, 3);
    ActividadResponse response = new ActividadResponse();
    response.setId(1L);

    when(service.findById(1L)).thenReturn(Optional.of(actividad));
    when(mapper.toResponse(actividad)).thenReturn(response);

    var resp = controller.getById(1L);

    assertEquals(200, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
    assertEquals(1L, resp.getBody().getId());
  }

  @Test
  void create_returns201_andBody() {
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    ActividadRequest req = new ActividadRequest();
    req.setNombre("Natación");
    req.setTipoActividad("ACUATICA");
    req.setDuracion(60);
    req.setPrecio(20.0);
    req.setPlazasMaximas(10);
    req.setPlazasOcupadas(0);

    Actividad domain = new Actividad(null, "Natación", "ACUATICA", 60, 20.0, 10, 0);
    Actividad saved = new Actividad(1L, "Natación", "ACUATICA", 60, 20.0, 10, 0);
    ActividadResponse response = new ActividadResponse();
    response.setId(1L);
    response.setNombre("Natación");

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
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    when(service.deleteById(1L)).thenReturn(true);

    var resp = controller.delete(1L);

    assertEquals(204, resp.getStatusCode().value());
  }

  @Test
  void delete_returns404_whenNotExists() {
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    when(service.deleteById(99L)).thenReturn(false);

    var resp = controller.delete(99L);

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void reservarPlaza_returns200_whenSuccess() {
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    when(service.reservarPlaza(1L)).thenReturn(true);

    var resp = controller.reservarPlaza(1L);

    assertEquals(200, resp.getStatusCode().value());
  }

  @Test
  void reservarPlaza_returns400_whenFull() {
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    when(service.reservarPlaza(1L)).thenReturn(false);

    var resp = controller.reservarPlaza(1L);

    assertEquals(400, resp.getStatusCode().value());
  }

  @Test
  void cancelarPlaza_returns200_whenSuccess() {
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    when(service.cancelarPlaza(1L)).thenReturn(true);

    var resp = controller.cancelarPlaza(1L);

    assertEquals(200, resp.getStatusCode().value());
  }

  @Test
  void getCompletas_returnsMappedList() {
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    Actividad completa = new Actividad(1L, "Spinning", "DEPORTIVA", 45, 10.0, 5, 5);
    ActividadResponse response = new ActividadResponse();
    response.setId(1L);

    when(service.findCompletas()).thenReturn(List.of(completa));
    when(mapper.toResponse(completa)).thenReturn(response);

    List<ActividadResponse> res = controller.getCompletas();

    assertEquals(1, res.size());
    verify(service).findCompletas();
  }

  @Test
  void getIngresos_returnsTotal() {
    IActividadService service = mock(IActividadService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ActividadController controller = new ActividadController(service, mapper);

    when(service.calcularIngresosTotales()).thenReturn(250.0);

    var resp = controller.getIngresos();

    assertEquals(200, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
    assertEquals(250.0, resp.getBody().get("total"));
  }
}
