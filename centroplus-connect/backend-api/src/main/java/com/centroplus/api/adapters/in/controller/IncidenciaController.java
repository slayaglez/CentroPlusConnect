package com.centroplus.api.adapters.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.centroplus.api.adapters.in.api.IncidenciaRequest;
import com.centroplus.api.adapters.in.api.IncidenciaResponse;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.business.interfaces.IIncidenciaService;
import com.centroplus.api.domain.model.Incidencia;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/incidencias")
@Tag(name = "Incidencias")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin
public class IncidenciaController {

  private final IIncidenciaService service;
  private final CentroPlusMapper mapper;

  public IncidenciaController(IIncidenciaService service, CentroPlusMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping
  @Operation(summary = "Obtener todas las incidencias")
  public List<IncidenciaResponse> getAll() {
    return service.findAll().stream().map(mapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener incidencia por ID")
  public ResponseEntity<IncidenciaResponse> getById(@PathVariable Long id) {
    return service.findById(id)
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Crear incidencia")
  public ResponseEntity<IncidenciaResponse> create(@RequestBody IncidenciaRequest request) {
    Incidencia created = service.create(mapper.toDomain(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Actualizar incidencia (parcial)")
  public ResponseEntity<IncidenciaResponse> update(@PathVariable Long id, @RequestBody IncidenciaRequest request) {
    Incidencia incidencia = new Incidencia();
    incidencia.setIdUsuario(request.getIdUsuario());
    incidencia.setAsunto(request.getAsunto());
    incidencia.setDescripcion(request.getDescripcion());
    incidencia.setFecha(request.getFecha());
    incidencia.setEstado(request.getEstado());

    return service.update(id, mapper.toDomain(request))
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar incidencia")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return service.deleteById(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @PatchMapping("/{id}/estado")
  @Operation(summary = "Cambiar estado de la incidencia")
  public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
    String nuevoEstado = body.get("estado");
    if (nuevoEstado == null)
      return ResponseEntity.badRequest().build();
    return service.cambiarEstado(id, nuevoEstado)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/usuario/{idUsuario}")
  @Operation(summary = "Obtener incidencias de un usuario")
  public List<IncidenciaResponse> getByUsuario(@PathVariable Long idUsuario) {
    return service.findByUsuario(idUsuario).stream().map(mapper::toResponse).toList();
  }
}
