package com.backend_spring.auth.mappers;

import com.backend_spring.auth.dto.*;
import com.backend_spring.auth.models.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioCreateRequest request);

    void updateEntityFromDto(UsuarioUpdateRequest request, @MappingTarget Usuario usuario);

    UsuarioResponse toResponse(Usuario usuario);
}
