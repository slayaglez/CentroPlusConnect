package proyecto.intermodular.adapters.in.controller;

import org.junit.jupiter.api.Test;

import com.centroplus.api.adapters.in.api.ReservaRequest;
import com.centroplus.api.adapters.in.api.ReservaResponse;
import com.centroplus.api.adapters.in.controller.ReservaController;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.business.interfaces.IReservaService;
import com.centroplus.api.domain.model.Reserva;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaControllerTest {

  @Test
  void getAll_returnsMappedResponses() {
    IReservaService service = mock(IReservaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaController controller = new ReservaController(service, mapper);

    Reserva reserva = new Reserva(1L, 2L, 3L, LocalDate.now(), "ACTIVA");
    ReservaResponse response = new ReservaResponse();
    response.setId(1L);

    when(service.findAll()).thenReturn(List.of(reserva));
    when(mapper.toResponse(reserva)).thenReturn(response);

    List<ReservaResponse> res = controller.getAll();

    assertEquals(1, res.size());
    assertEquals(1L, res.get(0).getId());
    verify(service).findAll();
    verify(mapper).toResponse(reserva);
  }

  @Test
  void getById_returns404_whenNotFound() {
    IReservaService service = mock(IReservaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaController controller = new ReservaController(service, mapper);

    when(service.findById(10L)).thenReturn(Optional.empty());

    var resp = controller.getById(10L);

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void getById_returns200_whenFound() {
    IReservaService service = mock(IReservaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaController controller = new ReservaController(service, mapper);

    Reserva reserva = new Reserva(1L, 2L, 3L, LocalDate.now(), "ACTIVA");
    ReservaResponse response = new ReservaResponse();
    response.setId(1L);

    when(service.findById(1L)).thenReturn(Optional.of(reserva));
    when(mapper.toResponse(reserva)).thenReturn(response);

    var resp = controller.getById(1L);

    assertEquals(200, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
  }

  @Test
  void create_returns201_andBody() {
    IReservaService service = mock(IReservaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaController controller = new ReservaController(service, mapper);

    ReservaRequest req = new ReservaRequest();
    req.setIdUsuario(1L);
    req.setIdActividad(2L);
    req.setFecha(LocalDate.now());
    req.setEstado("ACTIVA");

    Reserva domain = new Reserva(null, 1L, 2L, LocalDate.now(), "ACTIVA");
    Reserva saved = new Reserva(1L, 1L, 2L, LocalDate.now(), "ACTIVA");
    ReservaResponse response = new ReservaResponse();
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
    IReservaService service = mock(IReservaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaController controller = new ReservaController(service, mapper);

    when(service.deleteById(1L)).thenReturn(true);

    var resp = controller.delete(1L);

    assertEquals(204, resp.getStatusCode().value());
  }

  @Test
  void delete_returns404_whenNotExists() {
    IReservaService service = mock(IReservaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaController controller = new ReservaController(service, mapper);

    when(service.deleteById(99L)).thenReturn(false);

    var resp = controller.delete(99L);

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void cancelar_returns200_whenSuccess() {
    IReservaService service = mock(IReservaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaController controller = new ReservaController(service, mapper);

    when(service.cancelarReserva(1L)).thenReturn(true);

    var resp = controller.cancelar(1L);

    assertEquals(200, resp.getStatusCode().value());
  }

  @Test
  void cancelar_returns404_whenNotFound() {
    IReservaService service = mock(IReservaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaController controller = new ReservaController(service, mapper);

    when(service.cancelarReserva(99L)).thenReturn(false);

    var resp = controller.cancelar(99L);

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void getDisponibles_returnsMappedList() {
    IReservaService service = mock(IReservaService.class);
    CentroPlusMapper mapper = mock(CentroPlusMapper.class);
    ReservaController controller = new ReservaController(service, mapper);

    Reserva reserva = new Reserva(1L, 2L, 3L, LocalDate.now(), "ACTIVA");
    ReservaResponse response = new ReservaResponse();
    response.setId(1L);

    when(service.findDisponibles()).thenReturn(List.of(reserva));
    when(mapper.toResponse(reserva)).thenReturn(response);

    List<ReservaResponse> res = controller.getDisponibles();

    assertEquals(1, res.size());
    verify(service).findDisponibles();
  }
}
