package com.centroplus.api.adapters.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.centroplus.api.adapters.in.api.ReservaRequest;
import com.centroplus.api.adapters.in.api.ReservaResponse;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.business.interfaces.IReservaService;
import com.centroplus.api.domain.model.Reserva;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@Tag(name = "Reservas")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin
public class ReservaController {

  private final IReservaService service;
  private final CentroPlusMapper mapper;

  public ReservaController(IReservaService service, CentroPlusMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping
  @Operation(summary = "Obtener todas las reservas")
  public List<ReservaResponse> getAll() {
    return service.findAll().stream().map(mapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener reserva por ID")
  public ResponseEntity<ReservaResponse> getById(@PathVariable Long id) {
    return service.findById(id)
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Crear reserva")
  public ResponseEntity<ReservaResponse> create(@RequestBody ReservaRequest request) {
    Reserva created = service.create(mapper.toDomain(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Actualizar reserva (parcial)")
  public ResponseEntity<ReservaResponse> update(@PathVariable Long id, @RequestBody ReservaRequest request) {
    Reserva reserva = new Reserva();
    reserva.setIdUsuario(request.getIdUsuario());
    reserva.setIdActividad(request.getIdActividad());
    reserva.setFecha(request.getFecha());
    reserva.setEstado(request.getEstado());

    return service.update(id, mapper.toDomain(request))
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar reserva")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return service.deleteById(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @PostMapping("/{id}/cancelar")
  @Operation(summary = "Cancelar una reserva (cambia estado a CANCELADA)")
  public ResponseEntity<Void> cancelar(@PathVariable Long id) {
    return service.cancelarReserva(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/disponibles")
  @Operation(summary = "Obtener reservas activas")
  public List<ReservaResponse> getDisponibles() {
    return service.findDisponibles().stream().map(mapper::toResponse).toList();
  }
}
