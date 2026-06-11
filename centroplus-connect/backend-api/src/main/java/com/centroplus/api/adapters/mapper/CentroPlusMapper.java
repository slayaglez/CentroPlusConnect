package com.centroplus.api.adapters.mapper;

import org.mapstruct.*;

import com.centroplus.api.adapters.in.api.*;
import com.centroplus.api.adapters.out.persistence.jpa.*;
import com.centroplus.api.domain.model.*;

@Mapper(componentModel = "spring")
public interface CentroPlusMapper {

  // Usuario
    Usuario toDomain(UsuarioRequest request);

  UsuarioResponse toResponse(Usuario usuario);

  UsuarioJpaEntity toJpa(Usuario usuario);

    Usuario toDomain(UsuarioJpaEntity entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomainFromRequest(UsuarioRequest request, @MappingTarget Usuario usuario);

  // Actividad
  Actividad toDomain(ActividadRequest request);

  ActividadResponse toResponse(Actividad actividad);

  ActividadJpaEntity toJpa(Actividad actividad);

  Actividad toDomain(ActividadJpaEntity entity);
  
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomainFromRequest(ActividadRequest request, @MappingTarget Actividad actividad);

  // Reserva
  Reserva toDomain(ReservaRequest request);

  ReservaJpaEntity toJpa(Reserva reserva);

  ReservaResponse toResponse(Reserva reserva);

  Reserva toDomain(ReservaJpaEntity entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomainFromRequest(ReservaRequest request, @MappingTarget Reserva reserva);

  // Incidencia
  Incidencia toDomain(IncidenciaRequest request);

  IncidenciaJpaEntity toJpa(Incidencia incidencia);

  IncidenciaResponse toResponse(Incidencia incidencia);
  
  Incidencia toDomain(IncidenciaJpaEntity entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomainFromRequest(IncidenciaRequest request, @MappingTarget Incidencia incidencia);
}
