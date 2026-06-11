package com.centroplus.api.adapters.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.centroplus.api.adapters.in.api.ActividadRequest;
import com.centroplus.api.adapters.in.api.ActividadResponse;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.business.interfaces.IActividadService;
import com.centroplus.api.domain.model.Actividad;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/actividades")
@Tag(name = "Actividades")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin
public class ActividadController {

  private final IActividadService service;
  private final CentroPlusMapper mapper;

  public ActividadController(IActividadService service, CentroPlusMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping
  @Operation(summary = "Obtener todas las actividades")
  public List<ActividadResponse> getAll() {
    return service.findAll().stream().map(mapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener actividad por ID")
  public ResponseEntity<ActividadResponse> getById(@PathVariable Long id) {
    return service.findById(id)
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Crear actividad")
  public ResponseEntity<ActividadResponse> create(@RequestBody ActividadRequest request) {
    Actividad created = service.create(mapper.toDomain(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Actualizar actividad (parcial)")
  public ResponseEntity<ActividadResponse> update(@PathVariable Long id, @RequestBody ActividadRequest request) {
    Actividad actividad = new Actividad();
    actividad.setNombre(request.getNombre());
    actividad.setTipoActividad(request.getTipoActividad());
    actividad.setDuracion(request.getDuracion());
    actividad.setPrecio(request.getPrecio());
    actividad.setPlazasMaximas(request.getPlazasMaximas());
    actividad.setPlazasOcupadas(request.getPlazasOcupadas());
    
    return service.update(id, mapper.toDomain(request))
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar actividad")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return service.deleteById(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @PostMapping("/{id}/reservar-plaza")
  @Operation(summary = "Reservar una plaza en la actividad")
  public ResponseEntity<Void> reservarPlaza(@PathVariable Long id) {
    return service.reservarPlaza(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().build();
  }

  @PostMapping("/{id}/cancelar-plaza")
  @Operation(summary = "Cancelar una plaza de la actividad")
  public ResponseEntity<Void> cancelarPlaza(@PathVariable Long id) {
    return service.cancelarPlaza(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().build();
  }

  @GetMapping("/completas")
  @Operation(summary = "Obtener actividades sin plazas disponibles")
  public List<ActividadResponse> getCompletas() {
    return service.findCompletas().stream().map(mapper::toResponse).toList();
  }

  @GetMapping("/ingresos")
  @Operation(summary = "Calcular ingresos totales del centro")
  public ResponseEntity<Map<String, Double>> getIngresos() {
    return ResponseEntity.ok(Map.of("total", service.calcularIngresosTotales()));
  }
}
