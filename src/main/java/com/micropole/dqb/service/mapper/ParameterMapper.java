package com.micropole.dqb.service.mapper;

import com.micropole.dqb.domain.*;
import com.micropole.dqb.service.dto.ParameterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Parameter and its DTO ParameterDTO.
 */
@Mapper(componentModel = "spring", uses = {ContextMapper.class})
public interface ParameterMapper extends EntityMapper<ParameterDTO, Parameter> {

    @Mapping(source = "context.id", target = "contextId")
    ParameterDTO toDto(Parameter parameter);

    @Mapping(source = "contextId", target = "context")
    Parameter toEntity(ParameterDTO parameterDTO);

    default Parameter fromId(Long id) {
        if (id == null) {
            return null;
        }
        Parameter parameter = new Parameter();
        parameter.setId(id);
        return parameter;
    }
}
