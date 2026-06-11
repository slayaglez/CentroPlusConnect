package com.centroplus.api.adapters.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.centroplus.api.adapters.in.api.UsuarioRequest;
import com.centroplus.api.adapters.in.api.UsuarioResponse;
import com.centroplus.api.adapters.mapper.CentroPlusMapper;
import com.centroplus.api.business.interfaces.IUsuarioService;
import com.centroplus.api.domain.model.Usuario;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin
public class UsuarioController {

  private final IUsuarioService service;
  private final CentroPlusMapper mapper;

  public UsuarioController(IUsuarioService service, CentroPlusMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping
  @Operation(summary = "Obtener todos los usuarios")
  public List<UsuarioResponse> getAll() {
    return service.findAll().stream().map(mapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener usuario por ID")
  public ResponseEntity<UsuarioResponse> getById(@PathVariable Long id) {
    return service.findById(id)
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Crear usuario")
  public ResponseEntity<UsuarioResponse> create(@RequestBody UsuarioRequest request) {
    Usuario created = service.create(mapper.toDomain(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Actualizar usuario (parcial)")
  public ResponseEntity<UsuarioResponse> update(@PathVariable Long id, @RequestBody UsuarioRequest request) {
    Usuario usuario = new Usuario();
    usuario.setNombre(request.getNombre());
    usuario.setDni(request.getDni());
    usuario.setEmail(request.getEmail());
    usuario.setTelefono(request.getTelefono());
    usuario.setTipoUsuario(request.getTipoUsuario());

    return service.update(id, mapper.toDomain(request))
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar usuario")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return service.deleteById(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
